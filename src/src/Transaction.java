import java.security.*;

public class Transaction {
    private PublicKey publicKey;
    private int senderId;
    private int recipientWalletAdr;
    private double transactionAmount;
    private double fee = 0;
    private boolean notProcessed = true;
    private Wallet senderWallet;
    private Wallet recipientWallet;
    private byte[] signature;

    public Transaction(int recipientWalletAdr, double transactionAmount) {
        this.recipientWalletAdr = recipientWalletAdr;
        this.recipientWallet = User.getWalletByAddress(recipientWalletAdr);
        this.transactionAmount = transactionAmount;
    }
    public Transaction(PublicKey publicKey, int senderId, int recipientWalletAdr, double transactionAmount) {
        this.publicKey = publicKey;
        this.senderId = senderId;
        this.recipientWalletAdr = recipientWalletAdr;
        this.transactionAmount = transactionAmount;
    }
    public Transaction(PublicKey publicKey, int senderId, int recipientWalletAdr, double transactionAmount, double fee) {
        this.publicKey = publicKey;
        this.senderId = senderId;
        this.recipientWalletAdr = recipientWalletAdr;
        this.transactionAmount = transactionAmount;
        this.fee = fee;
    }

    public Transaction(PublicKey publicKey, int senderId, double amount,Wallet senderWallet, Wallet recipientWallet){
        this.publicKey = publicKey;
        this.senderId = senderId;
        this.senderWallet = senderWallet;
        this.recipientWallet = recipientWallet;
        this.recipientWalletAdr = recipientWallet.getWallet_address();
        this.transactionAmount = amount;
    }

    public void processTransaction()  {
        try {
            if(senderId != 0 && senderWallet!=null){
                if (verifySignature() && notProcessed) {
                    senderWallet.updateBalance(-transactionAmount);
                    recipientWallet.updateBalance(transactionAmount);
                    notProcessed = false;
                }
            }
            else{
                recipientWallet.updateBalance(transactionAmount);
            }
        }catch (SignatureException| NoSuchAlgorithmException|InvalidKeyException e){}
    }



/*    public byte[] sign(PrivateKey privateKey) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        String transactionData = this.toString();
        Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
        signatureAlgorithm.initSign(privateKey);
        signatureAlgorithm.update(transactionData.getBytes());
        return signatureAlgorithm.sign();
    }

 */

    public boolean verifySignature() throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
/*        String transactionData = this.toString();
        Signature signatureAlgorithm = Signature.getInstance("SHA256withRSA");
        signatureAlgorithm.initVerify(publicKey);
        signatureAlgorithm.update(transactionData.getBytes());
        return signatureAlgorithm.verify(signature);*/
        String message = this.toString();
        return DigitalSignature.verify(message, signature, publicKey);
    }


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

    public double getFee(){
        return this.fee;
    }

    public void setFee(double fees){
        this.fee = fees;
    }
    public Wallet getSenderWallet(){return senderWallet;}
    public Wallet getRecipientWallet(){return recipientWallet;}
}

