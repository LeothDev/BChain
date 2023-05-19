import java.security.*;
import java.util.Base64;

public class DigitalSignature {
/*    Java provides built-in support for digital signatures using the
    Java Cryptography Architecture (JCA) and Java Cryptography Extension (JCE) libraries.*/
    public static byte[] sign(String message, PrivateKey pvk) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature to_sign = Signature.getInstance("SHA256withRSA");
        to_sign.initSign(pvk);
        to_sign.update(message.getBytes());
        return to_sign.sign();
    }

/*    public static Boolean verify(byte[] encrypted, PublicKey pubk){
        // Verify the digital signature using the public key
        signature.initVerify(pubk);
        signature.update(message.getBytes());
        boolean verified = signature.verify(digitalSignature);
        return verified;
    }*/
    public static boolean verify(String message, byte[] signature, PublicKey pbk) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        /*We start by getting an instance of the Signature class, which is part of the java.security package.
        We use the "SHA256withRSA" algorithm, which is a widely-used algorithm for digital signatures.*/
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(pbk); //set this Signature Object into verification mode
        sign.update(message.getBytes()); //update with original message that was signed
        //the original message -> but its hash (digest)
        return sign.verify(signature);
       /* It then computes a new digest of the original message using the same hashing algorithm as
        was used to create the digest in the signature. If the new digest matches the decrypted
        digest in the signature, then the signature is valid and the verify() method
    returns true. If the digests do not match, then it return false*/
    }

}

