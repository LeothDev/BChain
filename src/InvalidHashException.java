public class InvalidHashException extends Exception{
    public void printMessage(){
        System.out.println("impossible to generate hash, field: previous hash is null");
    }

}