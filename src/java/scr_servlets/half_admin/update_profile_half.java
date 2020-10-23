package scr_servlets.half_admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import scr.half_admin.Details;

public class update_profile_half extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name, designation, station, address, phone, mail;
        name = request.getParameter("name");
        designation = request.getParameter("designation");
        station = request.getParameter("station");
        address = request.getParameter("address");
        phone = request.getParameter("phone");
        mail = request.getParameter("mail");

        HttpSession session = request.getSession(false);
        if (name != null && designation != null && station != null && address != null && phone != null && mail != null) {
            Details admin = (Details) session.getAttribute("ADMIN");
            admin.updateHalfAdminProfile(name, designation, station, address.trim(), phone, mail);
            response.sendRedirect("home");
        } else {
            session.setAttribute("message", "Unable To Update Profile.");
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
