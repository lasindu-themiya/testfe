<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Announcements</title>
</head>
<body>
<h1>Announcements</h1>
<c:if test="${not empty announcements}">
    <ul>
        <c:forEach var="announcement" items="${announcements}">
            <li>${announcement.content} (Date: ${announcement.date})</li>
        </c:forEach>
    </ul>
</c:if>
<c:if test="${empty announcements}">
    <p>No announcements available for the logged-in student.</p>
</c:if>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<a href="studentDashboard.jsp">Back to Dashboard</a>
</body>
</html>
