
<%@page isErrorPage="true" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <link rel="icon" type="image/png" href="static/images/favicon-32x32.png" sizes="32x32" />
        <link rel="icon" type="image/png" href="static/images/favicon-16x16.png" sizes="16x16" />
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>S.C.R</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="static/css/bootstrap.min.css" rel="stylesheet">
        <link href="static/css/custom.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="static/html/header.html" %>
        <div class="container">
            <div style="width: 80%;margin: auto;margin-top: 50px;">
                <div>
                    <h2 class="text-danger">
                        <%= exception.getLocalizedMessage() %>
                    </h2>
                    <div class="well">Click here to <a href="Login.jsp" style="font-size: 1.7em;text-decoration: underline;">Login</a>.</div>
                </div>
            </div>
        </div>
        <script src="static/js/jquery-3.2.1.js"></script>
        <script src="static/js/bootstrap.min.js"></script>
    </body>
</html>