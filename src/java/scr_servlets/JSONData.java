package scr_servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSONData extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fromdate = request.getParameter("fromdate");
        String todate = request.getParameter("todate");
        String filetype = request.getParameter("filetype");

        if (fromdate.length() > 0 && todate.length() > 0) {
            //send based on file_type
        } else if (fromdate.length() == 0 && todate.length() == 0) {
            //send all
        } else if (fromdate.length() > 0 && todate.length() != 0) {
            //send all from fromdate
        } else {
            //send all
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(fromdate.isEmpty() + "--" + todate + "--" + filetype);
        response.getWriter().println(json);

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
