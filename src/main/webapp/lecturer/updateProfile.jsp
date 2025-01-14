<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Lecturer Profile</title>
</head>
<body>
<h1>Update Lecturer Profile</h1>
<form method="POST" action="<%= request.getContextPath() %>/LecturerProfileServlet?action=updateProfile">
    <label>Name:</label>
    <input type="text" name="name" value="${lecturer.name}" /><br/>

    <label>Email:</label>
    <input type="text" name="email" value="${lecturer.email}" /><br/>

    <label>Department:</label>
    <input type="text" name="department" value="${lecturer.department}" /><br/>

    <label>Contact:</label>
    <input type="text" name="contact" value="${lecturer.contact}" /><br/>

    <label>Course Assigned:</label>
    <input type="text" name="courseAssign" value="${lecturer.courseAssign}" /><br/>

    <input type="submit" value="Update Profile"/>
</form>

<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
