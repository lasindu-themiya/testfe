<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Edit Announcement</title>
</head>
<body>
<h1>Edit Announcement</h1>

<!--
    "ann" is presumably set by your servlet as request.setAttribute("ann", theAnnouncementDTO).
    This page will POST back to the AnnouncementServlet with action=update.
-->

<form method="POST" action="${pageContext.request.contextPath}/AnnouncementServlet">
    <input type="hidden" name="action" value="update" />
    <!-- The backend wants the event ID, so let's store ann.event.id in hidden field: -->
    <input type="hidden" name="announcementId" value="${ann.eventId}" />

    <label>Content:</label><br/>
    <textarea name="content" rows="4" cols="50">${ann.content}</textarea><br/><br/>

    <input type="submit" value="Update Announcement" />
</form>
<p><a href="${pageContext.request.contextPath}/AnnouncementServlet?action=list">Back to Announcements</a></p>


<p><a href="${pageContext.request.contextPath}/AnnouncementServlet?action=list">Back to Announcements</a></p>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
