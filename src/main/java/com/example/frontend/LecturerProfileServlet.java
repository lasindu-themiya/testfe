package com.example.frontend;

import com.example.frontend.dto.LecturerDTO;
import com.example.frontend.dto.PasswordUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/LecturerProfileServlet")
public class LecturerProfileServlet extends HttpServlet {
    private static final String BASE_BACKEND_URL = "http://localhost:8081/api/lecturer";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Possibly you want to load the current lecturer profile
        // GET /api/lecturer/profile
        // Then forward to updateProfile.jsp
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("updateProfile".equals(action)) {
            // This calls a PUT method to the backend
            LecturerDTO dto = new LecturerDTO();
            dto.setName(request.getParameter("name"));
            dto.setEmail(request.getParameter("email"));
            dto.setDepartment(request.getParameter("department"));
            dto.setContact(request.getParameter("contact"));
            dto.setCourseAssign(request.getParameter("courseAssign"));

            boolean success = updateLecturerProfile(dto, request);
            if (success) {
                request.setAttribute("message", "Profile updated successfully");
            } else {
                request.setAttribute("message", "Failed to update profile");
            }
            request.getRequestDispatcher("lecturer/updateProfile.jsp").forward(request, response);
        }
        else if ("updatePassword".equals(action)) {
            // PUT /api/lecturer/password/update
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");

            PasswordUpdateRequest pwdRequest = new PasswordUpdateRequest(currentPassword, newPassword);
            boolean success = updateLecturerPassword(pwdRequest, request);
            if (success) {
                request.setAttribute("message", "Password updated successfully");
            } else {
                request.setAttribute("message", "Failed to update password");
            }
            request.getRequestDispatcher("lecturer/updatePassword.jsp").forward(request, response);
        }
    }

    private boolean updateLecturerProfile(LecturerDTO dto, HttpServletRequest request) throws IOException {
        String backendUrl = BASE_BACKEND_URL + "/profile/update";
        String backendSessionId = (String) request.getSession().getAttribute("backendSessionId");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(dto);

        HttpURLConnection connection = (HttpURLConnection) new URL(backendUrl).openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        if (backendSessionId != null) {
            connection.setRequestProperty("Cookie", backendSessionId);
        }
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes("utf-8"));
        }

        int responseCode = connection.getResponseCode();
        connection.disconnect();
        return responseCode == 200;
    }

    private boolean updateLecturerPassword(PasswordUpdateRequest pwdRequest, HttpServletRequest request) throws IOException {
        String backendUrl = BASE_BACKEND_URL + "/password/update";
        String backendSessionId = (String) request.getSession().getAttribute("backendSessionId");

        // Make sure PasswordUpdateRequest has getters and setters so this is not empty
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(pwdRequest);

        HttpURLConnection connection = (HttpURLConnection) new URL(backendUrl).openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        if (backendSessionId != null) {
            connection.setRequestProperty("Cookie", backendSessionId);
        }
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes("utf-8"));
        }

        int responseCode = connection.getResponseCode();
        connection.disconnect();
        return responseCode == 200;
    }

}
