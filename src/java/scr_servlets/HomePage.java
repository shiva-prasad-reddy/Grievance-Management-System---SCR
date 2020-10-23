package scr_servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import scr.TYPE;

public class HomePage extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        switch ((TYPE) session.getAttribute("TYPE")) {
            case ADMIN:
                request.getRequestDispatcher("admin.jsp").forward(request, response);
                return;
            case HALF_ADMIN:
                request.getRequestDispatcher("admin_user.jsp").forward(request, response);
                return;
            case USER:
                request.getRequestDispatcher("user.jsp").forward(request, response);
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
