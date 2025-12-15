package gitlet;

import java.io.Serializable;
import java.util.Date;
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
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    private final String message;
    private final String timestamp;
    private final HashMap<String, String> Blobs; //the key is file name,the value is sha1 id.
    private Commit Parent;
    private final String CommitID;

    public Commit(String message, Commit ParentCommit, HashMap<String, String> AddtionsBlobs, HashMap<String, String> RemovalBlobs)
    {
     this.message = message;
     Parent = ParentCommit;
     if (ParentCommit == null)
     {
         timestamp="00:00:00 UTC, Thursday, 1 January 1970";
         Blobs = new HashMap<>();
         Parent = null;
     }
     else
     {
         timestamp = new Date().toString(); //TODO:Format needed to be modified.
         Blobs = new HashMap<>(ParentCommit.getBlobs());
     }

     //add the blobs in the staging area(given all the blobs in addition area are all 'new' blobs)
     for(String filename : AddtionsBlobs.keySet())
     {
         Blobs.put(filename, AddtionsBlobs.get(filename));
     }

     //remove the blobs in the removal area
     for(String filename : RemovalBlobs.keySet())
     {
         Blobs.remove(filename);
     }

     //After all the variables set,calculate the sha1ID
     byte[] tocalculate = Utils.serialize(Blobs);
     CommitID=Utils.sha1(message,
             timestamp,
             Parent == null ? "" : Parent.getCommitID(),
             tocalculate);
    }

    //TODO:removed files to be dealt with!
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
