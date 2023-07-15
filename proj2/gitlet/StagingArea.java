package gitlet;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StagingArea implements Serializable {
    private Map<String, Blob> stagedFiles;

    public StagingArea() {
        stagedFiles = new HashMap<>();
    }

    public void add(String fileName, Blob blob) {
        stagedFiles.put(fileName, blob);
    }

    public void remove(String fileName) {
        stagedFiles.remove(fileName);
    }

    public boolean contains(String fileName) {
        return stagedFiles.containsKey(fileName);
    }

    public static StagingArea load() {
        try (FileInputStream fileIn = new FileInputStream(".gitlet/stagingArea.ser");
             ObjectInputStream objIn = new ObjectInputStream(fileIn)) {
            return (StagingArea) objIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new StagingArea();
        }
    }

    public void save() {
        try (FileOutputStream fileOut = new FileOutputStream(".gitlet/stagingArea.ser");
             ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {
            objOut.writeObject(this);
        } catch (IOException e) {
            System.err.println("Failed to save staging area: " + e.getMessage());
        }
    }
}
