import java.time.LocalDateTime;

public class KerberosProtocol {
    public static final String lifetime = "30";//30 minutes
    public static String keySession;


    public static void main(String[] args) {
        //Novi registrirani korisnici  na koi kdc generira klucevi
        KDC kdc = new KDC();
        keySession = kdc.generateSessionKey();

        User Alice = new User("Alice", "1", kdc.generateKey());
        User Bob = new User("Bob", "2", kdc.generateKey());

        kdc.registeredUsers.put(Alice.getId(), Alice.getKey());
        kdc.registeredUsers.put(Bob.getId(), Bob.getKey());

        //Alis saka da zboruva so Bob
        String request = Alice.sendRequest(Bob); //IDA, IDB, rA

        String[] kdcresponses = kdc.generateResponse(request); //ya i yb
        String nonce = request.split(" ")[2]; //save Nonce


        if (Alice.verifyYa(kdcresponses[0], nonce, Bob) == true) {
            LocalDateTime timestamp = LocalDateTime.now();
            String yAB = Alice.getId() + " " + timestamp.toString();
            yAB = AES.encrypt(yAB, KerberosProtocol.keySession);

            //na slednava proverka pagja aplikacijata poradi yB
            if (Bob.verifyYbAndYab(kdcresponses[1], yAB) == true) {
                String message = "Hello Bob how are you? Whats up lately?";
                String encryptedMessage = AES.encrypt(message, keySession);
                String decryptedMessage = AES.decrypt(encryptedMessage, keySession);

                //Ako ja ispecati istata poraka - aplikacijata e vo red
                System.out.println(decryptedMessage);
            }

        }

    }
}
