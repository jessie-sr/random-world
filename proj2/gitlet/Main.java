package gitlet;

import java.io.File;
import java.util.Map;

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
            case "commit":
                commit(args[1]);
                break;
            case "restore":
                if (args.length == 3) {
                    restore(null, args[2]);
                } else {
                    restore(args[1], args[3]);
                }
                break;
            case "log":
                log();
                break;
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
        treeSystem.save();
        StagingArea stagingArea = new StagingArea();
        stagingArea.save();
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
            CommitTree commitTree = CommitTree.load();
            Commit currCommit = commitTree.getMain();
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

    public static void commit(String message) {
        StagingArea stagingArea = StagingArea.load();
        if (stagingArea.getStagedFiles().isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        if (message.isBlank()) {
            System.out.println("Please enter a commit message.");
            return;
        }
        CommitTree commitTree = CommitTree.load();
        Commit parentCommit = commitTree.getMain();
        Map<String, Blob> stagedFiles = stagingArea.getStagedFiles();
        Commit newCommit = new Commit(message, parentCommit);
        newCommit.getBlobs().putAll(parentCommit.getBlobs());
        for (Map.Entry<String, Blob> entry : stagedFiles.entrySet()) {
            String fileName = entry.getKey();
            Blob blob = entry.getValue();
            newCommit.getBlobs().put(fileName, blob);
        }
        stagingArea.clear();
        stagingArea.save();
        commitTree.setMain(newCommit);
        commitTree.save();
    }

    public static void restore(String commitId, String fileName) {
        CommitTree commitTree = CommitTree.load();
        Commit parentCommit;

        if (commitId == null) {
            parentCommit = commitTree.getMain();
        } else {
            parentCommit = commitTree.findCommit(commitId);
            if (parentCommit == null) {
                System.out.println("No commit with the given id exists.");
                return;
            }
        }

        Blob fileBlob = parentCommit.getBlob(fileName);
        if (fileBlob == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        byte[] fileContent = fileBlob.getContent();
        File restoredFile = new File(Repository.CWD, fileName);
        Utils.writeContents(restoredFile, fileContent);
    }

    public static void log() {
        CommitTree commitTree = CommitTree.load();
        Commit commit = commitTree.getMain();

        while (commit != null) {
            System.out.println("===");
            System.out.println("commit " + commit.getId());

            // Print merge commit information if applicable
//            if (commit.getParents().size() > 1) {
//                System.out.print("Merge: ");
//                System.out.print(commit.getParents().get(0).getId().substring(0, 7) + " ");
//                System.out.println(commit.getParents().get(1).getId().substring(0, 7));
//            }

            System.out.println("Date: " + commit.getTimestamp());
            System.out.println(commit.getMessage());
            System.out.println();

            commit = commit.getParent();
        }
    }
}
