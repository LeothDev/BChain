package Garbage;

import java.util.ArrayList;
import java.util.List;

public class Miner {
    private int id;
    private List<Transaction> transactionPool;
    private Blockchain longestBlockchain;

    public Miner(int id) {
        this.id = id;
        this.transactionPool = new ArrayList<>();
        this.longestBlockchain = new Blockchain();
        this.longestBlockchain.addBlock(new Block(0, "", 0, new ArrayList<>()));
    }

    public void addTransaction(Transaction transaction) {
        this.transactionPool.add(transaction);
    }

    public void mineBlock() {
        // get the longest blockchain from the network
        Blockchain networkLongestBlockchain = getNetworkLongestBlockchain();

        // add all transactions in the pool to a candidate block
        Block candidateBlock = new Block(networkLongestBlockchain.getLatestBlock().getIndex() + 1,
                networkLongestBlockchain.getLatestBlock().getHash(),
                System.currentTimeMillis(),
                new ArrayList<>(this.transactionPool));

        // mine the block until its hash satisfies the difficulty requirement
        while (!candidateBlock.getHash().substring(0, networkLongestBlockchain.getDifficulty()).equals(networkLongestBlockchain.getPrefix())) {
            candidateBlock.incrementNonce();
        }

        // add the block to the longest blockchain
        this.longestBlockchain.addBlock(candidateBlock);

        // remove the transactions in the block from the transaction pool
        for (Transaction transaction : candidateBlock.getTransactions()) {
            this.transactionPool.remove(transaction);
        }

        // broadcast the new longest blockchain to the network
        broadcastLongestBlockchain();
    }

    private Blockchain getNetworkLongestBlockchain() {
        // in a real implementation, this method would get the longest blockchain from the network
        // for simplicity, we just return the miner's longest blockchain
        return this.longestBlockchain;
    }

    /*private void broadcastLongestBlockchain() {
        // in a real implementation, this method would broadcast the new longest blockchain to the network
        // for simplicity, we just print a message
        System.out.println("Miner " + this.id + " broadcasted a new longest blockchain with length " + this.longestBlockchain.getLength());
    }*/
    public void broadcastBlock(Block block) {
        // Add the block to the blockchain
        longestBlockchain.addBlock(block);
        System.out.println("Miner " + this.id + " broadcasted a new longest blockchain with length " + this.longestBlockchain.getLength());

        // Notify all threads that a new block has been added to the blockchain
        synchronized(this) {
            notifyAll();
        }
    }

    public void verifyTransaction(Transaction transaction) {
        // in a real implementation, this method would verify the transaction's signature and ensure it's valid
        // for simplicity, we assume all transactions in the pool are valid
    }

    public void updateLongestBlockchain(Blockchain newLongestBlockchain) {
        // in a real implementation, this method would update the miner's longest blockchain based on the new longest blockchain received from the network
        // for simplicity, we just set the miner's longest blockchain to the received blockchain
        this.longestBlockchain = newLongestBlockchain;
    }
}

