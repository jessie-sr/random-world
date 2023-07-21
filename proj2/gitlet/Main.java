package gitlet;

import java.io.File;
import java.util.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args == null) {
            System.out.println("Please enter a command.");
        }
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
                } else if (!args[2].equals("--")) {
                    System.out.println("Incorrect operands.");
                } else {
                    restore(args[1], args[3]);
                }
                break;
            case "log":
                log();
                break;
            case "global-log":
                globalLog();
                break;
            case "status":
                status();
                break;
            case "rm":
                rm(args[1]);
                break;
            case "find":
                find(args[1]);
                break;
            case "branch":
                branch(args[1]);
                break;
            case "switch":
                switchBranch(args[1]);
                break;
            case "rm-branch":
                rmBranch(args[1]);
                break;
            case "reset":
                reset(args[1]);
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
        Commit initCommit = new Commit("initial commit", null, "main");
        CommitTree treeSystem = initCommit.getTree();
        Utils.writeObject(new File(gitletDir, "commitTree"), treeSystem);
        treeSystem.save();
        StagingArea stagingArea = new StagingArea();
        stagingArea.save();
    }

    public static void add(String fileName) {
        CommitTree commitTree = CommitTree.load();
        StagingArea stagingArea = StagingArea.load();
        Set<String> rmFiles = stagingArea.getRmFiles().keySet();
        if (rmFiles.contains(fileName)) {
            stagingArea.rmRmFiles(fileName);
        }
        File targetFile = new File(Repository.CWD, fileName);
        if (!targetFile.exists()) {
            System.out.println("File does not exist.");
        } else {
            if (stagingArea.contains(fileName)) {
                stagingArea.remove(fileName);
            }
            Commit currCommit = commitTree.getMain();
            Blob currentBlob = currCommit.getBlob(fileName);
            byte[] fileBytes = Utils.readContents(targetFile);
            if (currentBlob != null && currentBlob.isEqualContent(fileBytes)) {
                // The file content is identical to the current commit, no need to stage
                stagingArea.save();
                return;
            }
            byte[] copyBytes = new byte[fileBytes.length];
            System.arraycopy(fileBytes, 0, copyBytes, 0, fileBytes.length);
            Blob newBlob = new Blob(copyBytes);
            stagingArea.add(fileName, newBlob);
            stagingArea.save();
            commitTree.save();
        }
    }

    public static void commit(String message) {
        StagingArea stagingArea = StagingArea.load();
        if (stagingArea.getStagedFiles().isEmpty() && stagingArea.getRmFiles().isEmpty()) {
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
        Commit newCommit = new Commit(message, parentCommit, parentCommit.getBranchName());
        newCommit.getBlobs().putAll(parentCommit.getBlobs());
        for (Map.Entry<String, Blob> entry : stagedFiles.entrySet()) {
            String fileName = entry.getKey();
            Blob blob = entry.getValue();
            newCommit.addBlob(fileName, blob);
        }
        stagingArea.clear();
        stagingArea.save();
        commitTree.setMain(newCommit.getBranchName(), newCommit);
        commitTree.save();
    }

    public static void restore(String commitId, String fileName) {
        CommitTree commitTree = CommitTree.load();
        Commit parentCommit;
        String branchName = commitTree.getMain().getBranchName();

        if (commitId == null) {
            parentCommit = commitTree.getMain();
        } else {
            parentCommit = commitTree.findCommit(commitId);
            if (parentCommit == null) {
                System.out.println("No commit with that id exists.");
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

    public static void globalLog() {
        CommitTree commitTree = CommitTree.load();
        Map<String, Commit> commits = commitTree.getCommits();

        for (Commit commit: commits.values()) {
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
        }
    }

    public static void status() {
        CommitTree commitTree = CommitTree.load();
        Commit currMain = commitTree.getMain();
        Map<String, Commit> branches = commitTree.getBranches();
        System.out.println("=== Branches ===");
        for (String branchName: branches.keySet()) {
            if (branchName.equals(currMain.getBranchName())) {
                System.out.println("*"+branchName);
            } else {
                System.out.println(branchName);
            }
        }
        System.out.println();
        StagingArea stagingArea = StagingArea.load();
        Map<String, Blob> stagedFiles = stagingArea.getStagedFiles();
        Set<String> sortedStagedFiles = new TreeSet<>(stagedFiles.keySet());
        System.out.println("=== Staged Files ===");
        for (String fileName: sortedStagedFiles) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        Set<String> rmFiles = stagingArea.getRmFiles().keySet();
        for (String fileName: rmFiles) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public static void rm(String fileName) {
        StagingArea stagingArea = StagingArea.load();
        Map<String, Blob> stagedFiles = stagingArea.getStagedFiles();
        CommitTree commitTree = CommitTree.load();
        Commit currCommit = commitTree.getMain();
        Map<String, Blob> currBlobs = currCommit.getBlobs();
        // Check if the file is currently staged for addition, and unstage it if it is.
        if (stagedFiles.containsKey(fileName)) {
            stagingArea.remove(fileName);
        }
        // Check if the file is tracked in the current commit, and stage it for removal.
        else if (currBlobs.containsKey(fileName)) {
            Blob rmBlob = currBlobs.get(fileName);
            stagingArea.addRm(fileName, rmBlob);
            // Remove the file from the working directory if the user has not already done so.
            File rmFile = new File(Repository.CWD, fileName);
            if (rmFile.exists()) {
                rmFile.delete();
            }
        }
        // Print an error message if the file is neither staged nor tracked by the head commit.
        else {
            System.out.println("No reason to remove the file.");
            return;
        }
    }

    public static void find(String commitMessage) {
        List<String> commitList = Utils.plainFilenamesIn(".gitlet/commit");
        CommitTree commitTree = CommitTree.load();
        boolean commitExist = false;
        for (String commitId: commitList) {
            Commit commit = commitTree.findCommit(commitId);
            if (commit.getMessage().equals(commitMessage)) {
                commitExist = true;
                System.out.println(commitId);
            }
        }
        if (!commitExist) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void branch(String branchName) {
        CommitTree commitTree = CommitTree.load();
        Map<String, Commit> currBranches = commitTree.getBranches();
        if (currBranches.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        Commit currCommit = commitTree.getMain();
        commitTree.addBranch(branchName, currCommit);
        commitTree.save();
    }

    public static void switchBranch(String branchName) {
        StagingArea stagingArea = StagingArea.load();
        CommitTree commitTree = CommitTree.load();
        Commit currMain = commitTree.getMain();
        Map<String, Commit> currBranches = commitTree.getBranches();
        // Check if the branch with the given branchName exists.
        if (!currBranches.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        Commit newMain = currBranches.get(branchName);
        // Check if that branch is the current branch.
        if (branchName.equals(currMain.getBranchName())) {
            System.out.println("No need to switch to the current branch.");
            return;
        }
        // Check if a working file in the branch to be switched to is untracked in the current branch.
        Map<String, Blob> newMainBlobs = newMain.getBlobs();
        Map<String, Blob> currMainBlobs = currMain.getBlobs();
        for (String fileName: newMainBlobs.keySet()) {
            File overwrittenFile = new File(Repository.CWD, fileName);
            Blob b = newMainBlobs.get(fileName);
            if (overwrittenFile.isFile() && b != null && !b.isEqualContent(Utils.readContents(overwrittenFile)) && !currMainBlobs.containsKey(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        // Takes all files in the commit at the head of the given branch, and puts them in the working directory, overwriting the versions of the files that are already there if they exist.
        for (String fileName: newMainBlobs.keySet()) {
            Blob b = newMainBlobs.get(fileName);
            File overwrittenFile = new File(Repository.CWD, fileName);
            Utils.writeContents(overwrittenFile, (Object) b.getContent());
        }
        // Delete any files that are tracked in the current branch but are not present in the checked-out branch.
        for (String fileName: currMainBlobs.keySet()) {
            if (!newMainBlobs.containsKey(fileName)) {
                File deletedFile = new File(Repository.CWD, fileName);
                deletedFile.delete();
            }
        }
        commitTree.setMain(branchName, newMain);
        commitTree.save();
        stagingArea.clear();
        stagingArea.save();
    }

    public static void rmBranch(String branchName) {
        CommitTree commitTree = CommitTree.load();
        Map<String, Commit> currBranches = commitTree.getBranches();
        // Check if a branch with the given name exists.
        if (!currBranches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        // Check if the branch to be removed is the current branch.
        Commit currMain = commitTree.getMain();
        Commit removedBranch = currBranches.get(branchName);
        if (currMain.equals(removedBranch)) {
            System.out.println("Cannot remove the current branch.");
        }
        // Delete the branch (pointer to the Commit) with the given name.
        currBranches.remove(branchName);
        commitTree.save();
    }

    public static void reset(String commitId) {
        StagingArea stagingArea = StagingArea.load();
        Map<String, Blob> stagedFiles = stagingArea.getStagedFiles();
        CommitTree commitTree = CommitTree.load();
        // Check if the commit with the given commitId exists.
        Commit targetCommit = commitTree.findCommit(commitId);
        if (targetCommit == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        // Check if a working file is untracked in the current branch and would be overwritten by the reset.
        Map<String, Blob> targetBlobs = targetCommit.getBlobs();
        Commit currMain = commitTree.getMain();
        Map<String, Blob> mainBlobs = currMain.getBlobs();
        for (String blobName : targetBlobs.keySet()) {
            File file = new File(Repository.CWD, blobName);
            Blob b = targetBlobs.get(blobName);
            if (file.isFile() && b != null && !b.isEqualContent(Utils.readContents(file)) && !mainBlobs.containsKey(blobName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        // Restore all the files tracked by the given commit.
        for (String blobName : targetBlobs.keySet()) {
            restore(commitId, blobName);
        }
        for (String fileName : stagedFiles.keySet()) {
            if (!targetBlobs.containsKey(fileName)) {
                rm(fileName);
            }
        }
        // Move the current branch’s head to that commit node.
        commitTree.setMain(commitTree.getMain().getBranchName(), targetCommit);
        commitTree.save();
        stagingArea.clear();
        stagingArea.save();
    }
}
