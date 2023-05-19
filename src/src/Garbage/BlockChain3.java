/*
package Garbage;


//the following code is written by CHATGPT,(mostly every line of code in garbage package is
// written by GPT or taken from Github
// Just use this package as a reference point and where to store something that are interesting
//but unpractical to use in the project (too complex, too long, etc)

import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain3 {
    private ArrayList<Block> chain;
    private int difficulty;
    private HashMap<String, UTXO> UTXOs;
    private double miningReward;

    public Blockchain3(int difficulty, double miningReward) {
        chain = new ArrayList<Block>();
        this.difficulty = difficulty;
        UTXOs = new HashMap<String, UTXO>();
        this.miningReward = miningReward;
        createGenesisBlock();
    }

    public void createGenesisBlock() {
        Block genesisBlock = new Block("0");
        chain.add(genesisBlock);
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block block) {
        block.setPreviousHash(getLatestBlock().getHash());
        block.mineBlock(difficulty);
        chain.add(block);
    }

    public void addTransaction(Transaction transaction) {
        if (!transaction.verifySignature()) {
            System.out.println("Transaction signature failed to verify.");
            return;
        }

        if (transaction.getAmount() <= 0) {
            System.out.println("Transaction amount must be greater than 0.");
            return;
        }

        if (transaction.getSender().getBalance() < transaction.getAmount()) {
            System.out.println("Sender does not have enough balance.");
            return;
        }

        double totalInputs = 0;
        for (UTXO utxo : transaction.getInputs()) {
            if (!UTXOs.containsKey(utxo.getTxOutputId())) {
                System.out.println("Input transaction is missing or already spent.");
                return;
            }
            totalInputs += UTXOs.get(utxo.getTxOutputId()).getAmount();
        }

        double totalOutputs = 0;
        for (TxOutput output : transaction.getOutputs()) {
            totalOutputs += output.getAmount();
        }

        if (totalInputs != totalOutputs) {
            System.out.println("Transaction inputs do not match outputs.");
            return;
        }

        for (UTXO utxo : transaction.getInputs()) {
            UTXOs.remove(utxo.getTxOutputId());
        }

        for (TxOutput output : transaction.getOutputs()) {
            UTXO utxo = new UTXO(output.getRecipient(), output.getAmount(), output.getTxOutputId());
            UTXOs.put(utxo.getTxOutputId(), utxo);
        }

        transaction.getSender().reduceBalance(transaction.getAmount());
        transaction.getRecipient().increaseBalance(transaction.getAmount());

        System.out.println("Transaction successful.");
    }

    public double getBalance(String walletAddress) {
        double balance = 0;
        for (UTXO utxo : UTXOs.values()) {
            if (utxo.isMine(walletAddress)) {
                balance += utxo.getAmount();
            }
        }
        return balance;
    }

    public Transaction createTransaction(User sender, User recipient, double amount) {
        ArrayList<TxInput> inputs = new ArrayList<TxInput>();
        ArrayList<TxOutput> outputs = new ArrayList<TxOutput>();

        double senderBalance = getBalance(sender.getPublicKey());

        if (senderBalance < amount) {
            System.out.println("Sender does not have enough balance.");
            return null;
        }

        double total = 0;
        for (UTXO utxo : UTXOs.values()) {
            if (utxo.isMine(sender.getPublicKey())) {
                total += utxo.getAmount();
                TxInput input = new TxInput(utxo.getTxOutputId());
                inputs.add(input

*/
