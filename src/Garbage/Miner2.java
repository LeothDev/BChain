package Garbage;

import java.util.ArrayList;
import java.util.List;

public class Miner2 implements Runnable {
    private final int id;
    private final Blockchain blockchain;
    private final List<Transaction> transactionPool;
    private final int miningReward;
    private final int difficulty;

    public Miner2(int id, Blockchain blockchain, int miningReward, int difficulty) {
        this.id = id;
        this.blockchain = blockchain;
        this.miningReward = miningReward;
        this.difficulty = difficulty;
        this.transactionPool = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactionPool.add(transaction);
    }

    public void run() {
        while (true) {
            // Check if any new transactions have been added to the transaction pool
            List<Transaction> transactionsToInclude = new ArrayList<>();
            for (Transaction transaction : transactionPool) {
                if (blockchain.isValidTransaction(transaction)) {
                    transactionsToInclude.add(transaction);
                }
            }

            // Attempt to mine a new block with the valid transactions
            Block newBlock = blockchain.mineBlock(transactionsToInclude, miningReward, difficulty);

            // Check if the new block is valid and the longest chain
            if (newBlock != null && blockchain.addBlock(newBlock)) {
                // Broadcast the new block to other nodes
                broadcastBlock(newBlock);
            }

            try {
                // Sleep for a short period of time before attempting to mine a new block
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastBlock(Block block) {
        // In a real implementation, this method would send the new block to other nodes on the network
        // Add the block to the blockchain
        longestBlockchain.addBlock(block);
        // Notify all threads that a new block has been added to the blockchain
        synchronized(this) {
            notifyAll();
            }
            System.out.println("Miner " + id + " broadcasting block: " + block);
        }


    }

