import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class Blockchain {
    public static int difficulty = 4;
    public static double reward = 1;
    public static ArrayList<Block> chain;
    public Block genesisBlock ;
    public static Mempool Pool;
    public static ArrayList<ArrayList<Block>> forks;
    private static Blockchain longest;
    Blockchain(Block genesisBlock, int difficulty){
        this.genesisBlock = genesisBlock;
        chain = new ArrayList<Block>();
        chain.add(genesisBlock);
        this.Pool = new Mempool(genesisBlock.getTransactions());
        this.difficulty = difficulty;
    }

    Blockchain(Block genesisBlock){
        this.genesisBlock = genesisBlock;
        chain = new ArrayList<Block>();
        chain.add(genesisBlock);
        this.Pool = new Mempool(genesisBlock.getTransactions());
    }

    public static Blockchain getLongestChain() {
        return longest;
    }

    public static Blockchain newChain() {
    }

    public static void addToMemoryPool(Transaction transaction) {
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

    public static double getReward() {
        return reward;
    }

    public static void addBlock(Block block){
        if(block.getHash().substring(0, difficulty).equals("0".repeat(difficulty))){
            for (ArrayList<Block> forkChain: forks) {
                if (forkChain.get(0).getHash().equals(block.getPreviousHash())){

                }
            }
        }
        chain.add(block);
    }

}
