<%@page import="java.util.Date"%>
<%@page import="java.util.HashMap"%>
<%@page import="scr.TransactionsDetails"%>
<%@page import="scr.FILE_TYPE"%>
<%@page import="scr.TYPE"%>
<%@page import="scr.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="scr.half_admin.Details"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    ArrayList<User> list;
    Details half_admin;
    ArrayList<TransactionsDetails> li;
    int counter = 1;
    String f = "";
%>
<%
    try {
        if (session.getAttribute("ADMIN") == null) {
            half_admin = new Details((String) session.getAttribute("ID"), (TYPE) session.getAttribute("TYPE"));
        } else {
            half_admin = (Details) session.getAttribute("ADMIN");
        }
        half_admin.load_details();
        session.setAttribute("ADMIN", half_admin);
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
                        <li><a href="#replies" data-toggle="tab">Replies</a></li>
                        <li><a href="#createuser" data-toggle="tab">Create User</a></li>
                        <li><a href="#deleteuser" data-toggle="tab">Delete User</a></li>
                        <li><a href="#sendfile" data-toggle="tab">Send File</a></li>
                        <li><a href="#forward" data-toggle="tab">Forward Files</a></li>
                        <!-- <li><a href="#reports" data-toggle="tab">Reports</a></li> -->
                        <li><a href="#profile" data-toggle="tab">Profile</a></li>
                        <li><a href="Logout" class="logout">Logout <span class="glyphicon glyphicon-log-out"></span></a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container-fluid">
            <div class="row content">
                <div class="col-sm-2 sidenav hidden-xs" style="padding-right: 1px;padding-left: 1px;">
                    <div class="row" style="padding-top: 5px;"><div class="col-md-4 person" ></div><div class="col-md-8 username"><%= half_admin.getUser().getName()%><br><small>(<%= half_admin.getUser().getDesignation()%>)</small></div></div>
                    <br>
                    <ul class="nav nav-pills nav-stacked">
                        <li class="active"><a href="#dashboard" data-toggle="tab">Dashboard</a></li>
                        <li><a href="#replies" data-toggle="tab">Replies</a></li>
                        <li><a href="#createuser" data-toggle="tab">Create User</a></li>
                        <li><a href="#deleteuser" data-toggle="tab">Delete User</a></li>
                        <li><a href="#sendfile" data-toggle="tab">Send File</a></li>
                        <li><a href="#forward" data-toggle="tab">Forward Files</a></li>
                      <!--  <li><a href="#reports" data-toggle="tab">Reports</a></li> -->
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
                                        HashMap<String, String> map = half_admin.summary();
                                    %>
                                    <ul class="list-group" style="height: 185px;">
                                        <li class="list-group-item">Informative Files Received: <span class="badge"><a data-toggle="modal" data-target="#badgereport" style="color: white;" onclick="showreporthalfadmin('<%= FILE_TYPE.INFORMATIVE.toString()%>')"><%= map.getOrDefault(FILE_TYPE.INFORMATIVE.toString(), "0")%></a></span></li>
                                        <li class="list-group-item">Reply due Files Received: <span class="badge"><a data-toggle="modal" data-target="#badgereport" style="color: white;" onclick="showreporthalfadmin('<%= FILE_TYPE.REPLY_DUE.toString()%>')"><%= map.getOrDefault(FILE_TYPE.REPLY_DUE.toString(), "0")%></a></span></li>
                                        <li class="list-group-item">Replied: <span class="badge"><a data-toggle="modal" data-target="#badgereport" style="color: white;" onclick="showreporthalfadmin('<%= FILE_TYPE.REPLIED_FILE.toString()%>')"><%= map.getOrDefault(FILE_TYPE.REPLIED_FILE.toString(), "0")%></a></span></li>
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
                                            String msg = half_admin.getUpdateFlashMessage();
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
                            <table class="table table-bordered">
                                <tr>
                                    <th>Date</th>
                                    <th>File Type</th>
                                    <th>File No</th>
                                    <th>Action</th>
                                </tr>
                                <%
                                    li = half_admin.received();
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
                                                            <form action="processreplyhalf" method="post" class="form-horizontal" enctype="multipart/form-data">

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
                                                                        <p class="form-control-static"><%= i.getSubject()%></p>
                                                                        <textarea class="form-control" name="subject" style="display: none;"><%= i.getSubject()%></textarea>
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
                    <div class="tab-pane fade" id="replies">
                        <div class="card">
                            <h4>Replies for forwarded files</h4>
                            <table class="table table-bordered">
                                <tr>
                                    <th>From</th>
                                    <th>Date</th>
                                    <th>File Type</th>
                                    <th>File No</th>
                                    <th>Action</th>
                                </tr>
                                <%
                                    li = half_admin.repliesForForwarded();
                                    if (li.size() == 0) {
                                %>
                                <tr>
                                    <td colspan="5" class="text-center">No files received.</td>
                                </tr>
                                <%                                    }
                                    for (TransactionsDetails i : li) {
                                        counter += 1;
                                %>
                                <tr>
                                    <td>
                                        <%= i.getSender_id()%>
                                    </td>
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
                                        <a data-toggle="modal" data-target="#<%=  counter%>">View</a>
                                        <div class="modal fade" id='<%=  counter%>' role="dialog">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                        <h4 class="modal-title"><%= i.getFile_type()%></h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <form class="form-horizontal" id='<%= counter + "inf"%>' >
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
                                                            <div class="form-group">
                                                                <div class="col-sm-offset-2 col-sm-10">
                                                                    <a class="btn btn-default" onclick="showreply(<%= counter%>)">Reply</a>
                                                                </div>
                                                            </div>
                                                        </form>
                                                        <div id="<%= counter%>rep" style="display: none;">
                                                            <form action="sendreplytoadmin" method="post" class="form-horizontal" enctype="multipart/form-data">
                                                                <input type="hidden" class="form-control" name="rtid" value="<%= i.getTid()%>">
                                                                <input type="hidden" class="form-control" name="to" value="<%= half_admin.getUser().getHigherofficial()%>">
                                                                <div class="form-group">
                                                                    <label class="col-sm-2 control-label">Upload Files:</label>
                                                                    <div class="col-sm-10">
                                                                        <input type="file" name="userfile" multiple>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-sm-2 control-label">Include User Sent Files:</label>
                                                                    <div class="col-sm-10">
                                                                        <%
                                                                            f = "";
                                                                            for (String j : i.getFiles()) {
                                                                                f = j + "~" + f;
                                                                            }
                                                                        %>
                                                                        <label style="font-weight: normal;"><input type="checkbox" name="includefiles" value="<%= f%>"> Include</label>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label class="col-sm-2 control-label">Reply Type:</label>
                                                                    <div class="col-sm-10">
                                                                        <select class="form-control" name="type" required>
                                                                            <option>REPLIED</option>
                                                                            <option>PARTIALLY REPLIED</option>
                                                                        </select>
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
                        <div class="card">
                            <h4>Replies for files sent by you</h4>
                            <table class="table table-bordered">
                                <tr>
                                    <th>Date</th>
                                    <th>File Type</th>
                                    <th>File No</th>
                                    <th>Action</th>
                                </tr>
                                <%
                                    li = half_admin.repliesForReceived();
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
                                                        <form class="form-horizontal">
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
                                                        </form>
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
                    <div id="forward" class="tab-pane fade">
                        <div class="card">
                            <form action="forwardfiles" method="post">
                                <h4>Forward</h4>
                                <hr>


                                <%
                                    li = half_admin.received();
                                    if (li.size() == 0) {
                                        out.println("<div class='text-center'>No Files To Forward.</div>");
                                    }
                                    for (TransactionsDetails i : li) {
                                %>
                                <div class="panel panel-default">

                                    <div class="panel-heading"><div class="panel-title"><a data-toggle="collapse" class="text-lowercase" href="#<%= i.getTid()%>"><%= i.getSend_date() + " ~ " + i.getFile_type()%></a></div></div>
                                    <div id="<%= i.getTid()%>" class="panel-collapse collapse">
                                        <div class="panel-body">
                                            <table class="table table-bordered table-condensed">
                                                <tr>
                                                    <th>File Name</th>
                                                    <th>Date</th>
                                                    <th>File Type</th>
                                                    <th>File No</th>
                                                </tr>    
                                                <%
                                                    for (String j : i.getFiles()) {
                                                %>
                                                <tr>
                                                    <td>
                                                        <%
                                                            if (i.getStatus().equals("REPLIED")) {
                                                                out.println("&nbsp;&nbsp;&nbsp;&nbsp;" + j.substring(j.lastIndexOf("_") + 1, j.length()));

                                                            } else {
                                                        %>
                                                        <div class="checkbox">
                                                            <label><input type="checkbox" name="selectedfiles" onclick="colorChange2(this)" value="<%= j%>_<%= i.getTid()%>"><%= j.substring(j.lastIndexOf("_") + 1, j.length())%></label>
                                                        </div>
                                                        <%
                                                            }
                                                        %>
                                                    </td>
                                                    <td><%= i.getSend_date()%></td>
                                                    <td><%= i.getFile_type()%></td>
                                                    <td><%= i.getFile_no()%> <%= i.getStatus().equals("REPLIED") ? "<span class='badge text-lowercase'>" + i.getStatus() + "</span>" : ""%></td>
                                                </tr>
                                                <%
                                                    }
                                                %>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <%
                                    }
                                %>

                                <br>
                                <h4>Select users</h4>
                                <table class="table table-bordered table-condensed">
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Designation</th>
                                        <th>Station</th>
                                    </tr>
                                    <%
                                        list = half_admin.usersCreated();
                                        if (list.size() == 0) {
                                            out.println("<tr class='text-center'><td colspan='4'>No User Exists</td></tr>");
                                        }
                                        if (list != null) {
                                            for (User obj : list) {
                                    %>
                                    <tr>
                                        <td>
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="users" onclick="colorChange2(this)" value="<%= obj.getId()%>"><%= obj.getId()%></label>
                                            </div>
                                        </td>
                                        <td><%= obj.getName()%></td>
                                        <td><%= obj.getDesignation()%></td>
                                        <td><%= obj.getStation()%></td>
                                    </tr>
                                    <%
                                            }
                                        }
                                    %>
                                </table>
                                <div class="form-group">
                                    <div class="text-right">
                                        <button type="submit" class="btn btn-primary">Forward</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div id="sendfile" class="tab-pane fade">
                        <div class="card">
                            <form class="form-horizontal" action="sendfilehalf" method="post" enctype="multipart/form-data">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">Upload Files:</label>
                                    <div class="col-sm-10">
                                        <input  type="file" name="userfile" multiple required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">File Type:</label>
                                    <div class="col-sm-10">
                                        <label class="radio-inline" onclick="diabledate()"><input type="radio" name="filetype" value="INFORMATIVE" checked>INFORMATIVE</label>
                                        <label class="radio-inline" onclick="makeUse()"><input type="radio" name="filetype" value="REPLY_DUE">REPLY DUE</label>
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
                                    <label class="control-label col-sm-2">Subject:</label>
                                    <div class="col-sm-10">
                                        <textarea class="form-control" name="subject" required></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">Target Date:</label>
                                    <div class="col-sm-10">
                                        <input class="form-control" id="targetdate" type="date" name="targetdate" disabled="disabled" value="<%= new java.sql.Date(new Date().getTime())%>" required>
                                    </div>
                                </div>
                                <br>
                                <div>
                                    <h4>Select users to send:</h4>
                                    <table class="table table-bordered">
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Designation</th>
                                            <th>Station</th>
                                        </tr>
                                        <%
                                            list = half_admin.usersCreated();
                                            if (list.size() == 0) {
                                                out.println("<tr class='text-center'><td colspan='4'>No User Exists</td></tr>");
                                            }
                                            if (list != null) {
                                                for (User obj : list) {
                                        %>
                                        <tr>
                                            <td>
                                                <div class="checkbox">
                                                    <label><input type="checkbox" name="users" onclick="colorChange2(this)" value="<%= obj.getId()%>"><%= obj.getId()%></label>
                                                </div>
                                            </td>
                                            <td><%= obj.getName()%></td>
                                            <td><%= obj.getDesignation()%></td>
                                            <td><%= obj.getStation()%></td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        %>
                                    </table>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10 text-right">
                                        <button type="submit" class="btn btn-danger">send file</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div id="createuser" class="tab-pane fade">
                        <div class="card">
                            <form class="form-horizontal" action="createuserhalf" method="post">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">ID:</label>
                                    <div class="col-sm-10">
                                        <input class="form-control" type="text" name="id" onblur="checkforuser(this)" required>
                                        <small class="text-danger" id="checkuser" style="display: none;"> A User already exists with same ID.</small>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Name:</label>
                                    <div class="col-sm-10">
                                        <input class="form-control" type="text" name="name" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Designation:</label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="designation">
                                            <option>Dy.CME/WS</option>
                                            <option>Dy.CME/DSL</option>
                                            <option>EME/PLG</option>
                                            <option>DY.CME/C&amp;W</option>
                                            <option>Dy.CME/EnHM</option>

                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Station:</label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="station">
                                            <option>SECUNDRABAD</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Address:</label>
                                    <div class="col-sm-10">
                                        <textarea class="form-control" name="address" required></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Phone:</label>
                                    <div class="col-sm-10">
                                        <input class="form-control" type="tel" name="phone" pattern="(7|8|9)\d{9}" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Mail:</label>
                                    <div class="col-sm-10">
                                        <input class="form-control" type="email" name="mail" required>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-2 control-label">Password:</label>
                                    <div class="col-sm-10">
                                        <input class="form-control" type="text" name="password" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <button type="submit" class="btn btn-default">Submit</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div id="deleteuser" class="tab-pane fade">
                        <div class="card">
                            <form class="form-horizontal" action="deleteuserhalf" method="post">
                                <div>
                                    <h4>Users created under you.</h4>
                                    <table class="table table-bordered">
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Designation</th>
                                            <th>Station</th>
                                        </tr>
                                        <%
                                            list = half_admin.usersCreated();
                                            if (list.size() == 0) {
                                                out.println("<tr class='text-center'><td colspan='4'>No User Exists</td></tr>");
                                            }
                                            if (list != null) {
                                                for (User obj : list) {
                                        %>
                                        <tr>
                                            <td>
                                                <div class="checkbox">
                                                    <label>
                                                        <input type="checkbox" name="users" onclick="colorChange(this)" value="<%= obj.getId()%>">
                                                        <%= obj.getId()%>
                                                    </label>
                                                </div>
                                            </td>
                                            <td>
                                                <%= obj.getName()%>
                                            </td>
                                            <td>
                                                <%= obj.getDesignation()%>
                                            </td>
                                            <td>
                                                <%= obj.getStation()%>
                                            </td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        %>
                                    </table>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10 text-right">
                                        <button type="submit" class="btn btn-danger">Delete Selected</button>
                                    </div>
                                </div>
                            </form>
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
                                                <%= half_admin.getUser().getId()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Name:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= half_admin.getUser().getName()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Designation:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= half_admin.getUser().getDesignation()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Station:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= half_admin.getUser().getStation()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Address:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= half_admin.getUser().getAddress()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Phone:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= half_admin.getUser().getPhone()%>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Mail:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-static">
                                                <%= half_admin.getUser().getMail()%>
                                            </p>
                                        </div>
                                    </div>
                                </form>
                                <form name="updateprofile" action="updateprofilehalf" method="post" class="form-horizontal" style="display:none;">
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Name:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="text" name="name" value="<%= half_admin.getUser().getName()%>" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Designation:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="text" name="designation" value="<%= half_admin.getUser().getDesignation()%>" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Station:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="text" name="station" value="<%= half_admin.getUser().getStation()%>" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Address:</label>
                                        <div class="col-sm-10">
                                            <textarea class="form-control" name="address" required><%= half_admin.getUser().getAddress().trim()%></textarea>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Phone:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="text" name="phone" value="<%= half_admin.getUser().getPhone()%>" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">Mail:</label>
                                        <div class="col-sm-10">
                                            <input class="form-control" type="email" name="mail" value="<%= half_admin.getUser().getMail()%>" required>
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
            if (!half_admin.getMessage().equals("")) {
                out.println("<div class='alert alert-success alert-dismissable alert_2'><a href='#' class='close close-alerts' data-dismiss='alert' aria-label='close'>&times;</a><strong>" + half_admin.getMessage() + "</strong></div>");
                half_admin.setMessage("");
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