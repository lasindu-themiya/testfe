<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>All Workshops</title>
</head>
<body>
<h1>All Workshops</h1>

<c:if test="${not empty message}">
    <p style="color: green;">${message}</p>
</c:if>

<table border="1">
    <tr>
        <th>Event ID</th>
        <th>Name</th>
        <th>Type</th>
        <th>Contact</th>
        <th>Date</th>
        <th>Time</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="wk" items="${workshopList}">
        <tr>
            <td>${wk.eventId}</td>
            <td>${wk.name}</td>
            <td>${wk.type}</td>
            <td>${wk.contact}</td>
            <td>${wk.date}</td>
            <td>${wk.time}</td>
            <td>${wk.status}</td>
            <td>
                <a href="${pageContext.request.contextPath}/WorkshopServlet?action=edit&id=${wk.eventId}">
                    Edit
                </a> |
                <a href="${pageContext.request.contextPath}/WorkshopServlet?action=delete&id=${wk.wId}"
                   onclick="return confirm('Are you sure?');">
                    Delete
                </a>
            </td>
        </tr>
    </c:forEach>
</table>

<p><a href="createWorkshop.jsp">Create New Workshop</a></p>
</body>
</html>
