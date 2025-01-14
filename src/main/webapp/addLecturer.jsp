<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 1/8/2025
  Time: 12:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/frontend_war/lecturer/addLecturer" method="post">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br>

    <label for="department">Department:</label>
    <input type="text" id="department" name="department" required><br>

    <label for="contact">Contact:</label>
    <input type="text" id="contact" name="contact" required><br>

    <label for="courseAssign">Course Assign:</label>
    <input type="text" id="courseAssign" name="courseAssign" required><br>

    <button type="submit">Add Lecturer</button>
</form>

</body>
</html>
