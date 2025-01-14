package com.example.frontend;

import com.example.frontend.dto.LecturerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/lecturer/dashboard")
public class LecturerDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String backendUrl = "http://localhost:8081/api/lecturer/profile";

        try {
            // Fetch lecturer details from the backend
            LecturerDTO lecturer = fetchLecturerDetailsFromBackend(backendUrl, req);

            if (lecturer != null) {
                // Pass lecturer details to the JSP
                req.setAttribute("id", lecturer.getId());
                req.setAttribute("name", lecturer.getName());
                req.setAttribute("email", lecturer.getEmail());
                req.setAttribute("department", lecturer.getDepartment());
                req.setAttribute("contact", lecturer.getContact());
                req.setAttribute("courseAssign", lecturer.getCourseAssign());

                // Forward the request to the dashboard JSP
                req.getRequestDispatcher("/lecturerDashboard.jsp").forward(req, resp);
            } else {
                resp.sendRedirect("login.jsp?error=Unable+to+fetch+lecturer+details");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("login.jsp?error=An+error+occurred+while+fetching+details");
        }
    }

    private LecturerDTO fetchLecturerDetailsFromBackend(String backendUrl, HttpServletRequest req) throws IOException {
        URL url = new URL(backendUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        // Attach the backend session cookie
        String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");
        if (backendSessionId != null) {
            connection.setRequestProperty("Cookie", backendSessionId);
        }

        connection.connect();

        if (connection.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(connection.getInputStream(), LecturerDTO.class);
        } else {
            throw new RuntimeException("Failed to fetch lecturer details. Response Code: " + connection.getResponseCode());
        }
    }
}
