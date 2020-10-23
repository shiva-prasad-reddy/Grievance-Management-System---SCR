package scr_servlets.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import scr.admin.Details;

public class create_user extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String id, name, designation, station, address, phone, mail, type, pwd;
        id = request.getParameter("id");
        name = request.getParameter("name");
        designation = request.getParameter("designation");
        station = request.getParameter("station");
        address = request.getParameter("address");
        phone = request.getParameter("phone");
        mail = request.getParameter("mail");
        type = request.getParameter("type");
        pwd = request.getParameter("password");
        HttpSession session = request.getSession(false);
        if (id != null && name != null && designation != null && station != null && address != null && phone != null && mail != null && type != null && pwd != null) {
            Details admin = (Details) session.getAttribute("ADMIN");
            admin.createUser(admin.getUser().getId(), id, pwd, name, designation, station, address, phone, mail, type);
            response.sendRedirect("home");
        } else {
            session.setAttribute("message", "Unable to create user.");
            response.sendRedirect("home");
        }
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
