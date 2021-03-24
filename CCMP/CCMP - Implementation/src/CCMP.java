import java.beans.Encoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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

public class CCMP {

    private static final String PACKET_NUMBER = "000001";
    private static final String COUNTER = "1";
    private static final String KEY = "kuey3rbsfks8h28a";

    public static void main(String[] args) {
        Header header = new Header("header1", "a", "203495", "984754", "20", "b26587dd4ss8"); //32 bajti
        String data = "I am going to encrypt this next sentence and try my work on it.."; //64 bajti
        ClearTextFrame frame = new ClearTextFrame(header, PACKET_NUMBER, data);

        // 1. Clear text frame data
        System.out.println("CLEAR TEXT FRAME DATA: ");
        System.out.println(frame.getData().toString()+"\n");

        //2. MIC
        byte[] mic = getMic(frame);


        //3. Enkripcija vo Counter Mode i AES vo ECB rezim
        String encryptedData = CMEncryption(frame.getData());
        EncryptedFrame encryptedFrame = new EncryptedFrame(header, "1", encryptedData, mic.toString());

        //4.Simulacija na prakjanje ramka
        sendData(encryptedFrame, mic);
    }

    private static void sendData(EncryptedFrame encryptedFrame, byte[] mic) {
        System.out.println("================SENDING================");
        System.out.println("ENCRYPTED DATA: ");
        System.out.println(encryptedFrame.getEncryptedData());
        System.out.println("MIC: ");
        System.out.println(mic.toString());
    }


    public static String CMEncryption(String data) {
        String dataEncrypted = "";
        String encryptedCounter = AES.encrypt(COUNTER, KEY);
        int counter = Integer.parseInt(COUNTER);
        byte[] dataBytes = data.getBytes();
        int parts = dataBytes.length / 16;
        if (parts * 16 != dataBytes.length) {
            parts += 1;
        }
        int i = 0;
        while (i < parts) {
            byte[] currentBlock;
            if (parts - 1 == i) {
                currentBlock = data.substring(i * 16, dataBytes.length).getBytes();
            } else {
                currentBlock = data.substring(i * 16, (i * 16) + 16).getBytes();
            }
            i++;
            byte[] xorOutput = xor(currentBlock, encryptedCounter.getBytes());
            counter = counter + 1;
            encryptedCounter = AES.encrypt(Integer.toString(counter), KEY);
            String xorOutputString = Arrays.toString(xorOutput);
            dataEncrypted = dataEncrypted + xorOutputString;
        }

        return dataEncrypted;
    }


    private static byte[] getMic(ClearTextFrame frame) {
        //1 Nonce i negova enkripcija
        String QoS = "9857";
        String sourceMacAddress = "203495P";
        String nonce = new Nonce(PACKET_NUMBER, QoS, sourceMacAddress).toString();
        String encryptedNonce = AES.encrypt(nonce, KEY);

        // Kalkulacija na MIC so CBC
        String headerData = frame.getHeader().toString();
        String frameData = frame.getData();

        byte[] headerDataBytes = headerData.getBytes();
        int parts = headerDataBytes.length / 16;
        if (parts * 16 != headerDataBytes.length) {
            parts += 1;
        }
        int i = 0;
        while (i < parts) {
            byte[] currentBlock;
            if (parts - 1 == i) {
                currentBlock = headerData.substring(i * 16, headerDataBytes.length).getBytes();
            } else {
                currentBlock = headerData.substring(i * 16, (i * 16) + 16).getBytes();
            }
            i++;
            byte[] xorOutput = xor(currentBlock, encryptedNonce.getBytes());
            encryptedNonce = AES.encrypt(Arrays.toString(xorOutput), KEY);
        }

        // data part
        byte[] dataBytes = frameData.getBytes();
        parts = dataBytes.length / 16;
        if (parts * 16 != dataBytes.length) {
            parts += 1;
        }
        i = 0;
        while (i < parts) {
            byte[] currentBlock;
            if (parts - 1 == i) {
                currentBlock = frameData.substring(i * 16, dataBytes.length).getBytes();
            } else {
                currentBlock = frameData.substring(i * 16, (i * 16) + 16).getBytes();
            }
            i++;
            byte[] xorOutput = xor(currentBlock, encryptedNonce.getBytes());
            encryptedNonce = AES.encrypt(Arrays.toString(xorOutput), KEY);
        }


        //Enkripcija na counter, i xor na toa so dosegasniot rezultat
        String encryptedCounter = AES.encrypt(COUNTER, KEY);
        return xor(encryptedCounter.getBytes(), encryptedNonce.getBytes());
    }

    public static byte[] xor(byte[] a1, byte[] a2) {
        byte[] a3 = new byte[a1.length];

        int i = 0;
        for (byte b : a1) {
            a3[i] = (byte) (b ^ a2[i++]);
        }
        return a3;
    }
}

