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
        execute();
        this.Pool = new Mempool(genesisBlock.getTransactions());
        this.difficulty = difficulty;
    }

    Blockchain(Block genesisBlock){
        this.genesisBlock = genesisBlock;
        chain = new ArrayList<Block>();
        chain.add(genesisBlock);
        execute();
        this.Pool = new Mempool(genesisBlock.getTransactions());
    }

    public static void addBlock(Block block){
        Blockchain selected;
        if(block.getHash().substring(0, difficulty).equals("0".repeat(difficulty))){
            for(ArrayList<Block> fork : forks){
                //check which blockchain is the block adding to
                if(fork.get(fork.size()-1).getHash().equals(block.getPrev_hash())){
                    if(fork.size()+1 > longest.size()) {
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
                    updateChain(block);
                    break;
                }
            }


        }
    }

    public static Blockchain getLongestChain() {
        return longest;
    }
    private void execute(){
        for(Transaction t : genesisBlock.getTransactions()){
            t.processTransaction();
        }
    }

    public static Blockchain newChain() {
        return longest;
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

    private static void updateChain(Block b ){
        longest.chain.add(b);
    }
}
