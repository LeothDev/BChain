package validation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class ProofOfWorkExample {
    private static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

/*import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class ProofOfWorkExample {

    public static void main(String[] args) {

        String transactions = "Transaction 1, Transaction 2, Transaction 3";
        int difficulty = 4;

        // Start mining process
        long startTime = new Date().getTime();
        int nonce = generateNonce();
        String hash = "";
        while (!hash.substring(0, difficulty).equals("0".repeat(difficulty))) {
            nonce = generateNonce();
            String blockData = transactions + nonce;
            hash = applySha256(blockData);
        }
        long endTime = new Date().getTime();

        // Print results
        System.out.println("Block mined: " + hash);
        System.out.println("Nonce value: " + nonce);
        System.out.println("Mining time: " + (endTime - startTime) + " ms");
    }

    private static int generateNonce() {
        SecureRandom random = new SecureRandom();
        byte[] nonceBytes = new byte[4];
        random.nextBytes(nonceBytes);
        return Math.abs(java.nio.ByteBuffer.wrap(nonceBytes).getInt());
    }

    private static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}*/
