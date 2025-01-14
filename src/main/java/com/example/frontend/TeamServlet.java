package com.example.frontend;

import com.example.frontend.dto.TeamRequest;
import com.example.frontend.dto.TeamResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@WebServlet("/TeamServlet")
public class TeamServlet extends HttpServlet {
    private static final String BASE_URL = "http://localhost:8081/api/team";
    private static final String STUDENT_INFO_URL = "http://localhost:8081/api/student/info"; // Backend endpoint for student info

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "view":
                handleViewTeams(request, response);
                break;
            case "add":
                populateAddTeamData(request, response);
                break;
            case "delete":
                handleDeleteTeam(request, response);
                break;
            default:
                response.sendRedirect("team/viewTeams.jsp");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            handleCreateTeam(request, response);
        } else if ("update".equals(action)) {
            handleUpdateTeam(request, response);
        }
    }

    private void handleViewTeams(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String batchId = fetchBatchIdFromBackend(request);

        if (batchId == null) {
            response.sendRedirect("login.jsp?error=Batch ID is missing. Please log in again.");
            return;
        }

        String url = BASE_URL + "/view/batch/" + batchId;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        setBackendSessionIdHeader(request, connection);

        if (connection.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            TeamResponse[] teams = mapper.readValue(connection.getInputStream(), TeamResponse[].class);
            request.setAttribute("teams", teams);
            request.getRequestDispatcher("team/viewTeams.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to fetch teams.");
        }
    }

    private void populateAddTeamData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String batchId = fetchBatchIdFromBackend(request);

        if (batchId == null) {
            response.sendRedirect("login.jsp?error=Batch ID is missing. Please log in again.");
            return;
        }

        String studentsUrl = BASE_URL + "/students/notInAteam/" + batchId;
        String lecturersUrl = "http://localhost:8081/api/lecturer/getAll";

        HttpURLConnection studentConnection = (HttpURLConnection) new URL(studentsUrl).openConnection();
        HttpURLConnection lecturerConnection = (HttpURLConnection) new URL(lecturersUrl).openConnection();

        setBackendSessionIdHeader(request, studentConnection);
        setBackendSessionIdHeader(request, lecturerConnection);

        if (studentConnection.getResponseCode() == 200 && lecturerConnection.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();

            // Deserialize students and lecturers into lists
            String[] studentIds = mapper.readValue(studentConnection.getInputStream(), String[].class);
            String[] lecturerIds = mapper.readValue(lecturerConnection.getInputStream(), String[].class);

            // Set attributes for JSP
            request.setAttribute("students", studentIds);
            request.setAttribute("lecturers", lecturerIds);

            request.getRequestDispatcher("team/addTeam.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to load students or lecturers.");
        }
    }

    private void handleCreateTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String leaderId = fetchStudentIdFromBackend(request);
        String batchId = fetchBatchIdFromBackend(request);

        if (leaderId == null || batchId == null) {
            response.sendRedirect("login.jsp?error=Student ID or Batch ID is missing. Please log in again.");
            return;
        }

        String members = String.join(",", request.getParameterValues("members"));
        String description = request.getParameter("description");
        String lecturerId = request.getParameter("lecturer");

        TeamRequest teamRequest = new TeamRequest(members, description, lecturerId);

        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + "/add").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        setBackendSessionIdHeader(request, connection);

        connection.setDoOutput(true);
        ObjectMapper mapper = new ObjectMapper();
        try (OutputStream os = connection.getOutputStream()) {
            mapper.writeValue(os, teamRequest);
        }

        if (connection.getResponseCode() == 200) {
            response.sendRedirect("TeamServlet?action=view");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create the team.");
        }
    }

    private void handleUpdateTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String teamId = request.getParameter("teamId");
        String members = String.join(",", request.getParameterValues("members"));
        String description = request.getParameter("description");
        String lecturerId = request.getParameter("lecturer");

        TeamRequest teamRequest = new TeamRequest(members, description, lecturerId);

        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + "/update/" + teamId).openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        setBackendSessionIdHeader(request, connection);

        connection.setDoOutput(true);
        ObjectMapper mapper = new ObjectMapper();
        try (OutputStream os = connection.getOutputStream()) {
            mapper.writeValue(os, teamRequest);
        }

        if (connection.getResponseCode() == 200) {
            response.sendRedirect("TeamServlet?action=view");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update the team.");
        }
    }

    private void handleDeleteTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String teamId = request.getParameter("teamId");
        String url = BASE_URL + "/delete/" + teamId;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("DELETE");
        setBackendSessionIdHeader(request, connection);

        if (connection.getResponseCode() == 200) {
            response.sendRedirect("TeamServlet?action=view");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete the team.");
        }
    }

    private String fetchStudentIdFromBackend(HttpServletRequest request) throws IOException {
        return fetchStudentInfoFromBackend(request).get("studentId");
    }

    private String fetchBatchIdFromBackend(HttpServletRequest request) throws IOException {
        return fetchStudentInfoFromBackend(request).get("batchId");
    }

    private Map<String, String> fetchStudentInfoFromBackend(HttpServletRequest request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(STUDENT_INFO_URL).openConnection();
        connection.setRequestMethod("GET");
        setBackendSessionIdHeader(request, connection);

        if (connection.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(connection.getInputStream(), new TypeReference<Map<String, String>>() {});
        } else {
            throw new IOException("Failed to fetch student information.");
        }
    }

    private void setBackendSessionIdHeader(HttpServletRequest request, HttpURLConnection connection) {
        String backendSessionId = (String) request.getSession().getAttribute("backendSessionId");
        if (backendSessionId != null) {
            connection.setRequestProperty("Cookie", backendSessionId);
        }
    }
}
