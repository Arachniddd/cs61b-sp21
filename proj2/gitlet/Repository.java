package gitlet;

import java.io.File;
import java.util.HashMap;

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

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The staging area directory. */
    public static final File StagingArea = join(GITLET_DIR, "StagingArea");
    /** The addition area directory in staging area. */
    public static final File AdditionArea = join(StagingArea, "AdditionArea");
    /** So does the removal area directory. */
    public static final File RemovalArea = join(StagingArea, "RemovalArea");
    /** The commit directory. */
    public static final File Commits =  join(GITLET_DIR, "Commits");
    /** The blob directory. */
    public static final File Blobs =  join(GITLET_DIR, "Blobs");
    /** The branch pointer. */
    public static final File Branches = join(GITLET_DIR, "Branches");
    /** The head pointer. */
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


    /** We can create a commit in the Commits directory! */
    private static void SaveCommit(Commit commit)
    {
        File Thiscommit = Utils.join(Commits, commit.getCommitID());
        Utils.writeObject(Thiscommit, commit);
    }

    private static File FindHead()
    {
        File head = Utils.join(heads, "head");
        String NowBranch = Utils.readContentsAsString(head);
        return Utils.join(Branches, NowBranch);
    }

    private static File FindCommit()
    {
        File branch = FindHead();
        String CommitID = Utils.readContentsAsString(branch);
        return Utils.join(Commits,  CommitID);
    }

    private static HashMap<String,String> GetAdditionMap()
    {
        File Addition =  Utils.join(AdditionArea, "Addition");
        return Addition.exists() ?
                Utils.readObject(Addition, HashMap.class)
                : new HashMap<>();
    }

    private static HashMap<String,String> GetRemovalMap()
    {
        File Removal =  Utils.join(RemovalArea, "Removal");
        return Removal.exists() ?
                Utils.readObject(Removal, HashMap.class)
                : new HashMap<>();
    }

    /** When we call 'init' methond, we should create a '.gitlet' directory in cwd.
     *  and create a commit with the message 'initial commit'
     *  also has a branch called Master pointing to the initial commit.
     */
    public static void init()
    {
        //if .gitlet dir doesn't exists
        if(!GITLET_DIR.exists())
        {
            GITLET_DIR.mkdirs();
            StagingArea.mkdirs();
            AdditionArea.mkdirs();
            RemovalArea.mkdirs();
            Commits.mkdirs();
            Blobs.mkdirs();

            /* make a initial commit */
            Commit initial = new Commit("initial commit", null, null, null);
            /* save the commit. */
            SaveCommit(initial);

            /* create a master branch and a head, points to the initial commit. */
            File branch = Utils.join(Branches, "master");
            Utils.writeContents(branch, initial.getCommitID());

            File head = Utils.join(heads, "head");
            Utils.writeContents(head, "master");
        }
        else
        {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }

    /**
     * When we add, we put the corresponding file into the staging area.
     * First, we need to find the file according to its name.
     * Second, create a hashmap, and copy the map from the current StagingArea.
     * Then append the new map to this file.
     * Save the file.
     *
     * If file has been saved in the directory, don't do anything.
     */
    public static void add(String filename)
    {
        File Addition = Utils.join(AdditionArea, "Addition");
        File Removal = Utils.join(RemovalArea, "Removal");

        File file = Utils.join(CWD, filename);
        /* If file doesn't exists. Print a warning. */
        if(!file.exists())
        {
            System.out.println("File does not exist.");
            return;
        }

        Blob blob = new Blob(file);

        HashMap<String, String> addition = GetAdditionMap();

        HashMap<String, String> removal =  GetRemovalMap();

        /* read the current addition area from current HEAD. */
        File NowCommit = FindCommit();
        Commit commit = Utils.readObject(NowCommit, Commit.class);
        HashMap<String,String> CommitBlobs = commit.getBlobs();

        /* if the file is in the removal area, remove it from removal area. */
        if(removal.get(filename) != null)
        {
            removal.remove(filename);
            Utils.writeObject(Removal, removal);
        }

        /* if the file id equals the id in the blobs, remove it from the existing addition area.
        *  else, save the file in the addition area and create a blob in the Blobs directory.
        */
        if(blob.getBlobID().equals(CommitBlobs.get(filename)))
        {
            addition.remove(filename);
            Utils.writeObject(Addition, addition);
            return;
        }
        else
        {
           addition.put(filename, blob.getBlobID());
           Utils.writeObject(Addition, addition);
           File BlobFile = join(Blobs, blob.getBlobID());
           Utils.writeObject(BlobFile, blob);
        }
    }

    public static void commit(String message)
    {
        /* if message is null, throw a warning. */
        if (message == null || message.trim().isEmpty())
        {
            System.out.println("Please enter a commit message.");
            return;
        }

        /* find the addtion and the removal map. If they are all null, throw a warning. */
        File Addition = Utils.join(AdditionArea, "Addition");
        File Removal = Utils.join(RemovalArea, "Removal");


        if (!Addition.exists() && !Removal.exists())
        {
            System.out.println("No changes added to the commit.");
            return;
        }

        HashMap<String, String> addition = GetAdditionMap();
        HashMap<String, String> removal = GetRemovalMap();

        File ParentCommitFile = FindCommit();
        Commit ParentCommit = Utils.readObject(ParentCommitFile, Commit.class);

        /* Create a new commit. */
        Commit commit = new Commit(message, ParentCommit, addition, removal);
        File CommitFile = join(Commits, commit.getCommitID());
        Utils.writeObject(CommitFile, commit);

        /* Clear addition area and removal area. */
        Utils.restrictedDelete(Addition);
        Utils.restrictedDelete(Removal);

        /* Move the HEAD pointer. */
        File HEAD = FindHead();
        Utils.writeContents(HEAD, commit.getCommitID());
    }

    public static void rm(String filename)
    {
        File file = Utils.join(CWD, filename);

        File Addition =  Utils.join(AdditionArea, "Addition");
        File Removal = Utils.join(RemovalArea, "Removal");

        HashMap<String, String> addition = GetAdditionMap();
        HashMap<String, String> removal = GetRemovalMap();

        File CommitFile = FindCommit();
        Commit commit = Utils.readObject(CommitFile, Commit.class);
        HashMap<String, String> CommitBlobs = commit.getBlobs();

        /* If doc doesn't exist. */
        if (!addition.containsKey(filename) && !CommitBlobs.containsKey(filename))
        {
            System.out.println("No reason to remove the file.");
            return;
        }

        /* If the file is tracked by the current addition area. */
        if (addition.get(filename) != null)
        {
            addition.remove(filename);
            Utils.writeObject(Addition, addition);
        }

        /* If the file is tracked by the current HEAD. */
        if (CommitBlobs.containsKey(filename))
        {
            removal.put(filename, CommitBlobs.get(filename));
            Utils.writeObject(Removal, removal);
            /* If the file exists in the CWD.Delete it. */
            if (file.exists())
            {
                file.delete();
            }
        }
    }
}
