<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Student</title>
    <script>
        const students = <%= request.getAttribute("students") != null
        ? new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request.getAttribute("students"))
        : "[]" %>;

        function fetchStudentsByBatch() {
            const batchId = document.getElementById("batchId").value;
            if (batchId) {
                window.location.href = "/frontend_war/student/updateStudent?batchId=" + batchId;
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

    function populateStudentDetails() {
    const selectedId = document.getElementById('studentId').value;
    const studentDropdown = document.getElementById('studentId');
    const selectedOption = Array.from(studentDropdown.options).find(option => option.value === selectedId);

    if (selectedOption && selectedOption.dataset.details) {
        const studentDetails = JSON.parse(selectedOption.dataset.details);
        document.getElementById('name').value = studentDetails.name;
        document.getElementById('email').value = studentDetails.email;
        document.getElementById('password').value = studentDetails.password;
        document.getElementById('year').value = studentDetails.year;
        document.getElementById('contact').value = studentDetails.contact;
        document.getElementById('photo').value = studentDetails.photo;
            } else {
                document.getElementById('name').value = '';
                document.getElementById('email').value = '';
                document.getElementById('password').value = '';
                document.getElementById('year').value = '';
                document.getElementById('contact').value = '';
                document.getElementById('photo').value = '';
            }
        }
    </script>
</head>
<body>
<form action="/frontend_war/student/updateStudent" method="post">
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
    <select id="studentId" name="id" onchange="populateStudentDetails()" required>
        <option value="">-- Select Student --</option>
    </select><br>

    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br>

    <input type="hidden" id="role" name="role" value="batchmate" hidden="hidden">

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br>

    <label for="year">Year:</label>
    <input type="number" id="year" name="year" required><br>

    <label for="contact">Contact:</label>
    <input type="text" id="contact" name="contact" required><br>

    <label for="photo">Photo URL:</label>
    <input type="text" id="photo" name="photo"><br>

    <button type="submit">Update Student</button>
</form>
</body>

</html>
