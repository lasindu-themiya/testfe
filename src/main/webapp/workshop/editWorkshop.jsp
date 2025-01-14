<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Workshop</title>
</head>
<body>
<h1>Edit Workshop</h1>
<form method="POST" action="<%= request.getContextPath() %>/WorkshopServlet?action=update">
    <input type="hidden" name="workshopId" value="${wk.eventId}" />

    <label>Type:</label>
    <input type="text" name="type" value="${wk.type}"/><br/>

    <label>Contact:</label>
    <input type="text" name="contact" value="${wk.contact}"/><br/>

    <input type="submit" value="Update Workshop"/>
</form>
<p><a href="listWorkshops.jsp">Back to Workshops</a></p>
</body>
</html>
