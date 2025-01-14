package com.example.frontend;

import com.example.frontend.dto.StudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String backendUrl = "http://localhost:8081/api/student/get";

        try {
            StudentDTO student = fetchStudentDetailsFromBackend(backendUrl, req);

            if (student != null) {
                req.setAttribute("id", student.getId());
                req.setAttribute("regDate", student.getRegDate());
                req.setAttribute("year", student.getYear());
                req.setAttribute("contact", student.getContact());
                req.setAttribute("email", student.getEmail());
                req.setAttribute("name", student.getName());
                req.setAttribute("photo", student.getPhoto());
                req.setAttribute("role", student.getRole());
                req.getRequestDispatcher("/studentDashboard.jsp").forward(req, resp);
            } else {
                resp.sendRedirect("/login.jsp?error=Unable+to+fetch+student+details");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/login.jsp?error=An+error+occurred+while+fetching+details");
        }
    }

    private StudentDTO fetchStudentDetailsFromBackend(String backendUrl, HttpServletRequest req) throws IOException {
        URL url = new URL(backendUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        // Use backend session ID
        String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");
        if (backendSessionId != null) {
            connection.setRequestProperty("Cookie", backendSessionId);
        }

        System.out.println("Request URL: " + backendUrl);
        System.out.println("Session ID Sent as Cookie: " + backendSessionId);

        connection.connect();

        if (connection.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(connection.getInputStream(), StudentDTO.class);
        } else {
            throw new RuntimeException("Failed to fetch student details. Response Code: " + connection.getResponseCode());
        }
    }

}
