<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page import="com.example.frontend.dto.Student" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <title>Delete Student</title>
  <script>
    const students = <%= request.getAttribute("students") != null
        ? new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request.getAttribute("students"))
        : "[]" %>;

    function fetchStudentsByBatch() {
      const batchId = document.getElementById("batchId").value;
      if (batchId) {
        window.location.href = "/frontend_war/student/deleteStudent?batchId=" + batchId;
      }
    }

    function populateStudentDropdown() {
      const studentDropdown = document.getElementById('studentId');
      studentDropdown.innerHTML = '<option value="">-- Select Student --</option>';

      students.forEach(student => {
        const option = document.createElement('option');
        option.value = student.id;
        option.textContent = student.id;
        studentDropdown.appendChild(option);
      });
    }

    window.onload = function () {
      populateStudentDropdown();
    };
  </script>
</head>
<body>
<form action="/frontend_war/student/deleteStudent" method="post">
  <label for="batchId">Select Batch:</label>
  <select id="batchId" name="batchId" onchange="fetchStudentsByBatch()" required>
    <option value="">-- Select Batch --</option>
    <%
      List<Batch> batches = (List<Batch>) request.getAttribute("batches");
      if (batches != null) {
        for (Batch batch : batches) {
    %>
    <option value="<%= batch.getId() %>"
            <%= batch.getId().equals(request.getParameter("batchId")) ? "selected" : "" %>>
      <%= batch.getName() %>
    </option>
    <%
        }
      }
    %>
  </select><br>

  <label for="studentId">Select Student:</label>
  <select id="studentId" name="id" required>
    <option value="">-- Select Student --</option>
  </select><br>

  <button type="submit">Delete Student</button>
</form>
</body>
</html>
