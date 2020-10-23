package scr_servlets.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import scr.admin.Details;

public class delete_user extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String admins[] = request.getParameterValues("admins");
        String users[] = request.getParameterValues("users");
        HttpSession session = request.getSession(false);
        Details admin = (Details) session.getAttribute("ADMIN");
        if (admins != null) {
            for (String i : admins) {
                admin.deleteAdmin(i);
            }
        }
        if (users != null) {
            for (String i : users) {
                admin.deleteUser(i);
            }
        }
        if (admins == null && users == null) {
            session.setAttribute("message", "Select users & Try again.");
        }
        response.sendRedirect("home");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
