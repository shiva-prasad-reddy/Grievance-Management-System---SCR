package scr_servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import scr.TYPE;
import scr.Validation;

public class user_authentication extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        HttpSession session = request.getSession();
        String id = request.getParameter("id");
        String pwd = request.getParameter("password");
        
        if (session.getAttribute("ID") == null) {
            if (id != null && pwd != null) {
                Validation user = new Validation(id, pwd);
                if (user.validate()) {
                    TYPE type = user.getType();
                    session.setAttribute("ID", id);
                    session.setAttribute("TYPE", type);
                    response.sendRedirect("home");
                } else {
                    request.setAttribute("message", user.getErrorMessage());
                    request.getRequestDispatcher("Login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("message", "Enter valid details.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
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
