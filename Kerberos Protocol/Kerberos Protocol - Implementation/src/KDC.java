import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class KDC {
    HashMap<String, String> registeredUsers;

    public KDC() {
        this.registeredUsers = new HashMap<>();
    }

    public String generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < 16) {
            sb.append(secureRandom.nextInt(10));
            i++;
        }
        return sb.toString();
    }

    public String[] generateResponse(String request) { //vo red e
        String[] parts = request.split(" ");
        String IdAlice = parts[0];
        String IdBob = parts[1];
        String RAlice = parts[2];


        String yA = KerberosProtocol.keySession + " " + RAlice + " " + KerberosProtocol.lifetime + " " + IdBob;
        yA = AES.encrypt(yA, registeredUsers.get(IdAlice));

        String yB = KerberosProtocol.keySession + " " + IdAlice + " " + KerberosProtocol.lifetime;
        yB = AES.encrypt(yB, registeredUsers.get(IdBob));

        String[] response = new String[2];
        response[0] = yA;
        response[1] = yB;

        return response;

    }

    public String generateSessionKey() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < 16) {
            sb.append(secureRandom.nextInt(10));
            i++;
        }
        return sb.toString();
    }


}

class AES {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

}