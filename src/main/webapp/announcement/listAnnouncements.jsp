<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>All Announcements</title>
</head>
<body>
<h1>All Announcements</h1>

<c:if test="${not empty message}">
    <p style="color: green;">${message}</p>
</c:if>

<table border="1">
    <tr>
        <th>Event ID</th>
        <th>Name</th>
        <th>Type</th>
        <th>Content</th>
        <th>Date</th>
        <th>Time</th>
        <th>Status</th>
        <th>Student Id</th>
        <th>Batch Id</th>
        <th>Actions</th>

    </tr>
    <c:forEach var="ann" items="${announcementList}">
        <tr>
            <td>${ann.eventId}</td>
            <td>${ann.name}</td>
            <td>${ann.type}</td>
            <td>${ann.content}</td>
            <td>${ann.date}</td>
            <td>${ann.time}</td>
            <td>${ann.status}</td>
            <td>${ann.studentId}</td>
            <td>${ann.batchId}</td>

            <td>
                <a href="${pageContext.request.contextPath}/AnnouncementServlet?action=edit&id=${ann.eventId}">
                    Edit
                </a> |
                <a href="${pageContext.request.contextPath}/AnnouncementServlet?action=delete&id=${ann.aId}"
                   onclick="return confirm('Are you sure?');">
                    Delete
                </a>

            </td>
        </tr>
    </c:forEach>
</table>

<p><a href="${pageContext.request.contextPath}/announcement/createAnnouncement.jsp">Create New Announcement</a></p>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
