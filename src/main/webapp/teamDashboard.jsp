<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 1/1/2025
  Time: 11:11 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Welcome to the Team Dashboard!</h2>
<a href="<%= request.getContextPath() %>/TeamServlet?action=update">update</a><br>
<a href="<%= request.getContextPath() %>/TeamServlet?action=delete">delete</a><br>

</body>
</html>
