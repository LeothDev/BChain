
import java.security.*;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
public class User extends Thread{
    protected static int total_wallet_number = 10010;//static because every miner has a different wallet address
    protected int wallet_adr;

    protected static int Id = 40080;
    protected int my_id;
    protected PrivateKey private_key;
    protected PublicKey public_key;

    protected ArrayList<Block> longest_chain;

    protected Wallet wallet;
    protected static HashMap<Integer, Wallet> walletMap = new HashMap<>();


   public User(){
       super();
       generate_keys();
       this.my_id = ++Id;
       this.wallet_adr = ++total_wallet_number;
       this.wallet = new Wallet(wallet_adr);
       this.longest_chain = Blockchain.getLongestChain();
       walletMap.put(wallet_adr, this.wallet);
   }



    public User(String name){
        super(name);
        generate_keys();
        this.my_id = ++Id;
        this.wallet_adr = ++total_wallet_number;
        this.wallet = new Wallet(wallet_adr);
        this.longest_chain = Blockchain.getLongestChain();
        walletMap.put(wallet_adr, this.wallet);
    }

    public User(String name, double initial_value){
        super(name);
        generate_keys();
        this.my_id = ++Id;
        this.wallet_adr = ++total_wallet_number;
        this.wallet = new Wallet(wallet_adr);
        this.longest_chain = Blockchain.getLongestChain();
        walletMap.put(wallet_adr, this.wallet);
        wallet.updateBalance(initial_value);
    }

    @Override
    public void run() {

        try {
            while(!isInterrupted() && !Thread.currentThread().isInterrupted()){
            // check if the user has enough money to perform the transaction
            if (wallet.get_balance() >= 5) {
                // perform a transaction
                do_transaction();


                //List<Block> new_chain = receive_broadcast();
                ArrayList<Block> new_chain = receive_broadcast();
                if (new_chain != null && new_chain.size() > longest_chain.size()) {
                    // choose the longest chain
                        // check if the last block is valid
                        if(verify_validity(new_chain.get(new_chain.size()-1)))
                            //update the blockchain
                            longest_chain = new_chain;
                }

                // sleep for a random amount of time
                Thread.sleep(generateRandomIntInRange(10, 200));
            }
            else {
                System.out.println("User " + this.getId() + " does not have enough money to perform transaction.");
                Thread.sleep(generateRandomIntInRange(10, 200));
            }

        }
        }catch (InterruptedException e) {
            System.out.println("User " + getName() + " quit the system" );
        }
    }

    public synchronized ArrayList<Block> receive_broadcast() {
       //return Blockchain.getLongestChain();
       return Blockchain.newChain();
    }
/*        while (!received_broadcast) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        received_broadcast = false;
        Blockchain received_blockchain = new Blockchain(blocks);
        blocks.clear();
        return received_blockchain;
    }*/

    public boolean verify_validity(Block block){
        return block.getHash().substring(0, Blockchain.difficulty).equals("0".repeat(Blockchain.difficulty));
    }
    protected void generate_keys(){
       try {
           KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
           // throws NoSuchAlgorithm Exception if no Provider supports the specified algorithm
           // and throws NullPointerException if the specified algorithm is null
           // since we are only using RSA instances, there will be no problem, but still we need to handle
           // the Exception in a try catch block
           keyGen.initialize(2048); //parameter = keysize in number of bits
           KeyPair keyPair = keyGen.generateKeyPair();
           this.private_key = keyPair.getPrivate();
           this.public_key = keyPair.getPublic();
       }
       catch(NoSuchAlgorithmException| NullPointerException e){}
        // Print the generated keys
        //System.out.println("Private key: " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        //System.out.println("Public key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    }

    public double getBalance(){
        return wallet.get_balance();
    }

    public PublicKey getPublicKey(){return public_key;}
    protected PrivateKey getPrivateKey(){return private_key;}
    public int get_Id(){return my_id;}

    // generate a random integer between min and max (inclusive)
    public static int generateRandomIntInRange(int min, int max) {
        if (min >= max) {
            System.out.println("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static Wallet getWalletByAddress(int walletAddress) {
        return walletMap.get(walletAddress);
    }
    public int getWallet_adr(){return wallet_adr;}

    public synchronized void do_transaction() {
        if(longest_chain == null)
            longest_chain = Blockchain.getLongestChain();
        // check if the user has enough money to perform the transaction
/*        if (wallet.get_balance() < 2) {
            System.out.println("User " + this.getId() + " does not have enough money to perform transaction.");

            return;
        }*/

        // generate a random wallet number for the recipient
        Integer recipientWalletNumber = generateRandomIntInRange(10011, total_wallet_number);
        double TRANSACTION_AMOUNT = generateRandomIntInRange(1,5);

        // create the transaction
        //debugging statement
        //System.out.println("I am User " + my_id + " sending money to wallet: " + recipientWalletNumber);
        //System.out.println("Which represents Wallet object: " + getWalletByAddress(recipientWalletNumber));
        Transaction transaction = new Transaction(this.getPublicKey(), this.get_Id(),
                TRANSACTION_AMOUNT,wallet, getWalletByAddress(recipientWalletNumber));

        //Transaction transaction = new Transaction(this.getPublicKey(), this.get_Id(),recipientWalletNumber, TRANSACTION_AMOUNT);

        if (TRANSACTION_AMOUNT >= 2)
            // I'm doing a "big" transaction! let's give some lucky Miner bitcoins
            transaction.setFee(0.1);


        try {
            // sign the transaction with the user's private key
            byte[] signature = DigitalSignature.sign(transaction.toString(), this.getPrivateKey());
            transaction.setSignature(signature);


            // add the transaction to the memory pool, only if it is valid
            //and also not double spending -> UTXO unspend transaction output
            if(wallet.get_UTXO() >= transaction.getFee() + TRANSACTION_AMOUNT) {
                Blockchain.addToMemoryPool(transaction);
                /*System.out.println("Now I have done a transaction, the MemPool has " +
                        Blockchain.getTransactions().size() + " Transactions inside");*/
                String mes = "Now I have done a transaction, the MemPool has " +
                        Blockchain.getTransactions().size() + " Transactions inside";
                Blockchain.printMessages(mes);
                System.out.println("my blockchain size is: " + longest_chain.size());
                wallet.updateUTXO(transaction.getFee() + TRANSACTION_AMOUNT);

                System.out.println("User " + this.getId() + " sent " + TRANSACTION_AMOUNT + " bitcoin to wallet " + recipientWalletNumber);

                // notify all waiting threads that a new transaction has been added to the memory pool
                //notifyAll(); in addToMemoryPool
            }
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.out.println("User " + this.getId() + " could not sign the transaction.");
        }

    }

}
