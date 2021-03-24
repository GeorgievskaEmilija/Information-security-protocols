public class Nonce {
    //Nonce e predodreden od ovie tri atributi

    String packetNumber; //6 bytes
    String qualityOfService; //4 bytes za simulacija
    String sourceMacAddress;  //6 bytes

    public Nonce(String packetNumber, String qualityOfService, String sourceMacAddress) {
        this.packetNumber = packetNumber;
        this.qualityOfService = qualityOfService;
        this.sourceMacAddress = sourceMacAddress;
    }

    @Override
    public String toString() {
        return packetNumber + qualityOfService + sourceMacAddress;
    }
}