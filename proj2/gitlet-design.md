# Gitlet Design Document

**Name**:Noenx

## Classes and Data Structures

### Commit

* This class represents commits saved in the directory **'.gitlet/commits'**.Every commit is serialized in this directory.Also can be anti-serialized.

* Each commit has a unique **SHA1 ID** so that we can save it by this ID.

**A commit contains:**

- **Metadata** (timestamp,parentcommit)  
- **Blobs**(See the specific document in the part of **Blob**.)
- **Message** (To describe all the changes you have made.**Must be included when you made a commit!**)
- **CommitID** (Actually,it's a Sha1 ID.So we can mark it and serialize it in proper position.)
#### Fields

1. **public String getMessage():** Get the commit message.
2. **public String getTimestamp():** Get the timestamp the moment when created. 
3. **public HashMap<String, String> getBlobs():** Get the Blob Map.(From file name to BlobID)
4. **public Commit getParent():** Get parent commit.
5. **public  String getCommitID():** Get sha1 ID of the commit.


### Blob

* This class represents each file **added** into gitlet.
* Each Blob has a serialized bytes array and a BlobID.

**A Blob contains:**
* **A bytes array:** To save the file in a serialized way.
* **A BlobID:** A sha1 ID calculated by the bytes array.
#### Fields

1. **public String getBlobID():** Get BlobID.
2. **public byte[] getContent():** Get the **serialized** file. 


## Algorithms

## Persistence

