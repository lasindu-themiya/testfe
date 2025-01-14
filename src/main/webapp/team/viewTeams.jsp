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
<table>
    <thead>
    <tr>
        <th>Team ID</th>
        <th>Leader</th>
        <th>Members</th>
        <th>Lecturer</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${teams}" var="team">
        <tr>
            <td>${team.id}</td>
            <td>${team.leader}</td>
            <td>${team.members}</td>
            <td>${team.lecturer}</td>
            <td>
                <a href="TeamServlet?action=update&teamId=${team.id}">Edit</a>
                <a href="TeamServlet?action=delete&teamId=${team.id}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>


</body>
</html>
