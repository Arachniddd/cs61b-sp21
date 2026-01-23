package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  We never instantiation a Repository! We just use it to save some dirs!
 *
 *  A gitlet repository should have these variables:
 *  -
 *
 *  @author TODO
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The staging area directory.
     */
    public static final File StagingArea = join(GITLET_DIR, "StagingArea");
    /**
     * The addition area directory in staging area.
     */
    public static final File AdditionArea = join(StagingArea, "AdditionArea");
    /**
     * So does the removal area directory.
     */
    public static final File RemovalArea = join(StagingArea, "RemovalArea");
    /**
     * The commit directory.
     */
    public static final File Commits = join(GITLET_DIR, "Commits");
    /**
     * The blob directory.
     */
    public static final File Blobs = join(GITLET_DIR, "Blobs");
    /**
     * The branch pointer.
     */
    public static final File Branches = join(GITLET_DIR, "Branches");
    /**
     * The head pointer.
     */
    public static final File refs = join(GITLET_DIR, "refs");
    public static final File heads = join(refs, "heads");


    /** What can we do to a repository?
     * - Main method just call the methods in the Repository class! We should realize things in Main! All the things we do are toward the 'Repository!'
     *
     * - When we call 'add' method, we should add the corresponding file to the addition area.
     * - And we should save the 'temporary' files in the AdditionArea directory.
     * - The same as removal area.
     *
     * - When we call 'commit' method, we should create a commit and save it in the Commits directory.
     * - After 'commit', we should clear the staging area.
     *
     * TODO: And we also have many other commands to do!Don't forget!
     */


    /**
     * We can create a commit in the Commits directory!
     */
    private static void clearStagingArea() {
        File addition = Utils.join(AdditionArea, "Addition");
        File removal = Utils.join(RemovalArea, "Removal");
        if (addition.exists()) {
            addition.delete();
        }
        if (removal.exists()) {
            removal.delete();
        }
    }

    public static void mergeCommit(String branchName, Commit secondParent) {
        File HEAD = FindHead();
        String currentBranch = readContentsAsString(HEAD);
        String message = "Merged " + branchName + " into " + currentBranch + ".";

        Commit currentCommit = getCurrentCommit();

        Commit newCommit = new Commit(
                message,
                currentCommit,
                secondParent,
                GetAdditionMap(),
                GetRemovalMap()
        );

        SaveCommit(newCommit);
        moveHEAD(newCommit);
        clearStagingArea();
    }

    private static void moveHEAD(Commit commit) {
        String currentBranch = getCurrentBranch();
        File branchFile = Utils.join(Branches, currentBranch);
        Utils.writeContents(branchFile, commit.getCommitID());
    }


    private static boolean untrackedFile(HashMap<String, String> blobs) {
        Commit currentCommit = getCurrentCommit();
        HashMap<String, String> currentBlobs = currentCommit.getBlobs();
        // untracked file in the way check
        for (String fileName : blobs.keySet()) {
            File cwdFile = Utils.join(CWD, fileName);
            if (cwdFile.exists()
                    && !currentBlobs.containsKey(fileName)) {
                return false;
            }
        }
        return true;
    }

    private static String readBlob(String blobName) {
        File blobFile = Utils.join(Blobs, blobName);
        return Utils.readContentsAsString(blobFile); // ❌
    }


    private static void SaveCommit(Commit commit) {
        File Thiscommit = Utils.join(Commits, commit.getCommitID());
        Utils.writeObject(Thiscommit, commit);
    }

    private static File FindHead() {
        return Utils.join(heads, "head");
    }

    private static File FindCommit() {
        File head = FindHead();
        String branchName = readContentsAsString(head);

        File branchFile = Utils.join(Branches, branchName);
        String commitID = readContentsAsString(branchFile);

        return Utils.join(Commits, commitID);
    }

    private static HashMap<String, String> GetAdditionMap() {
        File Addition = Utils.join(AdditionArea, "Addition");
        return Addition.exists() ?
                Utils.readObject(Addition, HashMap.class)
                : new HashMap<>();
    }

    private static HashMap<String, String> GetRemovalMap() {
        File Removal = Utils.join(RemovalArea, "Removal");
        return Removal.exists() ?
                Utils.readObject(Removal, HashMap.class)
                : new HashMap<>();
    }

    public static String getCurrentBranch() {
        return readContentsAsString(FindHead()).trim();
    }

    public static Commit getCurrentCommit() {
        File commitFile = FindCommit();
        return Utils.readObject(commitFile, Commit.class);
    }

    public static Commit getCommitByBranch(String branchName) {
        File branchFile = Utils.join(Branches, branchName);
        if (!branchFile.exists()) return null;
        String commitID = readContentsAsString(branchFile).trim();
        File commitFile = Utils.join(Commits, commitID);
        return Utils.readObject(commitFile, Commit.class);
    }

    // 获取当前工作区文件列表
    private static List<String> getCWDFileList() {
        List<String> files = Utils.plainFilenamesIn(CWD);
        return files != null ? files : new ArrayList<>();
    }

    // 判断文件是否被 commit 跟踪
    private static boolean isTracked(Commit commit, String fileName) {
        return commit.getBlobs().containsKey(fileName);
    }


    /**
     * When we call 'init' methond, we should create a '.gitlet' directory in cwd.
     * and create a commit with the message 'initial commit'
     * also has a branch called Master pointing to the initial commit.
     */
    public static void init() {
        //if .gitlet dir doesn't exists
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdirs();
            StagingArea.mkdirs();
            AdditionArea.mkdirs();
            RemovalArea.mkdirs();
            Commits.mkdirs();
            Blobs.mkdirs();
            refs.mkdirs();
            Branches.mkdirs();
            heads.mkdirs();

            /* make a initial commit */
            Commit initial = new Commit("initial commit", null, null, null, null);
            /* save the commit. */
            SaveCommit(initial);

            /* create a master branch and a head, points to the initial commit. */
            File branch = Utils.join(Branches, "master");
            Utils.writeContents(branch, initial.getCommitID());

            File head = Utils.join(heads, "head");
            Utils.writeContents(head, "master");
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }

    /**
     * When we add, we put the corresponding file into the staging area.
     * First, we need to find the file according to its name.
     * Second, create a hashmap, and copy the map from the current StagingArea.
     * Then append the new map to this file.
     * Save the file.
     * <p>
     * If file has been saved in the directory, don't do anything.
     */
    public static void add(String filename) {
        File Addition = Utils.join(AdditionArea, "Addition");
        File Removal = Utils.join(RemovalArea, "Removal");

        File file = Utils.join(CWD, filename);
        /* If file doesn't exist. Print a warning. */
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        Blob blob = new Blob(file);

        HashMap<String, String> addition = GetAdditionMap();

        HashMap<String, String> removal = GetRemovalMap();

        /* read the current commit from current HEAD. */
        Commit commit = getCurrentCommit();
        HashMap<String, String> CommitBlobs = commit.getBlobs();

        /* if the file is in the removal area, remove it from removal area. */
        if (removal.get(filename) != null) {
            removal.remove(filename);
            Utils.writeObject(Removal, removal);
        }

        /* if the file id equals the id in the blobs, remove it from the existing addition area.
         *  else, save the file in the addition area and create a blob in the Blobs directory.
         */
        if (blob.getBlobID().equals(CommitBlobs.get(filename))) {
            addition.remove(filename);
            Utils.writeObject(Addition, addition);
        } else {
            addition.put(filename, blob.getBlobID());
            Utils.writeObject(Addition, addition);
            File BlobFile = join(Blobs, blob.getBlobID());
            Utils.writeObject(BlobFile, blob);
        }
    }

    public static void commit(String message) {
        /* if message is null, throw a warning. */
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        }

        /* find the addition and the removal map. If they are all null, throw a warning. */
        File Addition = Utils.join(AdditionArea, "Addition");
        File Removal = Utils.join(RemovalArea, "Removal");

        HashMap<String, String> addition = GetAdditionMap();
        HashMap<String, String> removal = GetRemovalMap();

        if (!addition.isEmpty() && !removal.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        Commit ParentCommit = getCurrentCommit();

        /* Create a new commit. */
        Commit commit = new Commit(message, ParentCommit, null, addition, removal);
        SaveCommit(commit);

        /* Clear addition area and removal area. */
        if (Addition.exists()) {
            Utils.writeObject(Addition, new HashMap<>());
        }
        if (Removal.exists()) {
            Utils.writeObject(Removal, new HashMap<>());
        }

        /* Move the HEAD pointer. */
        String branchName = getCurrentBranch();
        File branchFile = Utils.join(Branches, branchName);

        Utils.writeContents(branchFile, commit.getCommitID());

    }

    public static void rm(String filename) {
        File Addition = Utils.join(AdditionArea, "Addition");
        File Removal = Utils.join(RemovalArea, "Removal");
        File file = Utils.join(CWD, filename);

        HashMap<String, String> addition = GetAdditionMap();
        HashMap<String, String> removal = GetRemovalMap();

        Commit commit = getCurrentCommit();
        HashMap<String, String> CommitBlobs = commit.getBlobs();

        /* If doc doesn't exist. */
        if (!addition.containsKey(filename) && !CommitBlobs.containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        /* If the file is tracked by the current addition area. */
        if (addition.get(filename) != null) {
            addition.remove(filename);
            Utils.writeObject(Addition, addition);
            return;
        }

        /* If the file is tracked by the current HEAD. */
        if (CommitBlobs.containsKey(filename)) {
            removal.put(filename, CommitBlobs.get(filename));
            Utils.writeObject(Removal, removal);
            /* If the file exists in the CWD.Delete it. */
            if (file.exists()) {
                Utils.restrictedDelete(file);
            }
        }
    }

    public static void log() {
        //find the current head
        Commit commit = getCurrentCommit();

        //stage all the information from the current commit to the first commit(null commit)
        while (commit != null) {
            System.out.println("===");
            System.out.println();
            System.out.println("Commit " + commit.getCommitID());
            System.out.println();
            //TODO: if this is a merged commit?
            System.out.println("Date " + commit.getTimestamp());
            System.out.println();
            System.out.println(commit.getMessage());
            System.out.println();
            commit = commit.getParent();
        }
    }

    public static void global_log() {
        //Find all the names in Commits dir
        List<String> commits = Utils.plainFilenamesIn(Commits);
        for (String commitID : commits) {
            //Find the file content by its name
            File nowCommit = Utils.join(Commits, commitID);
            Commit commit = Utils.readObject(nowCommit, Commit.class);

            System.out.println("===");
            System.out.println();
            System.out.println("Commit " + commit.getCommitID());
            System.out.println();
            //TODO: if this is a merged commit?
            System.out.println("Date " + commit.getTimestamp());
            System.out.println();
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    public static void find(String message) {
        //Find all the names in Commits dir
        List<String> commits = Utils.plainFilenamesIn(Commits);
        for (String commitID : commits) {
            //Find the file content by its name
            File nowCommit = Utils.join(Commits, commitID);
            Commit commit = Utils.readObject(nowCommit, Commit.class);

            if (commit.getMessage().equals(message)) {
                System.out.println("Found commit " + commit.getCommitID());
            }
        }
    }

    public static void status() {
        //Print branches and mark the current branch
        System.out.println("=== Branches ===");
        System.out.println();
        List<String> branches = Utils.plainFilenamesIn(Branches);
        String headName = getCurrentBranch();
        for (String branchName : branches) {
            if (branchName.equals(headName)) {
                System.out.println("*" + branchName);
                System.out.println();
            } else {
                System.out.println(branchName);
                System.out.println();
            }
        }

        //Find files in Staged Area and Removal Area
        HashMap<String, String> addition = GetAdditionMap();
        HashMap<String, String> removal = GetRemovalMap();
        List<String> additionKeys = new ArrayList<>(addition.keySet());
        List<String> removalKeys = new ArrayList<>(removal.keySet());
        Collections.sort(additionKeys);
        Collections.sort(removalKeys);

        System.out.println("=== Staged Files ===");
        System.out.println();

        for (String fileName : additionKeys) {
            System.out.println(fileName);
            System.out.println();
        }

        System.out.println("=== Removed Files ===");
        System.out.println();
        for (String fileName : removalKeys) {
            System.out.println(fileName);
            System.out.println();
        }

        //Files modified in the working directory
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        //Find current commit environment
        Commit commit = getCurrentCommit();
        HashMap<String, String> blobs = commit.getBlobs();

        List<String> CWD_files = Utils.plainFilenamesIn(CWD);
        if (CWD_files != null) {
            for (String fileName : CWD_files) {
                //1.Tracked in the current commit, changed in the working directory, but not staged
                File file = Utils.join(CWD, fileName);
                byte[] fileBytes = Utils.readContents(file);
                String fileContentSHA1 = Utils.sha1((Object) fileBytes);
                if (!addition.containsKey(fileName)
                        && !removal.containsKey(fileName)  //not staged
                        && blobs.containsKey(fileName)   //tracked in the current commit
                        && !blobs.get(fileName).equals(fileContentSHA1))  //changed
                {
                    System.out.println(fileName + " (modified) ");
                    System.out.println();
                }
                //2.Staged for addition, but with different contents than in the working directory
                else if (addition.containsKey(fileName)
                        && !addition.get(fileName).equals(fileContentSHA1)) {
                    System.out.println(fileName + " (modified) ");
                }
            }


            //3.Staged for addition, but deleted in the working directory
            for (String additionKey : addition.keySet()) {
                if (!CWD_files.contains(additionKey)) {
                    System.out.println(additionKey + " (deleted) ");
                    System.out.println();
                }
            }

            //4.Not staged for removal,
            //but tracked in the current commit and deleted from the working directory.
            for (String fileName : blobs.keySet()) {
                if (!removal.containsKey(fileName)
                        && !CWD_files.contains(fileName)
                        && !addition.containsKey(fileName)
                ) {
                    System.out.println(fileName + " (deleted) ");
                }
            }
        }


        //Files untracked
        System.out.println("=== Untracked Files ===");
        System.out.println();
        if (CWD_files != null) {
            for (String fileName : CWD_files) {
                if (!blobs.containsKey(fileName)
                        && !addition.containsKey(fileName)
                ) {
                    System.out.println(fileName);
                    System.out.println();
                }
            }
        }
    }

    //There are three versions of the checkout command.

    //1.java gitlet.Main checkout -- [file name],get the file from the current HEAD
    public static void checkout(String fileName) {
        File commitFile = FindCommit();
        checkoutFromCommit(fileName, commitFile);
    }

    //2.java gitlet.Main checkout [commit id] -- [file name]
    public static void checkout(String commitID, String fileName) {
        //Find the given commit
        File givenCommit = join(Commits, commitID);
        checkoutFromCommit(fileName, givenCommit);
    }

    private static void checkoutFromCommit(String fileName, File givenCommit) {
        if (!givenCommit.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Commit commit = Utils.readObject(givenCommit, Commit.class);
        HashMap<String, String> blobs = commit.getBlobs();

        if (!blobs.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        String fileID = blobs.get(fileName);
        File blobFile = Utils.join(Blobs, fileID);

        Blob blob = Utils.readObject(blobFile, Blob.class);
        byte[] fileBytes = blob.getContent();

        File fileToChange = Utils.join(CWD, fileName);
        Utils.writeContents(fileToChange, fileBytes);
    }


    //3.java gitlet.Main checkout [branch name]
    public static void checkoutBranch(String branchName) {
        File Head = FindHead();

        //Warnings
        List<String> branches = Utils.plainFilenamesIn(Branches);
        if (branches != null && !branches.contains(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }

        String currentBranch = readContentsAsString(Head);
        if (branchName.equals(currentBranch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        //The CWD files
        List<String> CWD_files = Utils.plainFilenamesIn(CWD);

        //Take the given branch
        Commit commit = getCommitByBranch(branchName);
        HashMap<String, String> blobs = commit.getBlobs();

        Commit currentBranchCommit = getCommitByBranch(currentBranch);
        HashMap<String, String> currentBlobs = currentBranchCommit.getBlobs();

        if (!untrackedFile(blobs)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }

        //Find each file in the given branch.Overwrite the existing file or create a new file.
        for (String fileName : blobs.keySet()) {
            File blobFile = Utils.join(Blobs, blobs.get(fileName));
            Blob blob = Utils.readObject(blobFile, Blob.class);
            byte[] fileBytes = blob.getContent();

            File currentFile = Utils.join(CWD, fileName);
            Utils.writeContents(currentFile, fileBytes);
        }

        //Any files that are tracked in the current branch
        //but are not present in the checked-out branch are deleted
        if (CWD_files != null) {
            for (String currentFileName : CWD_files) {
                if (!blobs.containsKey(currentFileName)
                        && currentBlobs.containsKey(currentFileName)) {
                    File deleteFile = Utils.join(CWD, currentFileName);
                    Utils.restrictedDelete(deleteFile);
                }
            }
        }

        //Change the head to the given branch
        Utils.writeContents(Head, branchName);

        //Clear the Staging area
        clearStagingArea();
    }

    public static void branch(String branchName) {
        File branchFile = Utils.join(Branches, branchName);
        if (branchFile.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        Commit commit = getCurrentCommit();
        Utils.writeContents(branchFile, commit.getCommitID());
    }


    public static void rm_branch(String branchName) {
        //Fail cases
        File branchFile = Utils.join(Branches, branchName);

        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        String nowHead = getCurrentBranch();
        if (nowHead.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }

        branchFile.delete();
    }

    private static Commit findSplitPoint(Commit currentBranch, Commit givenBranch) {
        // 1. 收集 current 分支的所有祖先（包括自己）
        Set<String> currentAncestors = new HashSet<>();
        Queue<Commit> q = new LinkedList<>();
        q.add(currentBranch);

        while (!q.isEmpty()) {
            Commit c = q.poll();
            if (c == null || currentAncestors.contains(c.getCommitID())) {
                continue;
            }
            currentAncestors.add(c.getCommitID());
            q.add(c.getParent());
            q.add(c.getSecondParent());
        }

        // 2. 从 given 分支出发 BFS，找到第一个公共祖先
        Queue<Commit> q2 = new LinkedList<>();
        q2.add(givenBranch);

        while (!q2.isEmpty()) {
            Commit c = q2.poll();
            if (c == null) {
                continue;
            }
            if (currentAncestors.contains(c.getCommitID())) {
                return c;   // 正确的 split point
            }
            q2.add(c.getParent());
            q2.add(c.getSecondParent());
        }

        return null; // 理论上不会发生
    }


    private static void checkoutFromGiven(String fileName, String branchName) {
        File branchFile = Utils.join(Branches, branchName);
        String commitId = Utils.readContentsAsString(branchFile);
        Commit givenCommit = Utils.readObject(
                Utils.join(Commits, commitId),
                Commit.class
        );

        String blobId = givenCommit.getBlobs().get(fileName);
        File blobFile = Utils.join(Blobs, blobId);

        Blob blob = Utils.readObject(blobFile, Blob.class);
        byte[] contents = blob.getContent();

        File file = Utils.join(CWD, fileName);
        Utils.writeContents(file, contents);
    }


    /* merge the target branch to the current branch */
    public static void merge(String branchName) {
        // 1. Check branch existence
        File branchFile = Utils.join(Branches, branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        // 2. Check if merging the current branch into itself
        String currentBranch = getCurrentBranch();
        if (branchName.equals(currentBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        // 3. Get commits
        Commit currentCommit = getCurrentCommit();
        String givenCommitID = readContentsAsString(branchFile);
        Commit givenCommit = readObject(Utils.join(Commits, givenCommitID), Commit.class);

        // 4. Find split point
        Commit splitCommit = findSplitPoint(currentCommit, givenCommit);

        // 5. Check for special cases
        // Case 1: GIVEN branch is ancestor of CURRENT
        // 什么都不用做
        if (splitCommit.getCommitID().equals(givenCommit.getCommitID())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }

        // Case 2: CURRENT branch is ancestor of GIVEN → fast-forward
        if (splitCommit.getCommitID().equals(currentCommit.getCommitID())) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }


        // 6. Check for untracked files that would be overwritten
        if (!untrackedFile(givenCommit.getBlobs())) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }

        // 7. Merge blobs
        HashMap<String, String> splitBlobs = splitCommit.getBlobs();
        HashMap<String, String> currentBlobs = currentCommit.getBlobs();
        HashMap<String, String> givenBlobs = givenCommit.getBlobs();

        Set<String> allFiles = new HashSet<>();
        allFiles.addAll(splitBlobs.keySet());
        allFiles.addAll(currentBlobs.keySet());
        allFiles.addAll(givenBlobs.keySet());

        boolean conflict = false;

        for (String fileName : allFiles) {
            String s = splitBlobs.get(fileName); // from split
            String c = currentBlobs.get(fileName); // from current
            String g = givenBlobs.get(fileName); // from given

            // Case 1: Unmodified in current, removed in given → remove
            if (Objects.equals(c, s) && g == null) {
                rm(fileName);
            }
            // Case 2: Unmodified in current, changed in given → accept given
            else if (Objects.equals(c, s) && g != null && !Objects.equals(g, s)) {
                checkoutFromGiven(fileName, branchName);
                add(fileName);
            }
            // Case 3: Removed in current, unmodified in given → do nothing
            else if (c == null && Objects.equals(g, s)) {
                // do nothing
            }
            // Case 4: Both modified differently → conflict
            else if (!Objects.equals(c, g)) {
                String currentContent = (c == null) ? "" : readBlob(c);
                String givenContent = (g == null) ? "" : readBlob(g);
                String conflictText = "<<<<<<< HEAD\n" + currentContent + "=======\n" + givenContent + ">>>>>>>\n";
                File file = Utils.join(CWD, fileName);
                Utils.writeContents(file, conflictText);
                add(fileName);
                conflict = true;
            }

        }

        // 8. Notify conflict if any
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }

        // 9. Create merge commit with second parent
        mergeCommit(branchName, givenCommit);
    }



}
