
package scr;

public enum FILE_TYPE {
    INFORMATIVE,   
    REPLY_DUE,
    REPLIED_FILE  /*if a user or a admin sent a file as a reply then it is 
marked as REPLIED_FILE because it cannot be informative or reply_due */
}
