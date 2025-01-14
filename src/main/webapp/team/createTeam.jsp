<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Team</title>
</head>
<body>
<form action="<%= request.getContextPath() %>/TeamServlet" method="POST">
    <input type="hidden" name="action" value="create">

    <!-- Members Section -->
    <label for="members">Members:</label>
    <select name="members" multiple>
        <c:forEach var="student" items="${students}">
            <option value="${student}">${student}</option>
        </c:forEach>
    </select>

    <!-- Lecturer Section -->
    <label for="lecturer">Lecturer:</label>
    <select name="lecturer">
        <c:forEach var="lecturer" items="${lecturers}">
            <option value="${lecturer}">${lecturer}</option>
        </c:forEach>
    </select>

    <button type="submit">Add Team</button>
</form>
</body>
</html>
