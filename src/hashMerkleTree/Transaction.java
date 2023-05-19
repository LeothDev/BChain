package hashMerkleTree;
import java.security.*;

public class Transaction {
    private PublicKey publicKey;
    private int senderId;
    private int recipientWalletAdr;
    private double transactionAmount;
    private byte[] signature;
    public Transaction(PublicKey publicKey, int recipientWalletAdr, double transactionAmount) {
        this.publicKey = publicKey;
        this.recipientWalletAdr = recipientWalletAdr;
        this.transactionAmount = transactionAmount;
    }
    public Transaction(PublicKey publicKey, int senderId, int recipientWalletAdr, double transactionAmount) {
        this.publicKey = publicKey;
        this.senderId = senderId;
        this.recipientWalletAdr = recipientWalletAdr;
        this.transactionAmount = transactionAmount;
    }

/*    public byte[] sign(PrivateKey privateKey) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        String transactionData = this.toString();
        Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
        signatureAlgorithm.initSign(privateKey);
        signatureAlgorithm.update(transactionData.getBytes());
        return signatureAlgorithm.sign();
    }

    public boolean verifySignature() throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        String transactionData = this.toString();
        Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
        signatureAlgorithm.initVerify(publicKey);
        signatureAlgorithm.update(transactionData.getBytes());
        return signatureAlgorithm.verify(signature);
    }*/

    public String toString() {
        return String.format("Sender ID: %d, Recipient Wallet Address: %d, Transaction Amount: %.2f", senderId, recipientWalletAdr, transactionAmount);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getRecipientWalletAdr() {
        return recipientWalletAdr;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getSignature() {
        return signature;
    }
}

