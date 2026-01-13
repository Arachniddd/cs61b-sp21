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


### Repository

* This class is a tool class.It represents the working directory of gitlet.

**A repository contains:**

1. **Staging area:** Save the temporary files.
2. **Commits:** Save commits.
3. **Blobs:** Save blobs.

#### Fields

1. **public static void init():** Init a '.gitlet' directory with subdirectories and create a initial commit.
2. **public static void add(String filename):** Add a file into staging area(addition area).If the file is in the removal area, remove it from removal area.
If the file is the same as the file in the HEAD, remove it from the current addition area.
Else, add the file and create a blob in the Blobs directory.
3. **public static void commit(String message):** Commit the current commit tree on your device.
You must prepare a message to specify what you have done.
4. **public static void rm(String filename):** If you "rm" a file after you add it, this file will be removed from the addition area.
If you "rm" a file in the current HEAD,it will be untracked.And,**Beware** if the file is in the CWD,the file will be **DELETED**!
5. **public static void log():** Show the commit history from the current head.
6. **public static void global_log():** Show all the commits in directory order.
7. **public static void find(String message):** Find a commit have the exact message.
8. **public static void status():** Show the current Status.
**(Including branches, staged and removed files, modified files, untracked files.)**
9.  **public static void checkout(String fileName):** Takes the version of the file as it exists in the head commit and puts it in the working directory, overwriting the version of the file that’s already there if there is one. The new version of the file is not staged.
10. **public static void checkout(String commitID, String fileName):** Takes the version of the file as it exists in the commit with the given id, and puts it in the working directory, overwriting the version of the file that’s already there if there is one. The new version of the file is not staged.
11. **public static void checkoutBranch(String branchName):** Takes all files in the commit at the head of the given branch, and puts them in the working directory, overwriting the versions of the files that are already there if they exist. Also, at the end of this command, the given branch will now be considered the current branch (HEAD). Any files that are tracked in the current branch but are not present in the checked-out branch are deleted. The staging area is cleared, unless the checked-out branch is the current branch.
12. 
## Algorithms

## Persistence

