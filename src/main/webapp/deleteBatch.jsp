<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Delete Batch</title>
</head>
<body>
<form action="/frontend_war/batch/deleteBatch" method="post">
    <label for="batchId">Select Batch:</label>
    <select id="batchId" name="id" required>
        <option value="">-- Select Batch --</option>
        <%
            // Fetch the list of batches from the request attribute
            List<Batch> batches = (List<Batch>) request.getAttribute("batches");
            if (batches != null) {
                for (Batch batch : batches) {
        %>
        <option value="<%= batch.getId() %>"><%= batch.getName() %></option>
        <%
                }
            }
        %>
    </select><br>

    <button type="submit">Delete Batch</button>
</form>
</body>
</html>
