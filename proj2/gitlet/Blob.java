package gitlet;
import java.io.File;
import java.io.Serializable;

/** Represents a gitlet blob object.

 * Each blob contains:
 * - Real file content (serialized version)
 * - A SHA1 ID
 */
public class Blob implements Serializable {
    //instance variables

    private final String BlobID;
    private final byte[] content; //Nobody wants to know the real file!

    public Blob(File NewFile)
    {
        content = Utils.readContents(NewFile);
        BlobID = Utils.sha1((Object) content);
    }

    public String getBlobID()
    {
        return BlobID;
    }

    public byte[] getContent() {
        return content;
    }
}
