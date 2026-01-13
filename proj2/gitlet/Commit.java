package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.util.Formatter;
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

    public Commit(String message, Commit ParentCommit, HashMap<String, String> AdditionsBlobs, HashMap<String, String> RemovalBlobs)
    {
     this.message = message;
     Parent = ParentCommit;
     if (ParentCommit == null)
     {
         timestamp="Thu Jan 1 00:00:00 1970 -0800";
         Blobs = new HashMap<>();
         Parent = null;
     }
     else
     {
         Date date = new Date();
         Formatter formatter = new Formatter();
         formatter.format("%1$ta %1$tb %1$td %1$tT %1$tY %1$tz", date);
         timestamp = formatter.toString();
         Blobs = new HashMap<>(ParentCommit.getBlobs());
     }

     //add the blobs in the staging area(given all the blobs in addition area are all 'new' blobs)
     if(AdditionsBlobs != null) {
         for (String filename : AdditionsBlobs.keySet()) {
             Blobs.put(filename, AdditionsBlobs.get(filename));
         }
     }

     //remove the blobs in the removal area
     if(RemovalBlobs != null) {
         for (String filename : RemovalBlobs.keySet()) {
             Blobs.remove(filename);
         }
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
