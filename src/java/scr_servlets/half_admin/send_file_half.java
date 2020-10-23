package scr_servlets.half_admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import scr.half_admin.Details;

public class send_file_half extends HttpServlet {

    private String filePath;

    @Override
    public void init() {
        filePath = getServletContext().getInitParameter("file-upload");  //to store uploaded data
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filetype = null, fileno = null, subject = null, targetdate = null;
        HttpSession session = request.getSession(false);

        Details admin = (Details) session.getAttribute("ADMIN");
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(5000000);
                factory.setRepository(new File("c:\\temp"));
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setSizeMax(5000000);
                List items = upload.parseRequest(request);
                Iterator iter = items.iterator();
                ArrayList<String> persons = new ArrayList<>();
                ArrayList<FileItem> files = new ArrayList<>();
                while (iter.hasNext()) {
                    FileItem fi = (FileItem) iter.next();
                    if (fi.isFormField()) {
                        String fieldName = fi.getFieldName();
                        String fieldValue = fi.getString();
                        switch (fieldName) {
                            case "users":
                                persons.add(fieldValue);
                                break;
                            case "admins":
                                persons.add(fieldValue);
                                break;
                            case "filetype":
                                filetype = fieldValue;
                                break;
                            case "fileno":
                                fileno = fieldValue;
                                break;
                            case "subject":
                                subject = fieldValue;
                                break;
                            case "targetdate":
                                targetdate = fieldValue;
                                break;
                            default:
                                break;
                        }
                    } else {
                        String fieldName = fi.getFieldName();
                        if (fieldName.equals("userfile")) {
                            files.add(fi);
                        }
                    }
                }

                if (filetype != null) {
                    if (filetype.equals("INFORMATIVE") && targetdate == null) {

                        Date d = new Date();
                        targetdate = new java.sql.Date(d.getTime()).toString();
                    }
                }

                if (filetype != null && fileno != null && subject != null && targetdate != null && persons.size() > 0) {

                    UUID uid = UUID.randomUUID();
                    String tid = String.valueOf(uid);
                    Date date;
                    int i = 1;
                    ArrayList<String> fnames = new ArrayList<>();
                    for (FileItem fi : files) {
                        String fileName = fi.getName();
                        File file;
                        date = new Date();
                        if (fileName.lastIndexOf("\\") >= 0) {
                            fileName = String.valueOf(date.getTime()) + "_" + i + "_" + fileName.substring(fileName.lastIndexOf("\\"));
                        } else {
                            fileName = String.valueOf(date.getTime()) + "_" + i + "_" + fileName.substring(fileName.lastIndexOf("\\") + 1);
                        }
                        i++;
                        fnames.add(fileName);
                        fi.write(new File(filePath + fileName));
                    }
                    admin.sendAndStoreFiles(tid, subject, filetype, fileno, targetdate, persons, fnames);
                    response.sendRedirect("home");

                } else {
                    session.setAttribute("message", "Unable To Send File.");
                    response.sendRedirect("home");
                }
            } else {
                session.setAttribute("message", "Invalid File.");
                response.sendRedirect("home");
            }
        } catch (Exception ex) {
            throw new ServletException(ex.getLocalizedMessage());
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
