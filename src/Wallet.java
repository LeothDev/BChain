import java.sql.SQLOutput;

public class Wallet {
    private double balance;
    private int wallet_address;
    private int UTXO; //"UTXO" (Unspent Transaction Output)
    // other fields and methods


    Wallet(int balance, int address){
        this.balance = balance;
        this.UTXO = balance;
        this.wallet_address = address;
    }


    Wallet(int address){
        this.wallet_address = address;
    }
    public double get_balance() {
        return balance;
    }

    protected int getWallet_address(){
        return this.wallet_address;
    }

    protected double get_UTXO(){
        return this.UTXO;
    }

    protected void updateUTXO(double n){
        this.UTXO -= n;
    }
    public void updateBalance(double n){
        balance = balance + n;
        if(n>0)
            //if n<0, means this is the sender, and UTXO has already been substracted
            UTXO += n;
    }


}

