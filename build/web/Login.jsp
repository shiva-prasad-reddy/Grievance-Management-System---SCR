<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (session.getAttribute("ID") != null) {
        response.sendRedirect("home");
    }
%>
<%@page import="scr.TYPE"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <link rel="icon" type="image/png" href="static/images/favicon-32x32.png" sizes="32x32" />
        <link rel="icon" type="image/png" href="static/images/favicon-16x16.png" sizes="16x16" />
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>S.C.R</title>
        <link href="static/css/bootstrap.min.css" rel="stylesheet">
        <link href="static/css/custom.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="static/html/header.html" %>
        <div class="container" style="margin-top:30px;">
            <form class="form-signin" action="validate" method="post">
                <h3 class="form-signin-heading">Login</h3>
                <br>
                <label for="inputid" class="sr-only">User Id</label>
                <input type="text" name="id" id="inputid" class="form-control" placeholder="ID" required autofocus>
                <label for="inputpassword" class="sr-only">Password</label>
                <input type="password" name="password" id="inputpassword" class="form-control" placeholder="Password" required>
                <button class="btn btn-lg btn-primary btn-block" type="submit">submit</button>
                <div class="text-muted">
                    <%
                        if (request.getAttribute("message") != null) {
                            out.println(request.getAttribute("message"));
                        }
                    %>
                </div>
            </form>
        </div> 
        <script src="static/js/jquery-3.2.1.js"></script>
        <script src="static/js/bootstrap.min.js"></script>
    </body>
</html>