package gitlet;

import java.io.Serializable;
import java.io.File;
import java.util.*;

public class CommitTree implements Serializable {

    private Map<String, Commit> branches;

    private Map<String, Commit> commits;

    private Commit main;

    public CommitTree(Commit currCommit) {
        this.branches = new TreeMap<>();
        this.commits = new TreeMap<>();
        this.main = currCommit;
        this.addBranch(currCommit.getBranchName(), currCommit);
        this.commits.put(currCommit.getId(), currCommit);
    }

    public Map<String, Commit> getBranches() {
        return branches;
    }

    public Map<String, Commit> getCommits() {
        return commits;
    }

    public void addCommit(String commitId, Commit commit) {
        this.commits.put(commitId, commit);
    }

    public void addBranch(String branchName, Commit newBranch) {
        branches.put(branchName, newBranch);
    }

    public Commit getMain() {
        return main;
    }

    public void setMain(String branchName, Commit mainCommit) {
        this.main = mainCommit;
        mainCommit.setBranchName(branchName);
        branches.put(branchName, mainCommit);
    }

    public static CommitTree load() {
        File fileIn = new File(".gitlet/commitTree.ser");
        return Utils.readObject(fileIn, CommitTree.class);
    }

    public void save() {
        File fileOut = new File(".gitlet/commitTree.ser");
        Utils.writeObject(fileOut, this);
    }

    public Commit findCommit(String commitId) {
        if (commits.containsKey(commitId)) {
            return commits.get(commitId);
        }
        for (String id: commits.keySet()) {
            if (id.contains(commitId)) {
                return findCommit(id);
            }
        }
        return null;
    }
}
