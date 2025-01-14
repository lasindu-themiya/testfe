package com.example.frontend;

import com.example.frontend.dto.Lecturer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@WebServlet("/lecturer/addLecturer")
public class AddLecturerServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/lecturer/add";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/addLecturer.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Retrieve Admin ID from session
            String adminId = (String) req.getSession().getAttribute("userAId");
            String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");

            System.out.println("Admin ID from session: " + adminId); // Debugging
            System.out.println("Backend Session ID: " + backendSessionId); // Debugging

            if (adminId == null || adminId.isEmpty()) {
                req.setAttribute("error", "Unauthorized: Admin must be logged in to add a lecturer.");
                req.getRequestDispatcher("/addLecturer.jsp").forward(req, resp);
                return;
            }

            // Create Lecturer object from request
            Lecturer lecturerDTO = new Lecturer();
            lecturerDTO.setName(req.getParameter("name"));
            lecturerDTO.setEmail(req.getParameter("email"));
            lecturerDTO.setPassword(req.getParameter("password"));
            lecturerDTO.setDepartment(req.getParameter("department"));
            lecturerDTO.setContact(req.getParameter("contact"));
            lecturerDTO.setCourseAssign(req.getParameter("courseAssign"));

            // Send lecturer data to backend
            boolean success = createLecturerInBackend(lecturerDTO, adminId, backendSessionId);

            if (success) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Lecturer+added+successfully");
            } else {
                req.setAttribute("error", "Failed to add lecturer.");
                req.getRequestDispatcher("/addLecturer.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/addLecturer.jsp").forward(req, resp);
        }
    }

    private boolean createLecturerInBackend(Lecturer lecturerDTO, String adminId, String backendSessionId) throws IOException {
        URL url = new URL(BACKEND_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Attach backend session cookie
        if (backendSessionId != null) {
            connection.setRequestProperty("Cookie", backendSessionId);
        }

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(lecturerDTO);
        System.out.println("Payload: " + payload);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes("utf-8"));
        }
        int responseCode = connection.getResponseCode();
        System.out.println("Backend Response Code: " + responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                String errorMessage = new BufferedReader(new InputStreamReader(errorStream))
                        .lines().collect(Collectors.joining("\n"));
                System.out.println("Backend Error Message: " + errorMessage);
            }
        }

        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
