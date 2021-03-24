import java.security.SecureRandom;
import java.time.LocalDateTime;

public class User {
    private String username;
    private String id;
    private String key;

    public User(String username, String id, String key) {
        this.username = username;
        this.id = id;
        this.key = key;
    }

    public String generateNonce() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < 16) {
            sb.append(secureRandom.nextInt(10));
            i++;
        }
        return sb.toString();
    }

    public String sendRequest(User reciever) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id + " "); //Alice
        sb.append(reciever.id + " "); //Bob
        sb.append(this.generateNonce() + " "); //Alice
        return sb.toString();
    }

    public boolean verifyYa(String encryptedYa, String nonce, User user) { //vo red e
        String decryptedYa = AES.decrypt(encryptedYa, this.getKey());
        String[] parts = decryptedYa.split(" ");
        if (!parts[1].equals(nonce)) { //proverka na nonce
            return false;
        } else if (parts[2].compareTo(KerberosProtocol.lifetime) > 0) {
            return false; //not fresh
        } else if (!parts[3].equals(user.getId())) { //proverka na identitetot na Bob
            return false;
        } else return true;

    }

    public boolean verifyYbAndYab(String encryptedYb, String encryptedYab) {
        //tuka dava null, ova e celiot problem
        String decryptedYb = AES.decrypt(encryptedYb, this.getId());
        //ova go dekriptira dobro, i se ostanato e dobro
        String decryptedYab = AES.decrypt(encryptedYab, KerberosProtocol.keySession);

        String[] ybparts = decryptedYb.split(" ");
        String[] yabparts = decryptedYab.split(" ");

        LocalDateTime timeAlice = LocalDateTime.parse(yabparts[0]);
        timeAlice = timeAlice.plusMinutes(Integer.parseInt(KerberosProtocol.lifetime));

        if (!ybparts[1].equals(yabparts[0])) { //id' so id
            return false;
        } else if (ybparts[2].compareTo(KerberosProtocol.lifetime) >= 0) {
            return false; //not fresh
        } else if (!LocalDateTime.now().isBefore(timeAlice)) {
            return false; //lifetime expired
        } else return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
