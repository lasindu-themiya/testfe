<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page import="com.example.frontend.dto.Student" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Feedback</title>
    <script>
        window.onload = function() {
            var batchListSize = <%= (request.getAttribute("batchList") == null) ? 0
                                     : ((List)request.getAttribute("batchList")).size() %>;
            if (batchListSize === 0) {
                window.location.href = '<%= request.getContextPath() %>/FeedbackServlet?action=getBatches';
            }
        };

        function loadStudentsForBatch() {
            const batchId = document.getElementById('batchId').value;
            if (batchId) {
                window.location.href = '<%= request.getContextPath() %>/FeedbackServlet?action=loadStudents&batchId=' + batchId;
            }
        }
    </script>
</head>
<body>
<h1>Add Feedback</h1>

<%
    List<Batch> batchList = (List<Batch>) request.getAttribute("batchList");
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");
%>

<% if (batchList != null && !batchList.isEmpty()) { %>
<form method="POST" action="<%= request.getContextPath() %>/FeedbackServlet?action=add">
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

    <label>Assign Feedback to Individual Student? (leave blank for batchwise)</label>
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

    <label>Feedback Content:</label><br/>
    <textarea name="content" rows="5" cols="30"></textarea>
    <br/><br/>

    <label>Points:</label>
    <input type="number" name="points"/>
    <br/><br/>

    <input type="submit" value="Add Feedback"/>
</form>
<% } else { %>
<p>Loading batches...</p>
<% } %>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
