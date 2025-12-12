package gitlet;

import java.io.File;
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
    public static final File Commits =  join(StagingArea, "Commits");
    /** The blob directory. */
    public static final File Blobs =  join(StagingArea, "Blobs");
    /** The head pointer. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /** The branch pointer. */
    public static final File refs = join(GITLET_DIR, "refs");
    public static final File heads = join(GITLET_DIR, "heads");


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
    private void SaveCommit(Commit commit)
    {}


    /** When we call 'init' methond, we should create a '.gitlet' directory in cwd.
     *  and create a commit with the message 'initial commit'
     *  also has a branch called Master pointing to the initial commit.
     */
    public static void init()
    {
        //if .gitlet dir doesn't exists
        if(!GITLET_DIR.exists())
        {
            GITLET_DIR.mkdir();
        }
        else
        {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }
}
