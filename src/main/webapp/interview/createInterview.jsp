<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page import="com.example.frontend.dto.Student" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Interview</title>
    <script>
        window.onload = function() {
            var batchListSize = <%= (request.getAttribute("batchList") == null) ? 0
                                     : ((List)request.getAttribute("batchList")).size() %>;
            if (batchListSize === 0) {
                window.location.href = '<%= request.getContextPath() %>/InterviewServlet?action=getBatches';
            }
        };

        function loadStudentsForBatch() {
            const batchId = document.getElementById('batchId').value;
            if (batchId) {
                window.location.href = '<%= request.getContextPath() %>/InterviewServlet?action=loadStudents&batchId=' + batchId;
            }
        }
    </script>
</head>
<body>
<h1>Create Interview</h1>

<%
    List<Batch> batchList = (List<Batch>) request.getAttribute("batchList");
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");
%>

<% if (batchList != null && !batchList.isEmpty()) { %>
<form method="POST" action="<%= request.getContextPath() %>/InterviewServlet?action=create">
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

    <label>Company Name:</label>
    <input type="text" name="companyName"/><br/><br/>

    <label>Position:</label>
    <input type="text" name="position"/><br/><br/>

    <label>Mode (Online/Offline):</label>
    <input type="text" name="mode"/><br/><br/>

    <input type="submit" value="Create Interview"/>
</form>
<% } else { %>
<p>Loading batches...</p>
<% } %>

<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
