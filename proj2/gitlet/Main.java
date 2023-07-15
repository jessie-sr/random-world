package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                add(args[1]);
                break;
            // TODO: FILL THE REST IN
        }
    }

    public static boolean isInitialized() {
        File gitletDir = new File(System.getProperty("user.dir"), ".gitlet");
        return gitletDir.exists() && gitletDir.isDirectory();
    }

    public static void init() {
        File gitletDir = new File(Repository.CWD, ".gitlet");
        if (gitletDir.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
        gitletDir.mkdir();
        Commit initCommit = new Commit("initial commit", null);
        CommitTree treeSystem = new CommitTree(initCommit);
        Utils.writeObject(new File(gitletDir, "commitTree"), treeSystem);
    }

    public static void add(String fileName) {
        File targetFile = new File(Repository.CWD, fileName);
        if (!targetFile.exists()) {
            System.out.println("File does not exist.");
        } else {
            StagingArea stagingArea = StagingArea.load();
            if (stagingArea.contains(fileName)) {
                stagingArea.remove(fileName);
            }
            File treeDir = new File(".gitlet/commitTree");
            CommitTree currTree = Utils.readObject(treeDir, CommitTree.class);
            Commit currCommit = currTree.getMain();
            Blob currentBlob = currCommit.getBlob(fileName);
            byte[] fileBytes = Utils.readContents(targetFile);
            if (currentBlob != null && currentBlob.isEqualContent(fileBytes)) {
                // The file content is identical to the current commit, no need to stage
                stagingArea.save();
                return;
            }
            Blob newBlob = new Blob(fileBytes);
            stagingArea.add(fileName, newBlob);
            stagingArea.save();
        }
    }

    private Commit getCurrCommit() {
        File treeDir = new File(".gitlet/commitTree");
        CommitTree currTree = Utils.readObject(treeDir, CommitTree.class);
        Commit currCommit = currTree.getMain();
        return currCommit;
    }
}
