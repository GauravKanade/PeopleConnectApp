<%-- 
    Document   : DisplayComplaint
    Created on : 18 Mar, 2015, 11:23:57 PM
    Author     : Gaurav Kanade
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <% String id = "", title = "", description = "", image = "", category = "";
            id = request.getParameter("id");
            description = request.getParameter("description");
            image = request.getParameter("image");
            category = request.getParameter("category");
            title = request.getParameter("title");

        %>

    </head>
    <body>
        <h1>Hello World!</h1>
        <h2>Title: </h2>&nbsp;<%=title%><br/>
        <h2>Description: </h2>&nbsp;<%=description%><br/>
        <h2>Category: </h2>&nbsp;<%=category%><br/>
        <h2>Image: </h2>&nbsp;<%=image%><br/>
    </body>
</html>
