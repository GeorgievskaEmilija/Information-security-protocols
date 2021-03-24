public class EncryptedFrame {
    private Header header;
    private String packageNum;
    private String encryptedData;
    private String MIC;
    private String frameCheckSequence;

    public EncryptedFrame() {
    }

    public EncryptedFrame(Header header, String packageNum, String encryptedData, String MIC) {
        this.header = header;
        this.packageNum = packageNum;
        this.encryptedData = encryptedData;
        this.MIC = MIC;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(String packageNum) {
        this.packageNum = packageNum;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getMIC() {
        return MIC;
    }

    public void setMIC(String MIC) {
        this.MIC = MIC;
    }

    public String getFrameCheckSequence() {
        return frameCheckSequence;
    }

    public void setFrameCheckSequence(String frameCheckSequence) {
        this.frameCheckSequence = frameCheckSequence;
    }
}
