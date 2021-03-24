import java.io.UnsupportedEncodingException;

public class ClearTextFrame {
    private Header header;
    private String packageNum;
    private String data;

    public ClearTextFrame(Header header, String packageNum, String data) {
        this.header = header;
        this.packageNum = packageNum;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return header.toString() + data;
    }

}
