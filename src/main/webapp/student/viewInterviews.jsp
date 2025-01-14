<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Interviews</title>
</head>
<body>
<h1>Interviews</h1>
<c:if test="${not empty interviews}">
    <ul>
        <c:forEach var="interview" items="${interviews}">
            <li>${interview.position} at ${interview.companyName} (Date: ${interview.date})</li>
        </c:forEach>
    </ul>
</c:if>
<c:if test="${empty interviews}">
    <p>No interviews available for the logged-in student.</p>
</c:if>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<a href="studentDashboard.jsp">Back to Dashboard</a>
</body>
</html>
