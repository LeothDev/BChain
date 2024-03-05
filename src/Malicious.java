import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;

public class Malicious extends Miner{
    ArrayList<Block> my_chain = new ArrayList<>();
    Malicious(){
        super(); //super is static, it will refer to the constructor of class Miner
        my_chain.add(Blockchain.getGenesisBlock());
    }
    Malicious(String name){
        super(name);
        my_chain.add(Blockchain.getGenesisBlock());
    }

    Malicious(String name, int initialvalue){
        super(name, initialvalue);
        my_chain.add(Blockchain.getGenesisBlock());
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
                System.out.println("malicious chain is now at pregess: " + my_chain.size() + " Blocks" + "-----------" +
                        "------------------------------------------------------");
            if(isInterrupted())
                break;
            // a malicious doesn't update his chain to longest: he uses the chain that he favours
            //and try to make it longer than any existing chain
            // update transaction pool (done by the Blockchain by removing included transactions)
            broadcastBlock(newBlock);
            Blockchain.addBlock(newBlock);
            my_chain.add(newBlock);
            System.out.println("Block mined: " + newBlock.getHash());
            System.out.println("Nonce value: " + newBlock.getNonce());
            System.out.println("Mining time: " + newBlock.getTimeStamp() + " ms");
        }
        System.out.println("Malicious " + getName() + " quit the system" );
    }
    private Block mineBlock(){

        transactionPool = new ArrayList<Transaction>(Blockchain.getTransactions());

        //Check if they are valid
        Block mining_block = new Block();
        //Block mining_block;

        mining_block.setMinerWallet(wallet.getWallet_address());
        mining_block.setMinerId(get_Id());

        do {
            /*ArrayList<Transaction> current_to_validate = Blockchain.getTransactions();
            for(Transaction t: transactionPool){
                if(!current_to_validate.contains(t))
                    transactionPool.remove(t);
            }*/
            transactionPool = new ArrayList<Transaction>(Blockchain.getTransactions(transactionPool));
        }while(transactionPool.size()<7 && !isInterrupted());
        //accumulate 8 transactions to start a block (first one is the one to receive rewards - coinbase)

        //System.out.println("I am Miner, and now I accumulated: " + transactionPool.size() + " transactions");
        String mes = "I am Malicious and now I accumulated: " + transactionPool.size() + " transactions";
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
            }catch(NoSuchAlgorithmException | SignatureException | InvalidKeyException e){
                System.out.println("Exception occured in mineBlock() while checking signatures");
            }
        }
        double rewards = Blockchain.getReward() + total_fees;
        Transaction coinbase = new Transaction(wallet.getWallet_address(),rewards);
        try {
            coinbase.setSignature(DigitalSignature.sign(coinbase.toString(), private_key));
        }catch(NoSuchAlgorithmException|InvalidKeyException|SignatureException e){}
        transactionPool.add(0, coinbase);

        //set the campus: Hash of previous block(Hash of the last block)
        mining_block.setPreviousHash(my_chain.get(my_chain.size()-1).getHash());
        //System.out.println(my_chain.get(my_chain.size()-1).getHash());
        MerkleTree tree = new MerkleTree();
        String root = tree.getRoot(transactionPool);
        mining_block.setMerkleRoot(root);
        mining_block.setHeight(my_chain.size()-1);

        //now we can calculate proof of work -> when calculating check if there is some broadcast
        double nonce = calculateProofOfWork(mining_block);
        mining_block.setNonce(nonce);
        long timestamp = System.currentTimeMillis() / 1000; // convert to seconds
        mining_block.setTimeStamp(timestamp);
        return mining_block;
    }
    private double calculateProofOfWork(Block block){
        //check if a new block arrives when calculating proof of work
        block.setNonce(0);
        double nonce = 0;

        // Start mining process
        long startTime = new Date().getTime();
        //String hash = String.valueOf(hash_of_block);
        String hash = block.getHash();
        while (!hash.substring(0, Blockchain.difficulty).equals("0".repeat(Blockchain.difficulty))) {
            nonce++;
            block.setNonce(nonce);
            hash = block.getHash();
        }
        long endTime = new Date().getTime();
        return nonce;
    }
}


