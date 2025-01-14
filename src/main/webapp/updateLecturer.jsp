<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Lecturer</title>
    <script>
        // Define the lecturers as a JavaScript variable embedded in the JSP
        const lecturers = <%= request.getAttribute("lecturers") != null
        ? new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request.getAttribute("lecturers"))
        : "[]" %>;

        // Populate the combo box and details when the page loads
        window.onload = function () {
            populateLecturerDropdown();
        };

        function populateLecturerDropdown() {
            const lecturerDropdown = document.getElementById('lecturerId');

            // Populate the dropdown
            lecturers.forEach(lecturer => {
                const option = document.createElement('option');
                option.value = lecturer.id;
                option.textContent = lecturer.name;
                lecturerDropdown.appendChild(option);
            });
        }

        function populateLecturerDetails() {
            const selectedId = document.getElementById('lecturerId').value;
            const selectedLecturer = lecturers.find(lecturer => lecturer.id === selectedId);

            if (selectedLecturer) {
                document.getElementById('name').value = selectedLecturer.name;
                document.getElementById('email').value = selectedLecturer.email;
                document.getElementById('password').value = selectedLecturer.password;
                document.getElementById('department').value = selectedLecturer.department;
                document.getElementById('contact').value = selectedLecturer.contact;
                document.getElementById('courseAssign').value = selectedLecturer.courseAssign;
            } else {
                // Clear fields if no lecturer is selected
                document.getElementById('name').value = '';
                document.getElementById('email').value = '';
                document.getElementById('password').value = '';
                document.getElementById('department').value = '';
                document.getElementById('contact').value = '';
                document.getElementById('courseAssign').value = '';
            }
        }
    </script>

</head>
<body>
<form action="/frontend_war/lecturer/updateLecturerForm" method="post">
    <label for="lecturerId">Select Lecturer:</label>
    <select id="lecturerId" name="id" onchange="populateLecturerDetails()" required>
        <option value="">-- Select Lecturer --</option>
    </select><br>

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

    <button type="submit">Update Lecturer</button>
</form>
</body>
</html>
