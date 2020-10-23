package scr.user;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import scr.FILE_TYPE;
import scr.TYPE;
import scr.TransactionsDetails;
import scr.User;

public class Details {

    private final User user;
    private String message;
    private InitialContext initialContext = null;
    private Context context = null;
    private DataSource ds = null;
    private Connection connection = null;
    private String Higher_Authority;

    public Details(String id, TYPE type) {
        this.user = new User();
        this.user.setId(id);
        this.user.setType(type);
        this.message = "";
    }

    public User getUser() {
        return this.user;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }

    public void updateUserProfile(String name, String designation, String station, String address, String phone, String mail) throws ServletException {
        PreparedStatement statement = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("UPDATE USERS SET NAME=?,DESIGNATION=?,STATION=?,ADDRESS=?,PHONE=?,MAIL=? WHERE ID=?");
            statement.setString(1, name);
            statement.setString(2, designation);
            statement.setString(3, station);
            statement.setString(4, address);
            phone = phone.trim();
            long Phone = Long.parseLong(phone);
            statement.setBigDecimal(5, BigDecimal.valueOf(Phone));
            statement.setString(6, mail);
            statement.setString(7, this.user.getId());
            int ret = statement.executeUpdate();
            connection.commit();
            if (ret == 0) {
                this.message = "An error ocurred while updating.";
            } else {
                this.message = "Profile updated successfully.";
            }
        } catch (NamingException | SQLException | NumberFormatException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new ServletException(ex.getLocalizedMessage());
            }
            throw new ServletException(e.getLocalizedMessage());

        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public void load_details() throws ServletException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            statement = connection.prepareStatement("SELECT * FROM USERS WHERE ID=?");
            statement.setString(1, this.user.getId());
            rs = statement.executeQuery();
            if (rs.first()) {
                this.user.setName(rs.getString("NAME"));
                this.user.setDesignation(rs.getString("DESIGNATION"));
                this.user.setStation(rs.getString("STATION"));
                this.user.setAddress(rs.getString("ADDRESS"));
                this.user.setPhone(rs.getString("PHONE"));
                this.user.setMail(rs.getString("MAIL"));
                this.Higher_Authority = rs.getString("ADMIN_ID");
            } else {
                this.message = "An error ocurred while trying to load details..";
            }
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public ArrayList receivedForwarded() throws ServletException {
        ArrayList<TransactionsDetails> list = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            statement = connection.prepareStatement("SELECT FORWARD.FORWARD_TID,FORWARD.FILE_NAME,FORWARD.SENDER,MAIL_BOX.TID,MAIL_BOX.SUBJECT,MAIL_BOX.FILE_TYPE,MAIL_BOX.FILE_NO,MAIL_BOX.SENT_DATE,MAIL_BOX.TARGET_DATE FROM FORWARD LEFT OUTER JOIN MAIL_BOX ON FORWARD.TID=MAIL_BOX.TID AND FORWARD.RECEIVER=? WHERE MAIL_BOX.TID IS NOT NULL ORDER BY FORWARD.CDATE DESC LIMIT 20");
            statement.setString(1, this.user.getId());
            rs = statement.executeQuery();
            while (rs.next()) {
                String ftid = rs.getString("FORWARD_TID");
                String subject = rs.getString("SUBJECT");
                String file_type = rs.getString("FILE_TYPE");
                String file_no = rs.getString("FILE_NO");
                String send_date = rs.getString("SENT_DATE");
                String target_date = rs.getString("TARGET_DATE");
                String sender_id = rs.getString("SENDER");
                String file_name = rs.getString("FILE_NAME");
                ArrayList<String> files = new ArrayList<>();
                files.add(file_name);
                TransactionsDetails det = new TransactionsDetails(ftid, subject, file_type, file_no, send_date, target_date, sender_id, files);
                list.add(det);
            }
            return list;
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public ArrayList received() throws ServletException {

        ArrayList<TransactionsDetails> list = new ArrayList<>();
        PreparedStatement statement = null, statement2 = null;
        ResultSet rs = null, rs2 = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();

            statement = connection.prepareStatement("SELECT * FROM MAIL_BOX WHERE TID IN (SELECT TID FROM TO_BOX WHERE RECEIVER=?) AND FILE_TYPE!=? ORDER BY SENT_DATE DESC LIMIT 20");
            statement.setString(1, this.user.getId());

            statement.setString(2, FILE_TYPE.REPLIED_FILE.toString());
            rs = statement.executeQuery();
            while (rs.next()) {
                String tid = rs.getString("TID");
                String sender_id = rs.getString("SENDER");
                String subject = rs.getString("SUBJECT");
                String file_type = rs.getString("FILE_TYPE");
                String file_no = rs.getString("FILE_NO");
                String send_date = rs.getString("SENT_DATE");
                String target_date = rs.getString("TARGET_DATE");
                ArrayList<String> files = new ArrayList<>();
                statement2 = connection.prepareStatement("SELECT FILE_NAME FROM FILES WHERE TID=?");
                statement2.setString(1, tid);
                rs2 = statement2.executeQuery();
                while (rs2.next()) {
                    files.add(rs2.getString("FILE_NAME"));
                }
                TransactionsDetails det = new TransactionsDetails(tid, subject, file_type, file_no, send_date, target_date, sender_id, files);
                list.add(det);
            }
            return list;
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(rs2);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(statement2);
            DbUtils.closeQuietly(connection);
        }

    }

    public void sendReplyReceived(String tid, String subject, String fileno, String to, ArrayList<String> fnames, String fortid) throws ServletException {
        PreparedStatement statement = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement("INSERT INTO MAIL_BOX VALUES(?,?,?,?,?,?,?)");
            statement.setString(1, tid);
            statement.setString(2, this.user.getId());
            statement.setString(3, subject);
            statement.setString(4, FILE_TYPE.REPLIED_FILE.toString());
            statement.setString(5, fileno);
            statement.setDate(6, new java.sql.Date(new Date().getTime()));
            statement.setDate(7, new java.sql.Date(new Date().getTime()));
            statement.executeUpdate();

            statement = connection.prepareStatement("INSERT INTO TO_BOX VALUES(?,?)");
            statement.setString(1, tid);
            statement.setString(2, to);
            statement.executeUpdate();

            statement = connection.prepareStatement("INSERT INTO FILES VALUES(?,?)");
            statement.setString(1, tid);
            for (String i : fnames) {
                statement.setString(2, i);
                statement.executeUpdate();
            }

            statement = connection.prepareStatement("INSERT INTO REPLY VALUES(?,?,?,?,?)");
            statement.setString(1, tid);
            statement.setString(2, fortid);
            statement.setString(3, this.user.getId());
            statement.setString(4, to);
            statement.setDate(5, new java.sql.Date(new Date().getTime()));
            statement.executeUpdate();

            connection.commit();
            this.message = "Reply sent successfully.";
        } catch (NamingException | SQLException | NumberFormatException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new ServletException(ex.getLocalizedMessage());
            }
            throw new ServletException(e.getLocalizedMessage());

        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public void sendReplyForwarded(String tid, String subject, String fileno, String to, ArrayList<String> fnames, String fortid) throws ServletException {
        PreparedStatement statement = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement("INSERT INTO MAIL_BOX VALUES(?,?,?,?,?,?,?)");
            statement.setString(1, tid);
            statement.setString(2, this.user.getId());
            statement.setString(3, subject);
            statement.setString(4, FILE_TYPE.REPLIED_FILE.toString());
            statement.setString(5, fileno);
            statement.setDate(6, new java.sql.Date(new Date().getTime()));
            statement.setDate(7, new java.sql.Date(new Date().getTime()));
            statement.executeUpdate();

            statement = connection.prepareStatement("INSERT INTO TO_BOX VALUES(?,?)");
            statement.setString(1, tid);
            statement.setString(2, to);
            statement.executeUpdate();

            statement = connection.prepareStatement("INSERT INTO FILES VALUES(?,?)");
            statement.setString(1, tid);
            for (String i : fnames) {
                statement.setString(2, i);
                statement.executeUpdate();
            }

            statement = connection.prepareStatement("INSERT INTO REPLY_TO_FORWARDED VALUES(?,?,?,?,?)");
            statement.setString(1, tid);
            statement.setString(2, fortid);
            statement.setString(3, this.user.getId());
            statement.setString(4, to);
            statement.setDate(5, new java.sql.Date(new Date().getTime()));
            statement.executeUpdate();

            connection.commit();
            this.message = "Reply sent successfully.";
        } catch (NamingException | SQLException | NumberFormatException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new ServletException(ex.getLocalizedMessage());
            }
            throw new ServletException(e.getLocalizedMessage());

        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public String getUpdateFlashMessage() throws ServletException {

        String msg = null;
        PreparedStatement statement = null, statement2 = null;
        ResultSet rs = null, rs2 = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();

            statement = connection.prepareStatement("SELECT TID,MESSAGE FROM MESSAGES ORDER BY STAMP DESC LIMIT 1");
            rs = statement.executeQuery();
            while (rs.next()) {
                String tid = rs.getString("TID");
                msg = rs.getString("MESSAGE");

                ArrayList<String> files = new ArrayList<>();
                statement2 = connection.prepareStatement("SELECT FILE_NAME FROM FILES WHERE TID=?");
                statement2.setString(1, tid);
                rs2 = statement2.executeQuery();
                while (rs2.next()) {
                    files.add(rs2.getString("FILE_NAME"));
                }

                for (String j : files) {
                    msg = msg + "<br><a href = 'downloadfile?" + j + "' >" + j.substring(j.lastIndexOf("_") + 1, j.length()) + "</a><br>";
                }

            }
            return msg;
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(rs2);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(statement2);
            DbUtils.closeQuietly(connection);
        }

    }

    public HashMap summary() throws ServletException {
        HashMap<String, String> map = new HashMap<>();
        String rec_inf = "0", rec_repd = "0";
        String for_inf = "0", for_repd = "0";
        String rep_forw = "0", rep_recd = "0";
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();

            statement = connection.prepareStatement("SELECT COUNT(TID) AS NO FROM MAIL_BOX WHERE TID IN (SELECT DISTINCT TID FROM TO_BOX WHERE RECEIVER=?) AND FILE_TYPE=?");
            statement.setString(1, this.user.getId());
            statement.setString(2, FILE_TYPE.INFORMATIVE.toString());
            rs = statement.executeQuery();
            if (rs.next()) {
                rec_inf = rs.getString("NO");
            }
            statement.setString(2, FILE_TYPE.REPLY_DUE.toString());
            rs = statement.executeQuery();
            if (rs.next()) {
                rec_repd = rs.getString("NO");
            }

            statement = connection.prepareStatement("SELECT COUNT(FORWARD_TID) AS NO FROM FORWARD WHERE RECEIVER=? AND TID IN (SELECT TID FROM MAIL_BOX WHERE FILE_TYPE=?)");
            statement.setString(1, this.user.getId());
            statement.setString(2, FILE_TYPE.INFORMATIVE.toString());
            rs = statement.executeQuery();
            if (rs.next()) {
                for_inf = rs.getString("NO");
            }
            statement.setString(2, FILE_TYPE.REPLY_DUE.toString());
            rs = statement.executeQuery();
            if (rs.next()) {
                for_repd = rs.getString("NO");
            }

            statement = connection.prepareStatement("SELECT COUNT(TID) AS NO FROM REPLY_TO_FORWARDED WHERE FROM_ID=?");
            statement.setString(1, this.user.getId());
            rs = statement.executeQuery();
            if (rs.next()) {
                rep_forw = rs.getString("NO");
            }
            statement = connection.prepareStatement("SELECT COUNT(TID) AS NO FROM REPLY WHERE FROM_ID=?");
            statement.setString(1, this.user.getId());
            rs = statement.executeQuery();
            if (rs.next()) {
                rep_recd = rs.getString("NO");
            }

            int inf = Integer.parseInt(rec_inf) + Integer.parseInt(for_inf);
            int repd = Integer.parseInt(rec_repd) + Integer.parseInt(for_repd);
            int repl = Integer.parseInt(rep_forw) + Integer.parseInt(rep_recd);

            map.put(FILE_TYPE.INFORMATIVE.toString(), String.valueOf(inf));
            map.put(FILE_TYPE.REPLY_DUE.toString(), String.valueOf(repd));
            map.put(FILE_TYPE.REPLIED_FILE.toString(), String.valueOf(repl));

            return map;
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public ArrayList receivedForJson(String FILETYPE) throws ServletException {

        ArrayList<TransactionsDetails> list = new ArrayList<>();
        PreparedStatement statement = null, statement2 = null;
        ResultSet rs = null, rs2 = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();

            statement = connection.prepareStatement("SELECT * FROM MAIL_BOX WHERE TID IN (SELECT TID FROM TO_BOX WHERE RECEIVER=?) AND FILE_TYPE=? ORDER BY SENT_DATE DESC LIMIT 50");
            statement.setString(1, this.user.getId());

            statement.setString(2, FILETYPE);
            rs = statement.executeQuery();
            while (rs.next()) {
                String tid = rs.getString("TID");
                String sender_id = rs.getString("SENDER");
                String subject = rs.getString("SUBJECT");
                String file_type = rs.getString("FILE_TYPE");
                String file_no = rs.getString("FILE_NO");
                String send_date = rs.getString("SENT_DATE");
                String target_date = rs.getString("TARGET_DATE");
                ArrayList<String> files = new ArrayList<>();
                statement2 = connection.prepareStatement("SELECT FILE_NAME FROM FILES WHERE TID=?");
                statement2.setString(1, tid);
                rs2 = statement2.executeQuery();
                while (rs2.next()) {
                    files.add(rs2.getString("FILE_NAME"));
                }
                TransactionsDetails det = new TransactionsDetails(tid, subject, file_type, file_no, send_date, target_date, sender_id, files);
                list.add(det);
            }

            statement = connection.prepareStatement("SELECT FORWARD.FORWARD_TID,FORWARD.FILE_NAME,FORWARD.SENDER,MAIL_BOX.TID,MAIL_BOX.SUBJECT,MAIL_BOX.FILE_TYPE,MAIL_BOX.FILE_NO,MAIL_BOX.SENT_DATE,MAIL_BOX.TARGET_DATE FROM FORWARD LEFT OUTER JOIN MAIL_BOX ON FORWARD.TID=MAIL_BOX.TID AND FORWARD.RECEIVER=?  WHERE MAIL_BOX.TID IS NOT NULL AND MAIL_BOX.FILE_TYPE=? ORDER BY FORWARD.CDATE DESC LIMIT 50");
            statement.setString(1, this.user.getId());
            statement.setString(2, FILETYPE);
            rs = statement.executeQuery();
            while (rs.next()) {
                String ftid = rs.getString("FORWARD_TID");
                String subject = rs.getString("SUBJECT");
                String file_type = rs.getString("FILE_TYPE");
                String file_no = rs.getString("FILE_NO");
                String send_date = rs.getString("SENT_DATE");
                String target_date = rs.getString("TARGET_DATE");
                String sender_id = rs.getString("SENDER");
                String file_name = rs.getString("FILE_NAME");
                ArrayList<String> files = new ArrayList<>();
                files.add(file_name);
                TransactionsDetails det = new TransactionsDetails(ftid, subject, file_type, file_no, send_date, target_date, sender_id, files);
                list.add(det);
            }

            return list;
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(rs2);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(statement2);
            DbUtils.closeQuietly(connection);
        }

    }

    public ArrayList repliedForJson() throws ServletException {

        ArrayList<TransactionsDetails> list = new ArrayList<>();
        PreparedStatement statement = null, statement2 = null;
        ResultSet rs = null, rs2 = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();

            statement = connection.prepareStatement("SELECT * FROM MAIL_BOX WHERE SENDER=? AND FILE_TYPE=? ORDER BY SENT_DATE DESC LIMIT 50");
            statement.setString(1, this.user.getId());
            statement.setString(2, FILE_TYPE.REPLIED_FILE.toString());
            rs = statement.executeQuery();
            while (rs.next()) {
                String tid = rs.getString("TID");
                String sender_id = rs.getString("SENDER");
                String subject = rs.getString("SUBJECT");
                String file_type = rs.getString("FILE_TYPE");
                String file_no = rs.getString("FILE_NO");
                String send_date = rs.getString("SENT_DATE");
                String target_date = rs.getString("TARGET_DATE");
                ArrayList<String> files = new ArrayList<>();
                statement2 = connection.prepareStatement("SELECT FILE_NAME FROM FILES WHERE TID=?");
                statement2.setString(1, tid);
                rs2 = statement2.executeQuery();
                while (rs2.next()) {
                    files.add(rs2.getString("FILE_NAME"));
                }
                TransactionsDetails det = new TransactionsDetails(tid, subject, file_type, file_no, send_date, target_date, sender_id, files);
                list.add(det);
            }

            return list;
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(rs2);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(statement2);
            DbUtils.closeQuietly(connection);
        }

    }

}
