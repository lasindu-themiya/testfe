<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page import="com.example.frontend.dto.Student" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Announcement</title>
    <script>
        // If batchList is empty, redirect to the servlet to load them
        window.onload = function() {
            var batchListSize = <%= (request.getAttribute("batchList") == null) ? 0
                                     : ((List)request.getAttribute("batchList")).size() %>;
            if (batchListSize === 0) {
                window.location.href = '<%= request.getContextPath() %>/AnnouncementServlet?action=getBatches';
            }
        };

        function loadStudentsForBatch() {
            const batchId = document.getElementById('batchId').value;
            if (batchId) {
                window.location.href = '<%= request.getContextPath() %>/AnnouncementServlet?action=loadStudents&batchId=' + batchId;
            }
        }
    </script>
</head>
<body>
<h1>Create Announcement</h1>

<!-- We assume the servlet sets "batchList" and optionally "studentList" -->
<%
    List<Batch> batchList = (List<Batch>) request.getAttribute("batchList");
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");
%>

<!-- Only render the form if batchList is present and non-empty -->
<% if (batchList != null && !batchList.isEmpty()) { %>
<form method="POST" action="AnnouncementServlet?action=create">
    <label>Select Batch:</label>
    <select id="batchId" name="batchId" onchange="loadStudentsForBatch()">
        <option value="">-- Select Batch --</option>
        <%
            String currentBatchId = request.getParameter("batchId");
            for (Batch b : batchList) {
        %>
        <option value="<%= b.getId() %>"
                <%= (b.getId().equals(currentBatchId)) ? "selected" : "" %>>
            <%= b.getName() %>
        </option>
        <%
            }
        %>
    </select>
    <br/><br/>

    <label>Assign to Individual Student? (leave blank to assign batchwise)</label>
    <select id="studentId" name="studentId">
        <option value="">-- Optional: Select a Student --</option>
        <%
            if (studentList != null) {
                for (Student s : studentList) {
        %>
        <option value="<%= s.getId() %>"><%= s.getName() %></option>
        <%
                }
            }
        %>
    </select>
    <br/><br/>

    <label>Announcement Content:</label><br/>
    <textarea name="content" rows="5" cols="30"></textarea>
    <br/><br/>

    <input type="submit" value="Create Announcement"/>
</form>
<% } else { %>
<!-- If batchList is empty, we rely on window.onload to redirect. -->
<p>Loading batches...</p>
<% } %>

<!-- Show any success/error messages -->
<c:if test="${not empty message}">
    <p style="color: green;">${message}</p>
</c:if>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>


</body>
</html>
