
package scr;

import java.util.ArrayList;

public class TransactionsDetails {

    String status;
    String tid;
    String subject;
    String file_type;
    String file_no;
    String send_date;
    String target_date;
    String sender_id;
    ArrayList<String> files;

    public ArrayList<String> getFiles() {

        return this.files;
    }

    public String getTid() {
        return tid;
    }

    public String getSubject() {
        return subject;
    }

    public String getFile_type() {
        return file_type;
    }

    public String getFile_no() {
        return file_no;
    }

    public String getSend_date() {
        return send_date;
    }

    public String getTarget_date() {
        return target_date;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public TransactionsDetails(String tid, String subject, String file_type, String file_no, String send_date, String target_date, String sender_id, ArrayList<String> files) {
        this.tid = tid;
        this.subject = subject;
        this.file_type = file_type;
        this.file_no = file_no;
        this.send_date = send_date;
        this.target_date = target_date;
        this.sender_id = sender_id;
        this.files = files;
        this.status = "PENDING";
    }

}
