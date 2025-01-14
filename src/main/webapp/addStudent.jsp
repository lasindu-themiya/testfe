<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <title>Add Student</title>
</head>
<body>
<form action="/frontend_war/student/addStudent" method="post">
  <label for="batchId">Select Batch:</label>
  <select id="batchId" name="batchId" required>
    <option value="">-- Select Batch --</option>
    <%
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

  <label for="name">Name:</label>
  <input type="text" id="name" name="name" required><br>

  <label for="email">Email:</label>
  <input type="email" id="email" name="email" required><br>

  <label for="password">Password:</label>
  <input type="password" id="password" name="password" required><br>

  <label for="year">Year:</label>
  <input type="number" id="year" name="year" required><br>

  <label for="contact">Contact:</label>
  <input type="text" id="contact" name="contact" required><br>

  <label for="photo">Photo URL:</label>
  <input type="text" id="photo" name="photo"><br>

  <button type="submit">Add Student</button>
</form>
</body>
</html>
