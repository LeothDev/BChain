import java.util.ArrayList;
public class TestBlockChain {
    public static void main(String[] args){
        ArrayList<User> utenti = new ArrayList<>();

        User Nakamoto = new User("Satoshi", 1000);
        String prev_hash = ""; // Empty string or a predefined value
        ArrayList<Transaction> ts = new ArrayList<Transaction>();
        Transaction InitialTransaction =new Transaction(Nakamoto.getWallet_adr(), 1000);
        ts.add(InitialTransaction);
        String Merkle_root = new MerkleTree().getRoot(ts);
        int height = 0;
        long timestamp = System.currentTimeMillis();
        Block genesis = new Block(prev_hash,Merkle_root,ts,height,timestamp);
        Blockchain chain = new Blockchain(genesis);

        Nakamoto.start();
        utenti.add(Nakamoto);

        User n1 = new User("n1", 10);
        utenti.add(n1);
        User n2 = new User("n2", 10);
        utenti.add(n2);
        User n3 = new User("n3", 10);
        utenti.add(n3);
        User n4 = new User("n4", 20);
        utenti.add(n4);
        User n5 = new User("n5", 30);
        utenti.add(n5);
        Miner m1 = new Miner("m1");
        utenti.add(m1);
        Miner m2 = new Miner("m2");
        utenti.add(m2);
        Miner m3 = new Miner("m3");
        utenti.add(m3);

        try {
            Thread.currentThread().sleep(5000);
        }catch(InterruptedException e){}

        for(User utente : utenti){
            utente.interrupt();
        }

        for(User actives: utenti){
            System.out.println(actives.get_Id() + ": " + "balance = " + actives.getBalance());
        }
        System.out.println("System finished");


    }
}
