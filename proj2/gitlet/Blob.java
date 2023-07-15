package gitlet;

import java.io.Serializable;
import java.util.Arrays;

public class Blob implements Serializable {
    private byte[] content;

    private String id;

    public Blob(byte[] content) {
        this.content = content;
        this.id = Utils.sha1(content);
    }

    public byte[] getContent() {
        return content;
    }

    public boolean isEqualContent(byte[] otherContent) {
        return Arrays.equals(content, otherContent);
    }

    public String getId() {
        return this.id;
    }
}
