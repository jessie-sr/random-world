package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /* TODO: fill in the rest of this class. */

    private String author;

    private Object timeStamp;

    private String id;

    private Commit parent;

    private CommitTree currTree;

    private Map<String, Blob> Blobs;

    public Commit(String message, Commit parent) {
        this.message = message;
        this.parent = parent;
        this.author = System.getProperty("user.name");
        this.Blobs = new HashMap<>();
        this.id = Setid();
        if (!Main.isInitialized()) {
            this.timeStamp = "00:00:00 UTC, Thursday, 1 January 1970";
        } else {
            this.timeStamp = Time();
        }
        if (parent != null) {
            this.setTree(parent.getTree());
        }
    }

    private String Time() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    private String Setid() {
        double vals = Math.random();
        return Utils.sha1(Double.toString(vals));
    }

    public CommitTree getTree() {
        return currTree;
    }
    public void setTree(CommitTree currTree) {
        this.currTree = currTree;
    }

    public Map<String, Blob> getBlobs() {
        return Blobs;
    }

    public Blob getBlob(String fileName) {
        return Blobs.getOrDefault(fileName, null);
    }

    public void addBlob(String fileName, Blob copyFile) {
        Blobs.put(fileName, copyFile);
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timeStamp.toString();
    }

    public String getId(){
        return id;
    }

    public Commit getParent() {
        return parent;
    }

    public boolean hasFile(String fileName) {
        if (Blobs.containsKey(fileName)) {
            return true;
        } else {
            return false;
        }
    }
}
