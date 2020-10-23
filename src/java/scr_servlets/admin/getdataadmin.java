package scr_servlets.admin;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import scr.FILE_TYPE;
import scr.TD;
import scr.TransactionsDetails;
import scr.admin.Details;

public class getdataadmin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        ArrayList<TransactionsDetails> list = new ArrayList<>();
        ArrayList<TD> li = new ArrayList<>();
        if (type != null) {
            HttpSession session = request.getSession(false);
            Details admin = (Details) session.getAttribute("ADMIN");
            if (type.equals(FILE_TYPE.REPLIED_FILE.toString())) {
                list = admin.repliedForJson();
                String json = gson.toJson(list);
                response.getWriter().println(json);
            } else {
                li = admin.receivedForJson(type);
                String json = gson.toJson(li);
                response.getWriter().println(json);
            }

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
