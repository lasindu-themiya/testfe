package com.example.frontend;

import com.example.frontend.dto.Lecturer;
import com.example.frontend.dto.LecturerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/lecturer/updateLecturerForm")
public class UpdateLecturerServlet extends HttpServlet {


    private static final String GET_ALL_LECTURERS_URL = "http://localhost:8081/api/lecturer/getAll";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Fetch all lecturers from the backend
            List<Lecturer> lecturers = fetchAllLecturers();
            System.out.println("Lecturers fetched: " + lecturers); // Debugging

            // Set the lecturers list in the request attribute
            req.setAttribute("lecturers", lecturers);

            // Forward to the JSP
            req.getRequestDispatcher("/updateLecturer.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching lecturers: " + e.getMessage());
        }
    }

    private List<Lecturer> fetchAllLecturers() throws IOException {
        URL url = new URL(GET_ALL_LECTURERS_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = connection.getInputStream()) {
            return Arrays.asList(mapper.readValue(is, Lecturer[].class));
        }
    }


    private static final String BACKEND_URL = "http://localhost:8081/api/lecturer/update/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            LecturerDTO lecturerDTO = new LecturerDTO();
            lecturerDTO.setId(req.getParameter("id"));
            lecturerDTO.setName(req.getParameter("name"));
            lecturerDTO.setEmail(req.getParameter("email"));
            lecturerDTO.setPassword(req.getParameter("password"));
            lecturerDTO.setDepartment(req.getParameter("department"));
            lecturerDTO.setContact(req.getParameter("contact"));
            lecturerDTO.setCourseAssign(req.getParameter("courseAssign"));

            boolean success = updateLecturerInBackend(lecturerDTO);

            if (success) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Lecturer+updated+successfully");
            } else {
                req.setAttribute("error", "Failed to update lecturer.");
                req.getRequestDispatcher("/updateLecturer.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/updateLecturer.jsp").forward(req, resp);
        }
    }

    private boolean updateLecturerInBackend(LecturerDTO lecturerDTO) throws IOException {
        URL url = new URL(BACKEND_URL + lecturerDTO.getId());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        try (OutputStream os = connection.getOutputStream()) {
            os.write(mapper.writeValueAsBytes(lecturerDTO));
        }

        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}


