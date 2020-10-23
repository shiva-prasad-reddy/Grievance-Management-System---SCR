package scr_servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;

public class checForkUser extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InitialContext initialContext = null;
        Context context = null;
        DataSource ds = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        boolean exists = false;
        String id = request.getParameter("id");
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) context.lookup("connpool");
            connection = ds.getConnection();
            statement = connection.prepareStatement("SELECT ID FROM VALIDATE WHERE ID=?");
            statement.setString(1, id);
            rs = statement.executeQuery();
            if (rs.first()) {
                exists = rs.getString("ID").equals(id);
            }
            response.getWriter().println(exists);
        } catch (NamingException | SQLException e) {
            log(e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
