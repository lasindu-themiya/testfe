<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Workshops</title>
</head>
<body>
<h1>Workshops</h1>
<c:if test="${not empty workshops}">
    <ul>
        <c:forEach var="workshop" items="${workshops}">
            <li>type:${workshop.type} contact: ${workshop.contact} date: ${workshop.date}</li>
        </c:forEach>
    </ul>
</c:if>
<c:if test="${empty workshops}">
    <p>No workshops available for the logged-in student.</p>
</c:if>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<a href="studentDashboard.jsp">Back to Dashboard</a>
</body>
</html>
