import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Blockchain {
    public static int difficulty = 2;
    public static double reward = 1;
    public static ArrayList<Block> chain;
    public Block genesisBlock ;
    public static Mempool Pool;
    public static ArrayList<ArrayList<Block>> forks;
    private static ArrayList<Block> longest;
    Blockchain(Block genesisBlock, int difficulty){
        this.genesisBlock = genesisBlock;
        chain = new ArrayList<Block>();
        chain.add(genesisBlock);
        execute();
        this.Pool = new Mempool(genesisBlock.getTransactions());
        forks = new ArrayList<>();
        forks.add(chain);
        this.difficulty = difficulty;

    }

    Blockchain(Block genesisBlock){
        this.genesisBlock = genesisBlock;
        chain = new ArrayList<Block>();
        chain.add(genesisBlock);
        execute();
        this.Pool = new Mempool(genesisBlock.getTransactions());
        forks = new ArrayList<>();
        forks.add(chain);
    }

    public static synchronized void addBlock(Block block){
        Blockchain selected;
        if(block.getHash().substring(0, difficulty).equals("0".repeat(difficulty))){
            for(ArrayList<Block> fork : forks){
                //check which blockchain is the block adding to
                //System.out.println("the hash of last block of this blockchain: " + (fork.get(fork.size()-1)).getHash());
                //System.out.println("prev_hash field of the added block: " + block.getPrev_hash());
                if((fork.get(fork.size()-1).getHash()).equals(block.getPrev_hash())){
                    if(fork.size()+1 > chain.size()) {
                        //if the chain is the longest one, validate transactions and reward miner
                        ArrayList<Transaction> transactions = block.getTransactions();
                        int MinerId = block.getMinerId();
                        Wallet MinerWallet = User.getWalletByAddress(block.getMinerWalletAddress());
                        MinerWallet.updateBalance(reward);
                        int fees = 0;
                        transactions.remove(0); //the first transaction is the coinbase - reward
                        for (Transaction t : block.getTransactions()) {
                            t.processTransaction();
                            fees += t.getFee();
                            Pool.removeTransaction(t);
                        }
                        MinerWallet.updateBalance(fees);
                    }

                    fork.add(block);
                    updateChain(fork);
                    break;
                }
            }


        }
    }

    public static ArrayList<Block> getLongestChain() {
        return chain;
    }

    private void execute(){
        for(Transaction t : genesisBlock.getTransactions()){
            t.processTransaction();
        }
    }

    public static ArrayList<Block> newChain() {
        return chain;
    }

    public static synchronized void addToMemoryPool(Transaction transaction) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        if(transaction.verifySignature())
            Pool.addTransaction(transaction);
    }

    public int size(){
        return chain.size();
    }
    public Block get(int n){
        if(n<size() && n>=0)
            return chain.get(n);
        return null;
    }

    public static ArrayList<Transaction> getTransactions(){
        return Pool.getTransactions();
    }
    //Overloaded for miners
    public static synchronized ArrayList<Transaction> getTransactions(ArrayList<Transaction> minerPool){
        if(Pool.size()<=7)
            return Pool.getTransactions();
        ArrayList<Transaction> ret = new ArrayList<Transaction>(7);
        for (Transaction t: Pool.getTransactions()) {
            if(ret.size()<7)
                ret.add(t);
            else
                break;
        }
        return ret;
    }

    public static double getReward() {
        return reward;
    }

    public static synchronized void printMessages(String s){
        System.out.println(s);
    }
    private static void updateChain(ArrayList<Block> newchain){
        chain = newchain;
    }
}
