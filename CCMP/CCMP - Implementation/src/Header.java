public class Header {
    //32
    String preamble; // 7 bytes
    String SFD; // 1 byte
    String sourceMacAddress; //  6 bytes
    String destinationMacAddress; // 6 bytes
    String etherType; // 2 bytes
    String restFields; // 10 bytes do vkupno 32

    public Header() {
    }

    public Header(String preamble, String SFD, String sourceMacAddress, String destinationMacAddress, String etherType, String restFields) {
        this.preamble = preamble;
        this.SFD = SFD;
        this.sourceMacAddress = sourceMacAddress;
        this.destinationMacAddress = destinationMacAddress;
        this.etherType = etherType;
        this.restFields = restFields;
    }

    public String getPreamble() {
        return preamble;
    }

    public void setPreamble(String preamble) {
        this.preamble = preamble;
    }

    public String getSFD() {
        return SFD;
    }

    public void setSFD(String SFD) {
        this.SFD = SFD;
    }

    public String getSourceMacAddress() {
        return sourceMacAddress;
    }

    public void setSourceMacAddress(String sourceMacAddress) {
        this.sourceMacAddress = sourceMacAddress;
    }

    public String getDestinationMacAddress() {
        return destinationMacAddress;
    }

    public void setDestinationMacAddress(String destinationMacAddress) {
        this.destinationMacAddress = destinationMacAddress;
    }

    public String getEtherType() {
        return etherType;
    }

    public void setEtherType(String etherType) {
        this.etherType = etherType;
    }

    public String getRestFields() {
        return restFields;
    }

    public void setRestFields(String restFields) {
        this.restFields = restFields;
    }

    @Override
    public String toString() {
        return preamble + SFD + sourceMacAddress + destinationMacAddress + restFields; //256
    }
}