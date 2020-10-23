package scr.half_admin;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import scr.FILE_TYPE;
import scr.TD;
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

    public void updateHalfAdminProfile(String name, String designation, String station, String address, String phone, String mail) throws ServletException {
        PreparedStatement statement = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("UPDATE ADMINS SET NAME=?,DESIGNATION=?,STATION=?,ADDRESS=?,PHONE=?,MAIL=? WHERE ID=?");
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
        ResultSet rs = null, rs2 = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            statement = connection.prepareStatement("SELECT * FROM ADMINS WHERE ID=?");
            statement.setString(1, this.user.getId());
            rs = statement.executeQuery();
            if (rs.first()) {
                this.user.setName(rs.getString("NAME"));
                this.user.setDesignation(rs.getString("DESIGNATION"));
                this.user.setStation(rs.getString("STATION"));
                this.user.setAddress(rs.getString("ADDRESS"));
                this.user.setPhone(rs.getString("PHONE"));
                this.user.setMail(rs.getString("MAIL"));
                statement = connection.prepareStatement("SELECT ID FROM VALIDATE WHERE TYPE=?");
                statement.setString(1, TYPE.ADMIN.toString());
                rs2 = statement.executeQuery();
                if (rs2.next()) {
                    this.user.setHigherofficial(rs2.getString("ID"));
                }
            } else {
                this.message = "An error ocurred while trying to load details..";
            }
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(rs2);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public void createUser(String creator_id, String id, String pwd, String name, String designation, String station, String address, String phone, String mail) throws ServletException {
        PreparedStatement statement1 = null, statement2 = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            statement1 = connection.prepareStatement("INSERT INTO VALIDATE VALUES(?,?,?)");
            statement1.setString(1, id);
            statement1.setString(2, pwd);
            statement1.setString(3, "USER");
            statement2 = connection.prepareStatement("INSERT INTO USERS VALUES(?,?,?,?,?,?,?,?)");
            statement2.setString(1, id);
            statement2.setString(2, name);
            statement2.setString(3, designation);
            statement2.setString(4, station);
            statement2.setString(5, address);
            phone = phone.trim();
            long Phone = Long.parseLong(phone);
            statement2.setBigDecimal(6, BigDecimal.valueOf(Phone));
            statement2.setString(7, mail);
            statement2.setString(8, creator_id);
            int ret1 = statement1.executeUpdate();
            int ret2 = statement2.executeUpdate();
            connection.commit();
            if (ret1 != 0 && ret2 != 0) {
                this.message = "Created user successfully.";
            } else {
                this.message = "An error ocurred while creating user.";
            }
        } catch (NamingException | SQLException | NumberFormatException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new ServletException(ex.getLocalizedMessage());
            }
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(statement1);
            DbUtils.closeQuietly(statement2);
            DbUtils.closeQuietly(connection);
        }
    }

    public boolean checkUserExistsOrNot(String id) throws ServletException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            statement = connection.prepareStatement("SELECT ID FROM VALIDATE WHERE ID=?");
            statement.setString(1, id);
            rs = statement.executeQuery();
            if (rs.first()) {
                return rs.getString("ID").equals(id);
            } else {
                return false;
            }
        } catch (NamingException | SQLException e) {
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public ArrayList usersCreated() throws ServletException {
        ArrayList<User> list = new ArrayList<>();

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            statement = connection.prepareStatement("SELECT * FROM USERS WHERE ADMIN_ID=?");
            statement.setString(1, this.user.getId());
            rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new User(rs.getString("ID"), rs.getString("NAME"), rs.getString("DESIGNATION"), rs.getString("STATION"), rs.getString("ADDRESS"), rs.getString("PHONE"), rs.getString("MAIL"), TYPE.USER));
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

    public void deleteUser(String id) throws ServletException {
        PreparedStatement statement1 = null, statement2 = null;
        ResultSet rs = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            statement1 = connection.prepareStatement("DELETE FROM USERS WHERE ID=?");
            statement1.setString(1, id);
            statement2 = connection.prepareStatement("DELETE FROM VALIDATE WHERE ID=?");
            statement2.setString(1, id);
            int ret1 = statement1.executeUpdate();
            int ret2 = statement2.executeUpdate();
            connection.commit();
            if (ret1 != 0 && ret2 != 0) {
                this.message = "Successfully deleted.";
            } else {
                this.message = "Error in deleting.Try again.";
            }
        } catch (NamingException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new ServletException(ex.getLocalizedMessage());
            }
            throw new ServletException(e.getLocalizedMessage());
        } finally {

            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement1);
            DbUtils.closeQuietly(statement2);
            DbUtils.closeQuietly(connection);

        }
    }

    public void sendAndStoreFiles(String tid, String subject, String filetype, String fileno, String targetdate, ArrayList<String> persons, ArrayList<String> fnames) throws ServletException {
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
            statement.setString(4, filetype);
            statement.setString(5, fileno);
            Date date = new Date();
            statement.setDate(6, new java.sql.Date(date.getTime()));
            date = new SimpleDateFormat("yyyy-MM-dd").parse(targetdate);
            statement.setDate(7, new java.sql.Date(date.getTime()));
            statement.executeUpdate();

            statement = connection.prepareStatement("INSERT INTO TO_BOX VALUES(?,?)");
            statement.setString(1, tid);
            for (String i : persons) {
                statement.setString(2, i);
                statement.executeUpdate();
            }

            statement = connection.prepareStatement("INSERT INTO FILES VALUES(?,?)");
            statement.setString(1, tid);
            for (String i : fnames) {
                statement.setString(2, i);
                statement.executeUpdate();
            }

            connection.commit();
            this.message = "Files sent successfully.";
        } catch (NamingException | SQLException | NumberFormatException | ParseException e) {
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

    public void sendReply(String tid, String subject, String fileno, String to, ArrayList<String> fnames, String fortid) throws ServletException {
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
            this.message = "Files sent successfully.";
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

                statement2 = connection.prepareStatement("SELECT * FROM REPLY WHERE FOR_TID=? AND FROM_ID=?");
                statement2.setString(1, tid);
                statement2.setString(2, this.user.getId());
                rs2 = statement2.executeQuery();
                if (rs2.next()) {
                    det.setStatus("REPLIED");
                }

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

    public void forward(String[] fnamesandtids, String[] users) throws ServletException {
        PreparedStatement statement = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO FORWARD VALUES(?,?,?,?,?,?)");

            for (String u : users) {
                for (String ft : fnamesandtids) {
                    String tid = ft.substring(ft.length() - 36);
                    String fname = ft.substring(0, ft.length() - 37);
                    UUID uid = UUID.randomUUID();
                    String forward_tid = String.valueOf(uid);
                    statement.setString(1, forward_tid);
                    statement.setString(2, tid);
                    statement.setString(3, fname);
                    statement.setString(4, this.user.getId());
                    statement.setString(5, u);
                    statement.setDate(6, new java.sql.Date(new Date().getTime()));
                    statement.executeUpdate();
                }
            }

            connection.commit();
            this.message = "Files Forwarded.";
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

    public ArrayList repliesForReceived() throws ServletException {

        ArrayList<TransactionsDetails> list = new ArrayList<>();
        PreparedStatement statement = null, statement2 = null;
        ResultSet rs = null, rs2 = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();

            statement = connection.prepareStatement("SELECT * FROM MAIL_BOX WHERE TID IN (SELECT TID FROM REPLY WHERE TO_ID=?) AND FILE_TYPE=? ORDER BY SENT_DATE DESC LIMIT 20");

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

    public ArrayList repliesForForwarded() throws ServletException {

        ArrayList<TransactionsDetails> list = new ArrayList<>();
        PreparedStatement statement = null, statement2 = null;
        ResultSet rs = null, rs2 = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();

            statement = connection.prepareStatement(" SELECT MAIL_BOX.TID,MAIL_BOX.SENDER,MAIL_BOX.SUBJECT,MAIL_BOX.FILE_TYPE,MAIL_BOX.FILE_NO,MAIL_BOX.SENT_DATE,MAIL_BOX.TARGET_DATE,REPLY_TO_FORWARDED.FORWARD_TID,REPLY_TO_FORWARDED.FROM_ID,REPLY_TO_FORWARDED.TO_ID,REPLY_TO_FORWARDED.CDATE FROM MAIL_BOX LEFT OUTER JOIN REPLY_TO_FORWARDED ON REPLY_TO_FORWARDED.TID=MAIL_BOX.TID WHERE MAIL_BOX.FILE_TYPE=? AND REPLY_TO_FORWARDED.TO_ID=? AND REPLY_TO_FORWARDED.FORWARD_TID IS NOT NULL ORDER BY MAIL_BOX.SENT_DATE DESC LIMIT 20;");
            statement.setString(2, this.user.getId());
            statement.setString(1, FILE_TYPE.REPLIED_FILE.toString());
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
                tid = rs.getString("FORWARD_TID");
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

    public void sendReplyToAdmin(String tid, String subject, String fileno, String to, ArrayList<String> fnames, String fortid, String type) throws ServletException {
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

            statement = connection.prepareStatement("INSERT INTO REPLY_TO_ADMIN VALUES(?,?)");
            statement.setString(1, fortid);
            statement.setString(2, type);
            statement.executeUpdate();

            connection.commit();
            this.message = "Files sent successfully.";
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
        String inf = "0";
        String repd = "0";
        String rep_d = "0", rep_f = "0";
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();

            statement = connection.prepareStatement("SELECT COUNT(TID) AS NO FROM MAIL_BOX WHERE TID IN (SELECT TID FROM TO_BOX WHERE RECEIVER=?) AND FILE_TYPE=?");
            statement.setString(1, this.user.getId());
            statement.setString(2, FILE_TYPE.INFORMATIVE.toString());
            rs = statement.executeQuery();
            if (rs.next()) {
                inf = rs.getString("NO");
            }
            statement.setString(2, FILE_TYPE.REPLY_DUE.toString());
            rs = statement.executeQuery();
            if (rs.next()) {
                repd = rs.getString("NO");
            }

            
            
            
            statement = connection.prepareStatement("SELECT COUNT(TID) AS NO FROM REPLY WHERE FROM_ID=?");
            statement.setString(1, this.user.getId());
            rs = statement.executeQuery();
            if (rs.next()) {
                rep_d = rs.getString("NO");
            }
            statement = connection.prepareStatement("SELECT COUNT(TID) AS NO FROM REPLY_TO_FORWARDED WHERE FROM_ID=?");
            statement.setString(1, this.user.getId());
            rs = statement.executeQuery();
            if (rs.next()) {
                rep_f = rs.getString("NO");
            }

            int reply = Integer.parseInt(rep_d) + Integer.parseInt(rep_f);

            map.put(FILE_TYPE.INFORMATIVE.toString(), String.valueOf(inf));
            map.put(FILE_TYPE.REPLY_DUE.toString(), String.valueOf(repd));
            map.put(FILE_TYPE.REPLIED_FILE.toString(), String.valueOf(reply));

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

            statement = connection.prepareStatement("SELECT * FROM MAIL_BOX WHERE TID IN(SELECT TID FROM REPLY WHERE FROM_ID=? ORDER BY CDATE DESC) AND FILE_TYPE=? ORDER BY SENT_DATE DESC LIMIT 50");
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
            
            
            statement = connection.prepareStatement("SELECT * FROM MAIL_BOX WHERE TID IN (SELECT TID FROM REPLY_TO_FORWARDED WHERE FROM_ID=? ORDER BY CDATE DESC) AND FILE_TYPE=? ORDER BY SENT_DATE DESC LIMIT 50");
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
