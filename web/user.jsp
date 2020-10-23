
<%@page import="scr.FILE_TYPE"%>
<%@page import="scr.TransactionsDetails"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="scr.TYPE"%>
<%@page import="scr.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="scr.user.Details"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    Details user;
    ArrayList<TransactionsDetails> li;
    int counter = 1;
%>
<%
    try {
        if (session.getAttribute("ADMIN") == null) {
            user = new Details((String) session.getAttribute("ID"), (TYPE) session.getAttribute("TYPE"));
        } else {
            user = (Details) session.getAttribute("ADMIN");
        }
        user.load_details();
        session.setAttribute("ADMIN", user);
%>
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
        <nav class="navbar navbar-inverse visible-xs">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">SCR</a>
                </div>
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="#dashboard" data-toggle="tab">Dashboard</a></li>
                        <li><a href="#profile" data-toggle="tab">Profile</a></li>
                        <li><a href="Logout" class="logout">Logout <span class="glyphicon glyphicon-log-out"></span></a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container-fluid">
            <div class="row content">
                <div class="col-sm-2 sidenav hidden-xs" style="padding-right: 1px;padding-left: 1px;">
                    <div class="row" style="padding-top: 5px;"><div class="col-md-4 person"></div><div class="col-md-8 username"><%= user.getUser().getName()%><br><small>(<%= user.getUser().getDesignation() %>)</small></div></div>
                    <br>
                    <ul class="nav nav-pills nav-stacked">
                        <li class="active"><a href="#dashboard" data-toggle="tab">Dashboard</a></li>
                        <li><a href="#profile" data-toggle="tab">Profile</a></li>
                        <li><a href="Logout" class="logout">Logout <span class="glyphicon glyphicon-log-out"></span></a></li>
                    </ul>
                    <br>
                </div>
                <br>
                <div class="col-sm-10 tab-content">

                    <div id="dashboard" class="tab-pane fade in active">
                        <div class="dashboard-card">
                            <div class="row">
                                <div class="col-md-6">
                                    <h4>Summary</h4>
                                    <hr>
                                    <%
                                        HashMap<String, String> map = user.summary();
                                    %>
                                    <ul class="list-group" style="height: 185px;">
                                        <li class="list-group-item">Informative Received: <span class="badge"><a data-toggle="modal" data-target="#badgereport" style="color: white;" onclick="showreport('<%= FILE_TYPE.INFORMATIVE.toString() %>')"><%= map.getOrDefault(FILE_TYPE.INFORMATIVE.toString(), "0")%></a></span></li>
                                        <li class="list-group-item">Reply due Received: <span class="badge"><a data-toggle="modal" data-target="#badgereport" style="color: white;" onclick="showreport('<%= FILE_TYPE.REPLY_DUE.toString() %>')"><%= map.getOrDefault(FILE_TYPE.REPLY_DUE.toString(), "0")%></a></span></li>
                                        <li class="list-group-item">Replied: <span class="badge"><a data-toggle="modal" data-target="#badgereport" style="color: white;" onclick="showreport('<%= FILE_TYPE.REPLIED_FILE.toString() %>')"><%= map.getOrDefault(FILE_TYPE.REPLIED_FILE.toString(), "0")%></a></span></li>
                                    </ul>
                                    <div class="modal fade" id='badgereport' role="dialog">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                    <h4 class="modal-title">REPORT<small style="color: white;">(Last 50 are transactions are viewed)</small></h4>
                                                </div>
                                                <div class="modal-body" id="reportdata">

                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <div class="col-md-6">
                                    <h4 class="text-primary">Updates</h4>
                                    <hr>
                                    <div class="marquee">
                                        <%
                                            String msg = user.getUpdateFlashMessage();
                                            if (msg == null) {
                                                msg = "<h3>Welcome to SCR..</h3>";
                                            }
                                            out.println(msg);
                                        %>  
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card">
                            <h4>Files Received.</h4>
                            <hr>
                            <h4>Forwarded</h4>
                            <table class="table table-bordered">
                                <tr>
                                    <th>Date</th>
                                    <th>File Type</th>
                                    <th>File No</th>
                                    <th>Action</th>
                                </tr>
                                <%
                                    li = user.receivedForwarded();
                                    if (li.size() == 0) {
                                %>
                                <tr>
                                    <td colspan="4" class="text-center">No files received.</td>
                                </tr>
                                <%                                    }
                                    for (TransactionsDetails i : li) {
                                        counter += 1;
                                %>
                                <tr>

                                    <td>
                                        <%= i.getSend_date()%>
                                    </td>
                                    <td>
                                        <%= i.getFile_type()%>
                                    </td>
                                    <td>
                                        <%= i.getFile_no()%>
                                    </td>
                                    <td>
                                        <a data-toggle="modal" data-target="#<%= counter%>">View</a>
                                        <div class="modal fade" id='<%= counter%>' role="dialog">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                        <h4 class="modal-title"><%= i.getFile_type()%></h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <form class="form-horizontal" id='<%= i.getFile_type().equals(FILE_TYPE.REPLY_DUE.toString()) ? counter + "inf" : ""%>' >
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Sent By:</label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <%= i.getSender_id()%>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Subject:</label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <%= i.getSubject()%>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Date:</label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <%= i.getSend_date()%>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <%
                                                                if (i.getFile_type().equals(FILE_TYPE.REPLY_DUE.toString())) {
                                                            %>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Target date:</label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <%= i.getTarget_date()%>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <%
                                                                }
                                                            %>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Attachements: </label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <br>
                                                                        <%
                                                                            for (String j : i.getFiles()) {
                                                                        %>
                                                                        <a href="downloadfile?<%= j%>">
                                                                            <%= j.substring(j.lastIndexOf("_") + 1, j.length())%>
                                                                        </a>
                                                                        <br>
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <%
                                                                if (i.getFile_type().equals(FILE_TYPE.REPLY_DUE.toString())) {
                                                            %>
                                                            <div class="form-group">
                                                                <div class="col-sm-offset-2 col-sm-10">
                                                                    <a class="btn btn-default" onclick="showreply(<%= counter%>)">Reply</a>
                                                                </div>
                                                            </div>
                                                            <%
                                                                }
                                                            %>
                                                        </form>
                                                        <%
                                                            if (i.getFile_type().equals(FILE_TYPE.REPLY_DUE.toString())) {
                                                        %>
                                                        <div id="<%= counter%>rep" style="display: none;">
                                                            <form action="processreplyuserforwarded" method="post" class="form-horizontal" enctype="multipart/form-data">

                                                                <input type="hidden" class="form-control" name="rtid" value="<%= i.getTid()%>">
                                                                <input type="hidden" class="form-control" name="to" value="<%= i.getSender_id()%>">


                                                                <div class="form-group">
                                                                    <label class="col-sm-2 control-label">Upload Files:</label>
                                                                    <div class="col-sm-10">
                                                                        <input type="file" name="userfile" multiple required>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-2">Subject:</label>
                                                                    <div class="col-sm-10">
                                                                        <textarea class="form-control" name="subject" required></textarea>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-2">File No:</label>
                                                                    <div class="col-sm-10">
                                                                        <select class="form-control" name="fileno" required>
                                                                            <option>A</option>
                                                                            <option>B</option>
                                                                            <option>C</option>
                                                                            <option>D</option>
                                                                            <option>E</option>
                                                                            <option>F</option>
                                                                            <option>a</option>
                                                                            <option>b</option>
                                                                            <option>c</option>
                                                                            <option>d</option>
                                                                            <option>e</option>
                                                                            <option>f</option>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-offset-2 col-sm-10">
                                                                        <button type="submit" class="btn btn-default">Reply</button>
                                                                    </div>
                                                                </div>
                                                            </form>
                                                        </div>
                                                        <%
                                                            }
                                                        %>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <%
                                    }
                                %>
                            </table>



                            <h4>Recieved</h4>
                            <table class="table table-bordered">
                                <tr>
                                    <th>Date</th>
                                    <th>File Type</th>
                                    <th>File No</th>
                                    <th>Action</th>
                                </tr>
                                <%
                                    li = user.received();
                                    if (li.size() == 0) {
                                %>
                                <tr>
                                    <td colspan="4" class="text-center">No files received.</td>
                                </tr>
                                <%                                    }
                                    for (TransactionsDetails i : li) {
                                        counter += 1;
                                %>
                                <tr>

                                    <td>
                                        <%= i.getSend_date()%>
                                    </td>
                                    <td>
                                        <%= i.getFile_type()%>
                                    </td>
                                    <td>
                                        <%= i.getFile_no()%>
                                    </td>
                                    <td>
                                        <a data-toggle="modal" data-target="#<%= counter%>">View</a>
                                        <div class="modal fade" id='<%= counter%>' role="dialog">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                        <h4 class="modal-title"><%= i.getFile_type()%></h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <form class="form-horizontal" id='<%= i.getFile_type().equals(FILE_TYPE.REPLY_DUE.toString()) ? counter + "inf" : ""%>'>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Sent By:</label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <%= i.getSender_id()%>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Subject:</label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <%= i.getSubject()%>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Date:</label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <%= i.getSend_date()%>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <%
                                                                if (i.getFile_type().equals(FILE_TYPE.REPLY_DUE.toString())) {
                                                            %>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Target date:</label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <%= i.getTarget_date()%>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <%
                                                                }
                                                            %>
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-2">Attachements: </label>
                                                                <div class="col-sm-10">
                                                                    <p class="form-control-static">
                                                                        <br>
                                                                        <%
                                                                            for (String j : i.getFiles()) {
                                                                        %>
                                                                        <a href="downloadfile?<%= j%>">
                                                                            <%= j.substring(j.lastIndexOf("_") + 1, j.length())%>
                                                                        </a>
                                                                        <br>
                                                                        <%
                                                                            }
                                                                        %>
                                                                    </p>
                                                                </div>
                                                            </div>
                                                            <%
                                                                if (i.getFile_type().equals(FILE_TYPE.REPLY_DUE.toString())) {
                                                            %>
                                                            <div class="form-group">
                                                                <div class="col-sm-offset-2 col-sm-10">
                                                                    <a class="btn btn-default" onclick="showreply(<%= counter%>)">Reply</a>
                                                                </div>
                                                            </div>
                                                            <%
                                                                }
                                                            %>
                                                        </form>
                                                        <%
                                                            if (i.getFile_type().equals(FILE_TYPE.REPLY_DUE.toString())) {
                                                        %>
                                                        <div id="<%= counter%>rep" style="display: none;">
                                                            <form action="processreplyuserreceived" method="post" class="form-horizontal" enctype="multipart/form-data">

                                                                <input type="hidden" class="form-control" name="rtid" value="<%= i.getTid()%>">
                                                                <input type="hidden" class="form-control" name="to" value="<%= i.getSender_id()%>">


                                                                <div class="form-group">
                                                                    <label class="col-sm-2 control-label">Upload Files:</label>
                                                                    <div class="col-sm-10">
                                                                        <input type="file" name="userfile" multiple required>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-2">Subject:</label>
                                                                    <div class="col-sm-10">
                                                                        <textarea class="form-control" name="subject" required></textarea>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-2">File No:</label>
                                                                    <div class="col-sm-10">
                                                                        <select class="form-control" name="fileno" required>
                                                                            <option>A</option>
                                                                            <option>B</option>
                                                                            <option>C</option>
                                                                            <option>D</option>
                                                                            <option>E</option>
                                                                            <option>F</option>
                                                                            <option>a</option>
                                                                            <option>b</option>
                                                                            <option>c</option>
                                                                            <option>d</option>
                                                                            <option>e</option>
                                                                            <option>f</option>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-offset-2 col-sm-10">
                                                                        <button type="submit" class="btn btn-default">Reply</button>
                                                                    </div>
                                                                </div>
                                                            </form>
                                                        </div>
                                                        <%
                                                            }
                                                        %>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <%
                                    }
                                %>
                            </table>
                        </div>
                    </div>
                    <div id="profile" class="tab-pane fade">
                        <div class="panel card" style="padding: 0px;">
                            <div class="panel-heading">
                                <h4>User's Details</h4>
                                <hr>
                            </div>
                            <div class="panel-body">
                                <form class="form-horizontal" id="profileUpdate">
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">ID:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= user.getUser().getId()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Name:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= user.getUser().getName()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Designation:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= user.getUser().getDesignation()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Station:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= user.getUser().getStation()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Address:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= user.getUser().getAddress()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Phone:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= user.getUser().getPhone()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Mail:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= user.getUser().getMail()%>
                                            </p>
                                        </div>
                                    </div>
                                </form>
                                <form name="updateprofile" action="updateprofileuser" method="post" class="form-horizontal" style="display:none;">
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Name:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="text" name="name" value="<%= user.getUser().getName()%>" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Designation:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="text" name="designation" value="<%= user.getUser().getDesignation()%>" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Station:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="text" name="station" value="<%= user.getUser().getStation()%>" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Address:</label>
                                        <div class="col-sm-10">
                                            <textarea class="form-control" name="address" required><%= user.getUser().getAddress().trim()%></textarea>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Phone:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="text" name="phone" value="<%= user.getUser().getPhone()%>" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Mail:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="email" name="mail" value="<%= user.getUser().getMail()%>" required>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="panel-footer">
                                <input type="hidden" class="btn btn-success" value="update" id="submitButton" onclick="document.forms.updateprofile.submit()">
                                <button class="btn btn-success" type="button"  onclick="editProfile(this)">Edit Profile</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%
            if (session.getAttribute("message") != null) {
                out.println("<div class='alert alert-success alert-dismissable alert_1'><a href='#' class='close close-alerts' data-dismiss='alert' aria-label='close'>&times;</a><strong>" + session.getAttribute("message") + "</strong></div");
                session.removeAttribute("message");
            }
            if (!user.getMessage().equals("")) {
                out.println("<div class='alert alert-success alert-dismissable alert_2'><a href='#' class='close close-alerts' data-dismiss='alert' aria-label='close'>&times;</a><strong>" + user.getMessage() + "</strong></div>");
                user.setMessage("");
            }
        %>
        <script src="static/js/jquery-3.2.1.js"></script>
        <script src="static/js/bootstrap.min.js"></script>
        <script src="static/js/jquery.marquee.js"></script>
        <script src="static/js/custom.js"></script>
    </body>
</html>
<%
    } catch (Exception e) {
        throw new ServletException(e.getLocalizedMessage());
    }
%>