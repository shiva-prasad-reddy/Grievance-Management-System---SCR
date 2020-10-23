package scr_servlets.half_admin;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import scr.half_admin.Details;

public class ForwardFiles extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String users[] = request.getParameterValues("users");
        String file_names[] = request.getParameterValues("selectedfiles");

        HttpSession session = request.getSession(false);

        if (users != null && file_names != null) {
            Details admin = (Details) session.getAttribute("ADMIN");
           
            if (users.length > 0 && file_names.length > 0) {
                admin.forward(file_names, users);
                response.sendRedirect("home");
            }
        } else {
            session.setAttribute("message", "Select Users and Files.");
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
