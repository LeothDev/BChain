import java.util.ArrayList;
import java.util.Random;

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

        //Nakamoto.start();
        utenti.add(Nakamoto);


        /*Malicious mali1 = new Malicious("mali1");
        utenti.add(mali1);
        Malicious mali2 = new Malicious("mali2");
        utenti.add(mali2);*/

        /*User n1 = new User("n1", 10);
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
        utenti.add(m3);*/

        String username = "";
        Random gen = new Random();
        for(int i = 0; i< 50; i++){
            username = "n" + i;
            utenti.add(new User(username,gen.nextInt(30)));
        }
        for(int i = 0; i< 30; i++){
            username = "m" + i;
            utenti.add(new Miner(username,gen.nextInt(20)));
        }
        for(int i = 0; i< 3; i++){
            username = "mali" + i;
            utenti.add(new Malicious(username,gen.nextInt(20)));
        }


        for(User utente : utenti){
            utente.start();
        }

        try {
            Thread.currentThread().sleep(10000);
        }catch(InterruptedException e){}

        for(User utente : utenti){
            utente.interrupt();
        }

        for(User actives: utenti){
            //System.out.println(actives.get_Id() + ": " + "balance = " + actives.getBalance());
            System.out.println(actives.getName() + ": " + "balance = " + (User.getWalletByAddress(actives.getWallet_adr())).get_balance());
        }

        /*System.out.println("longest chain has size: " + Blockchain.getLongestChain().size());*/

        try {
            Thread.currentThread().sleep(10);
        } catch (InterruptedException e) {}

        System.out.println("longest chain has size: " + Blockchain.getLongestChain().size());
        System.out.println("System finished");

        /*Object lock = new Object();
        Thread.currentThread().notify();
        lock.notify();
        synchronized (lock){
            //Thread.currentThread().notify();
            lock.notify();
        }*/


        return;





    }
}

