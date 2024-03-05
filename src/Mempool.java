import java.util.ArrayList;

public class Mempool {

    private ArrayList<Transaction> transactions;

    public Mempool() {
        transactions = new ArrayList<Transaction>();
    }
    public Mempool(ArrayList<Transaction> transactions){this.transactions = transactions;}

    public synchronized void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public synchronized void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public synchronized ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    public static synchronized ArrayList<Transaction> getTransactions(Blockchain chain, ArrayList<Transaction> transactions){
        ArrayList<Transaction> ret = new ArrayList<>();
        int count = 0;
        for(Transaction transac : transactions) {
            //and still not included in other blocks of the longest chain
            // in this case check if the transaction is in the last 3 blocks
            for (int i = chain.size() - 3; i < chain.size(); i++) {
                if (count<7 && ! chain.get(i).getTransactions().contains(transac)){
                    ret.add(transac);
                }
            }
        }
        return ret;
    }

    public synchronized ArrayList<Transaction> getTransactions(Blockchain chain){
        ArrayList<Transaction> ret = new ArrayList<>();
        int count = 0;
        for(Transaction transac : transactions) {
            //and still not included in other blocks of the longest chain
            for (int i = chain.size() - 3; i < chain.size(); i++) {
                if (count<=7 && !chain.get(i).getTransactions().contains(transac)){
                    ret.add(transac);
                }
            }
        }
        return ret;
    }

    public int size(){
        return transactions.size();
    }
}
