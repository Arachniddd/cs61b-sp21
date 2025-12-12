# Gitlet Design Document

**Name**:Noenx

## Classes and Data Structures

### Commit

* This class represents commits saved in the directory **'.gitlet/commits'**.Every commit is serialized in this directory.Also can be anti-serialized.

* Each commit has a unique **SHA1 ID** so that we can save it by this ID.

**A commit contains:**

- **Metadata** (for example,the timestamp,the author name etc., if you wanna add it.)  
- **Blobs**(See the specific document in the part of **Blob**.)
- **Message** (To describe all the changes you have made.**Must be included when you made a commit!**)
- **CommitID** (Actually,it's a Sha1 ID.So we can mark it and serialize it in proper position.)
#### Fields

1. Field 1
2. Field 2


### Class 2

#### Fields

1. Field 1
2. Field 2


## Algorithms

## Persistence

