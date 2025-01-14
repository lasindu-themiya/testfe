<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 1/14/2025
  Time: 3:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="TeamServlet" method="POST">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="teamId" value="${team.id}">
    <label for="members">Members:</label>
    <select name="members" multiple>
        <c:forEach items="${students}" var="student">
            <option value="${student.id}" <c:if test="${fn:contains(team.members, student.id)}">selected</c:if>>
                    ${student.name}
            </option>
        </c:forEach>
    </select>
    <label for="lecturer">Lecturer:</label>
    <select name="lecturer">
        <c:forEach items="${lecturers}" var="lecturer">
            <option value="${lecturer.id}" <c:if test="${lecturer.id == team.lecturer}">selected</c:if>>
                    ${lecturer.name}
            </option>
        </c:forEach>
    </select>
    <button type="submit">Update Team</button>
</form>


</body>
</html>
