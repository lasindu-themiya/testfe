<%@ page import="com.example.frontend.dto.Lecturer" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 1/8/2025
  Time: 12:29 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/frontend_war/lecturer/deleteLecturer" method="post">
    <label for="lecturerId">Select Lecturer:</label>
    <select id="lecturerId" name="id" required>
        <option value="">-- Select Lecturer --</option>
        <%
            // Fetch the list of lecturers from the request attribute
            List<Lecturer> lecturers = (List<Lecturer>) request.getAttribute("lecturers");
            if (lecturers != null) {
                for (Lecturer lecturer : lecturers) {
        %>
        <option value="<%= lecturer.getId() %>"><%= lecturer.getName() %></option>
        <%
                }
            }
        %>
    </select><br>

    <button type="submit">Delete Lecturer</button>
</form>


</body>
</html>
