package gitlet;

import java.io.Serializable;
import java.io.File;

public class CommitTree implements Serializable {

    private Commit main;

    public CommitTree(Commit currCommit) {
        this.main = currCommit;
        currCommit.setTree(this);
    }

    public Commit getMain() {
        return main;
    }

    public void setMain(Commit mainCommit) {
        this.main = mainCommit;
    }

    public static CommitTree load() {
        File fileIn = new File(".gitlet/commitTree.ser");
        return Utils.readObject(fileIn, CommitTree.class);
    }

    public void save() {
        File fileOut = new File(".gitlet/commitTree.ser");
        Utils.writeObject(fileOut, this);
    }

    public boolean hasCommit(String commitId) {
        Commit pointer = this.main;
        while (pointer != null) {
            if (pointer.getId().equals(commitId)) {
                return true;
            }
            pointer = pointer.getParent();
        }
        return false;
    }
}
