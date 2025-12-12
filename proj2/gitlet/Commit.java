package gitlet;

// TODO: any imports you need here

import edu.princeton.cs.algs4.SET;

import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

/** Represents a gitlet commit object.

 *  Each commit stores:
 *  - A message
 *  - A timestamp
 *  - A mapping of file names to blob IDs
 *  - A parent commit (and possibly a second parent for merges)
 *  - A SHA1 ID
 *
 *  Commits form a DAG structure and are stored as serialized files in .gitlet/commits.
 *  @author Noen
 */
public class Commit {
    /**
     * TODO: add instance variables here.

     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    private String meassage;
    private String timestamp;
    private HashMap<String, Blob> Blobs; //use different SHA1ID to represent each blob.
    private Commit Parent;

    /* TODO: fill in the rest of this class. */

    public Commit(String meassage, Commit ParentCommit)
    {
     this.meassage = meassage;
     this.Parent = ParentCommit;
     this.timestamp = new Date().toString();
     if (ParentCommit!=null)
     {
         this.timestamp="Whatever it is just let it be here.";
     }
    }

    public String getMeassage()
    {
        return meassage;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public HashMap<String, Blob> getBlobs()
    {
        return Blobs;
    }

    public Commit getParent()
    {
        return Parent;
    }
}
