
package scr_servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {
    
  
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    
    /**
     * @param req
     * To validate user request and to check whether the user is a authenticated user or not by maintaining session .
     * if session does not contains id attribute or session does not exist the user is redirected to the login page.
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/Login.jsp";
        boolean loggedIn = session != null && session.getAttribute("ID") != null;
        boolean loginRequest = request.getRequestURI().equals(loginURI);
        if (loggedIn || loginRequest) {
            chain.doFilter(request, response);
        } else if (request.getRequestURI().contains("validate")) {
            chain.doFilter(req, res);
        } else if (request.getRequestURI().contains("static")) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(loginURI);
        }

    }

    @Override
    public void destroy() {
    }

}
