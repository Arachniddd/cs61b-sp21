package gitlet;

// TODO: any imports you need here

import gitlet.Utils.*;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

/** Represents a gitlet commit object.

 *  Each commit stores:
 *  - A message
 *  - A timestamp
 *  - A mapping of file names to blob IDs(both are String type)
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
    private String message;
    private String timestamp;
    private HashMap<String, String> Blobs; //the key is file name,the value is sha1 id.
    private Commit Parent;
    private String CommitID;

    /* TODO: fill in the rest of this class. */

    public Commit(String message, Commit ParentCommit, HashMap<String, String> StagedBlobs)
    {
     this.message = message;
     Parent = ParentCommit;
     if (ParentCommit == null)
     {
         timestamp="Wed Dec 31 16:00:00 1969 -0800";
         Blobs = new HashMap<>();
         Parent = null;
     }
     else
     {
         timestamp = new Date().toString();
         Blobs = new HashMap<>(ParentCommit.getBlobs());
         Parent = ParentCommit;
     }

     //add the blobs in the staging area(given all the blobs in addition area are all 'new' blobs)
     for(String filename : StagedBlobs.keySet())
     {
         Blobs.put(filename, StagedBlobs.get(filename));
     }

     //After all the variables set,calculate the sha1ID
     CommitID=Utils.sha1(message,
             timestamp,
             Parent == null ? "" : Parent.getCommitID(),
             Blobs.toString());
    }

    public String getMessage()
    {
        return message;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public HashMap<String, String> getBlobs()
    {
        return Blobs;
    }

    public Commit getParent()
    {
        return Parent;
    }

    public  String getCommitID()
    {
        return CommitID;
    }
}
