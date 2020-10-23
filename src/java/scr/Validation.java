package scr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.dbutils.DbUtils;

public class Validation {

    private final String id, password;
    private String type, error;

    public Validation(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public TYPE getType() {

        switch (this.type) {
            case "ADMIN":
                return TYPE.ADMIN;
            case "HALF_ADMIN":
                return TYPE.HALF_ADMIN;
            default:
                return TYPE.USER;
        }

    }

    public String getErrorMessage() {
        return this.error;
    }

    public boolean validate() {
        InitialContext initialContext = null;
        Context context = null;
        DataSource ds = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            statement = connection.prepareStatement("SELECT ID,PASSWORD,TYPE FROM VALIDATE WHERE ID=?");
            statement.setString(1, this.id);
            rs = statement.executeQuery();
            if (rs.first()) {
                if (rs.getString("PASSWORD").equals(this.password)) {
                    this.type = rs.getString("TYPE");
                    return true;
                } else {
                    error = "password mismatch";
                    return false;
                }
            } else {
                error = "user doesn't exist";
                return false;
            }
        } catch (NamingException | SQLException e) {
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
        return false;
    }

}
