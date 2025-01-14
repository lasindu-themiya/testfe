<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 1/14/2025
  Time: 3:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="<%= request.getContextPath() %>/TeamServlet" method="POST">
    <input type="hidden" name="action" value="deleteTeam">
    <input type="hidden" name="teamId" value="${team.id}">
    <button type="submit">Delete Team</button>
</form>

</body>
</html>
