package gitlet;
import java.io.File;

/** Represents a gitlet blob object.

 * Each blob contains:
 * - Real file content (serialized version)
 * - A SHA1 ID
 */
public class Blob {
    //instance variables

    private String BlobID;
    private byte[] content; //Nobody wants to know the real file!

    public Blob(File NewFile)
    {
        content = Utils.readContents(NewFile);
        BlobID = Utils.sha1(Utils.readContentsAsString(NewFile));
    }

    public String getBlobID()
    {
        return BlobID;
    }

    public byte[] getContent() {
        return content;
    }
}
