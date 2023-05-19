public class Wallet {
    private double balance;
    private int wallet_address;
    // other fields and methods


    Wallet(int balance, int address){
        this.balance = balance;
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

    public void updateBalance(double amount){
        this.balance += amount;
    }
}

