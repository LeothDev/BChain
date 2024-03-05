import java.util.ArrayList;

public class Block {
    private double nonce;

    private int MinerWalletAddress;
    private int MinerId;
    private ArrayList<Transaction> transactions;
    private String prev_hash;
    private long timeStamp;
    private String hash;
    private String Merkle_root;
    private int height;

    Block(){
        prev_hash = null;
        nonce = 0;
        transactions = new ArrayList<Transaction>();
    }
    Block(String prev_hash, String Merkle_root, ArrayList<Transaction> ts, int height){
        this.prev_hash = prev_hash;
        this.Merkle_root = Merkle_root;
        this.transactions = ts;
        this.nonce = 0;
        this.height = height;
    }
    Block(String prev_hash, String Merkle_root, ArrayList<Transaction> ts, int height, long timestamp){
        this.prev_hash = prev_hash;
        this.Merkle_root = Merkle_root;
        this.transactions = ts;
        this.nonce = 0;
        this.height = height;
        this.timeStamp = timestamp;
    }

    Block(String prev_hash, String Merkle_root, ArrayList<Transaction> ts, int MinerWallet, int Id, int height){
        this.prev_hash = prev_hash;
        this.Merkle_root = Merkle_root;
        this.transactions = ts;
        this.nonce = 0;
        this.MinerWalletAddress = MinerWallet;
        this.MinerId = Id;
        this.height = height;

    }

    public String getHash() {
        try {
            if (prev_hash == null)
                throw new InvalidHashException();
/*            if (prev_hash.isEmpty())
                //genesis block*/

            String to_hash = prev_hash + Merkle_root + String.valueOf(nonce) + String.valueOf(height);
            //update the field hash_of_current_block and return it
            this.hash = HashFunction.applySha256(to_hash);
            return hash;

        } catch (InvalidHashException e) {return null;}
    }

    public void addTransaction(Transaction t){
        transactions.add(t);
    }

    public void clearTransactions(){
        this.transactions.clear();
    }
    public ArrayList<Transaction> getTransactions(){return this.transactions;}


    public void setNonce(double nonce) {
        this.nonce = nonce;
    }
    public double getNonce(){
        return nonce;
    }

    public void setTimeStamp(long timestamp) {
        this.timeStamp = timestamp;
    }
    public long getTimeStamp(){
        return timeStamp;
    }

    public void setMinerWallet(int walletAddress) {
        this.MinerWalletAddress = walletAddress;
    }

    public void setMinerId(int id) {
        this.MinerId = id;
    }

    public void setPreviousHash(String prev_hash){
        this.prev_hash = prev_hash;
    }

    public void setMerkleRoot(String root) {
        this.Merkle_root = root;
    }
    public int getHeight(){
        return this.height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public String getPrev_hash(){
        return prev_hash;
    }

    public int getMinerWalletAddress(){return MinerWalletAddress;}
    public int getMinerId(){return MinerId;}
}
