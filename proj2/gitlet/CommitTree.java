package gitlet;

import java.io.Serializable;

public class CommitTree implements Serializable {

    private Commit main;

    public CommitTree(Commit currCommit) {
        this.main = currCommit;
        currCommit.setTree(this);
    }

    public Commit getMain() {
        return main;
    }
}
