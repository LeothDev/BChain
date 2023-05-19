
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.*;
/*import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;*/

public class Miner extends User {
    // above functionalities are already inherited from user by using extends User

    private ArrayList<Block> longestBlockchain;
    /*
    private PrivateKey private_key;
    public PublicKey public_key;
    private Wallet wallet;*/
    private ArrayList<Transaction> transactionPool;

    private Lock lock;
    private Condition broadcasted;

    Miner() {
        super();
        transactionPool = new ArrayList<Transaction>();
        longestBlockchain = longest_chain;
        /*generate_keys();
        this.Id = ++super.Id;
        this.wallet = new Wallet(++super.wallet_adr);
        lock = new ReentrantLock();
        broadcasted = lock.newCondition();
        this.longestBlockchain = Blockchain.getLongestChain();
        above functionalities are already done by calling the super() constructor*/
    }

    Miner(String name) {
        super(name);
        transactionPool = new ArrayList<Transaction>();
        longestBlockchain = longest_chain;
    }
    Miner(String name, double initial_value) {
        super(name, initial_value);
        transactionPool = new ArrayList<Transaction>();
        longestBlockchain = longest_chain;
    }

    public void addTransaction(Transaction transaction) {
        transactionPool.add(transaction);
    }

    private void broadcastBlock(Block block) {
/*        // In a real implementation, this method would send the new block to other nodes on the network
        // Add the block to the blockchain
        //longestBlockchain.addBlock(block);
        // acquire the lock and signal all waiting threads
        lock.lock();
        try {
            // add the block to the blockchain
            longestBlockchain.addBlock(block);
            // signal all waiting threads that a new block has been added
            broadcasted.signalAll();
            System.out.println("Miner " + Id + " broadcasting block: " + block);
        } finally {
            lock.unlock();
        }*/
        // Notify all threads that a new block has been added to the blockchain

        //synchronization should be on the blockchain object
        //Blockchain.addNewBlock(block);
        System.out.println("Miner " + Id + " broadcasting block: " + block);
    }

    @Override
    public void run() {
            while (!isInterrupted()) {
                // get transaction(s) from mempool class
                // create a block of class Block
                Block newBlock;
                do {
                    newBlock = mineBlock();
                } while (newBlock == null && !isInterrupted()); //if newBlock is null -> means mineBlock() failed

                if(newBlock!=null)
                    System.out.println("Oh yeah I mined a Block!");
                if(isInterrupted())
                    break;

                // If the miner is the first:  claim the reward(the system does)and broadcast the block
                ArrayList<Block> Checkchain = Blockchain.getLongestChain();
                if (Checkchain.size() <= 1 + longestBlockchain.size()) {
                    Blockchain.addBlock(newBlock);
                    longestBlockchain = Blockchain.getLongestChain();
                    // update transaction pool (done by the Blockchain by removing included transactions)
                    broadcastBlock(newBlock);
                    System.out.println("Block mined: " + newBlock.getHash());
                    System.out.println("Nonce value: " + newBlock.getNonce());
                    System.out.println("Mining time: " + newBlock.getTimeStamp() + " ms");
                } else
                    longestBlockchain = Checkchain;
                // else: in the meantime someone succeded, change the block(pointer to prev block)
                // this can be done be comparing latestBroadcasted block with mine
            }
        System.out.println("Miner " + getName() + " quit the system" );
    }

    public boolean verifyTransaction(Transaction transaction) {
        // in a real implementation, this method would verify the transaction's signature and ensure it's valid
        // for simplicity, we assume all transactions in the pool are valid
        boolean verified = false;
        try {
            verified = DigitalSignature.verify(transaction.toString(), transaction.getSignature(), transaction.getPublicKey());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {}
        return verified;
    }

    public void clear_pool(){
        transactionPool.clear(); // method of list
    }

    private Block mineBlock(){
        //probably getTransactions needs to be a method of class Blockchain since the pool is part
        //of the Blockchain system
        //transactionPool = Mempool.getTransactions();

        //different transactionpool(Transactions to include for every miner)
        transactionPool = new ArrayList<Transaction>(Blockchain.getTransactions());

        //Check if they are valid
        Block mining_block = new Block();
        //Block mining_block;
        ArrayList<Block> Checkchain = Blockchain.getLongestChain();

        mining_block.setMinerWallet(wallet.getWallet_address());
        mining_block.setMinerId(get_Id());

        do {
            if (Checkchain!=null && Checkchain.size() > longestBlockchain.size()) {
                //if there is already a longer chain (someone succedded before me)
                longestBlockchain = Checkchain;
                ArrayList<Transaction> current_to_validate = Blockchain.getTransactions();
                for(Transaction t: transactionPool){
                    if(!current_to_validate.contains(t))
                        transactionPool.remove(t);
                }
            } else {
                transactionPool = new ArrayList<Transaction>(Blockchain.getTransactions(transactionPool));
            }
        }while(transactionPool.size()<7 && !isInterrupted());
        if(isInterrupted())
            return null;
        //accumulate 8 transactions to start a block (first one is the one to receive rewards - coinbase)

        //System.out.println("I am Miner and now I accumulated: " + transactionPool.size() + " transactions");
        String mes = "I am Miner and now I accumulated: " + transactionPool.size() + " transactions";
        Blockchain.printMessages(mes);
        double total_fees = 0;

        for(Transaction transac : transactionPool){
            try {
                if (DigitalSignature.verify(transac.toString(), transac.getSignature(), transac.getPublicKey())) {
                    //check if the transaction is valid
                    mining_block.addTransaction(transac);
                    total_fees += transac.getFee();
                }
                //Block should have a list attribute containing transactions
            }catch(NoSuchAlgorithmException|SignatureException|InvalidKeyException e){}
        }
        double rewards = Blockchain.getReward() + total_fees;
        Transaction coinbase = new Transaction(wallet.getWallet_address(),rewards);
        try {
            coinbase.setSignature(DigitalSignature.sign(coinbase.toString(), private_key));
        }catch(NoSuchAlgorithmException|InvalidKeyException|SignatureException e){}
        transactionPool.add(0, coinbase);

        //set the campus: Hash of previous block(Hash of the last block)
        mining_block.setPreviousHash(longestBlockchain.get(longestBlockchain.size()-1).getHash());
        System.out.println(longestBlockchain.get(longestBlockchain.size()-1).getHash());
        MerkleTree tree = new MerkleTree();
        String root = tree.getRoot(transactionPool);
        mining_block.setMerkleRoot(root);
        mining_block.setHeight(longestBlockchain.size()-1);

        //now we can calculate proof of work -> when calculating check if there is some broadcast
        double nonce = calculateProofOfWork(mining_block);
        if(nonce == -1) {
            // the miner has failed, because someone succeded before him
            longestBlockchain = Blockchain.getLongestChain();
            return null;
        }
        mining_block.setNonce(nonce);
        long timestamp = System.currentTimeMillis() / 1000; // convert to seconds
        mining_block.setTimeStamp(timestamp);
        return mining_block;
    }
    private double calculateProofOfWork(Block block){
        //check if a new block arrives when calculating proof of work
        block.setNonce(0);
        double nonce = 0;
        //double hash_of_block = mining_block.getHash();

        // Start mining process
        long startTime = new Date().getTime();
        //String hash = String.valueOf(hash_of_block);
        String hash = block.getHash();
        while (!hash.substring(0, Blockchain.difficulty).equals("0".repeat(Blockchain.difficulty))) {
            if(tooSlow())
                return -1;
            nonce++;
            block.setNonce(nonce);
            //hash = String.valueOf(block.getHash());
            //hash = applySha256(hash);
            hash = block.getHash();
        }
        long endTime = new Date().getTime();

/*        // Print results
        System.out.println("Block mined: " + hash);
        System.out.println("Nonce value: " + nonce);
        System.out.println("Mining time: " + (endTime - startTime) + " ms");*/
        //block.setHash(applySha256(hash_of_block));
        return nonce;
    }

    private boolean tooSlow(){
        return Blockchain.getLongestChain().size() > longestBlockchain.size();
    }
    private static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}



