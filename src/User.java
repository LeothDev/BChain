import Garbage.Blockchain;

import java.security.*;
import java.util.Random;
public class User extends Thread{

    protected static int wallet_adr = 10010; //static because every miner has a different wallet address

    protected static int Id = 40080;
    private PrivateKey private_key;
    public PublicKey public_key;

    private Blockchain longest_chain;

    private Wallet wallet;


   public User(){
       super();
       generate_keys();
       this.Id = ++Id;
       this.wallet = new Wallet(++wallet_adr);
       this.longest_chain = Blockchain.getLongestChain;
   }

    public User(String name){
        super(name);
        generate_keys();
        this.Id = ++Id;
        this.wallet = new Wallet(++wallet_adr);
        this.longest_chain = Blockchain.getLongestChain;
    }

    @Override
    public void run() {
        //while (true) {
        while(!isInterrupted()){
            try {
                // check if the user has enough money to perform the transaction
                if (wallet.get_balance() >= 1) {
                    // perform a transaction
                    do_transaction(longest_chain);

                    while (true) {
                        //List<Block> new_chain = receive_broadcast();
                        Blockchain new_chain = receive_broadcast();
                        if (new_chain != null) {
                            // choose the longest chain
                            if (new_chain.size() > longest_chain.size()) {
                                longest_chain = new_chain;
                            }
                            break;
                        }
                    }

                    // sleep for a random amount of time
                    Thread.sleep(generateRandomIntInRange(10, 200));
                }
                else {
                    System.out.println("User " + this.getId() + " does not have enough money to perform transaction.");
                    Thread.sleep(generateRandomIntInRange(10, 200));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized Blockchain receive_broadcast() {
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

    public PublicKey getPublicKey(){return public_key;}
    private PrivateKey getPrivateKey(){return private_key;}
    public int get_Id(){return this.Id;}

    // generate a random integer between min and max (inclusive)
    public static int generateRandomIntInRange(int min, int max) {
        if (min >= max) {
            System.out.println("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public synchronized void do_transaction(Blockchain blockchain) {
        // check if the user has enough money to perform the transaction
/*        if (wallet.get_balance() < 2) {
            System.out.println("User " + this.getId() + " does not have enough money to perform transaction.");

            return;
        }*/

        // generate a random wallet number for the recipient
        int recipientWalletNumber = generateRandomIntInRange(10010, wallet_adr);
        double TRANSACTION_AMOUNT = generateRandomIntInRange(0,1);

        // create the transaction
        Transaction transaction = new Transaction(this.getPublicKey(), this.get_Id(),recipientWalletNumber, TRANSACTION_AMOUNT);

        if (TRANSACTION_AMOUNT >= 0.5)
            // I'm doing a "big" transaction! let's give some lucky Miner bitcoins
            transaction.setFee(0.1);


        try {
            // sign the transaction with the user's private key
            byte[] signature = DigitalSignature.sign(transaction.toString(), this.getPrivateKey());

            // add the transaction to the memory pool
            blockchain.addToMemoryPool(transaction, signature);

            System.out.println("User " + this.getId() + " sent " + TRANSACTION_AMOUNT + " bitcoin to wallet " + recipientWalletNumber);

            // notify all waiting threads that a new transaction has been added to the memory pool
            notifyAll();
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.out.println("User " + this.getId() + " could not sign the transaction.");
        }
    }

}
