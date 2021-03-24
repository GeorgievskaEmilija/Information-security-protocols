import java.util.Random;
import java.security.PrivateKey;
import java.security.PublicKey;

public class User {
    private String name;
    private double randomValue;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public User(String name) {
        this.name = name;
    }


    public double getExponential() {
        // generator^random from the user that calls this method
        return Math.pow(STS.generatorG, this.getRandomValue());
    }

    public String getKey(double exponentialNumber) {
        return String.valueOf(Math.pow(exponentialNumber, this.getRandomValue()));
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(double randomValue) {
        this.randomValue = randomValue;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
