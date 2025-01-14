package com.example.frontend;

import com.example.frontend.dto.JwtResponse;
import com.example.frontend.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final String BACKEND_BASE_URL = "http://localhost:8081/api/auth";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // Validate input
        if (userId == null || password == null) {
            request.setAttribute("errorMessage", "User ID and password are required.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            // Authenticate the user with the backend
            JwtResponse jwtResponse = authenticateUser(userId, password, request);

            // Store session attributes from the backend
            storeSessionAttributes(request, jwtResponse);

            // Redirect to the appropriate dashboard based on the role
            redirectUser(response, jwtResponse.getRole());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Login failed: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private JwtResponse authenticateUser(String userId, String password, HttpServletRequest request) throws Exception {
        LoginRequest loginRequest = new LoginRequest(userId, password);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);

        for (String roleEndpoint : new String[]{"admin/login", "student/login", "lecturer/login"}) {
            String apiUrl = BACKEND_BASE_URL + "/" + roleEndpoint;
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonRequest.getBytes("utf-8"));
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Capture backend session ID
                String backendSessionId = connection.getHeaderField("Set-Cookie");
                request.getSession().setAttribute("backendSessionId", backendSessionId);

                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    return objectMapper.readValue(response.toString(), JwtResponse.class);
                }
            }
        }
        throw new Exception("Invalid credentials or API error.");
    }

    private void storeSessionAttributes(HttpServletRequest request, JwtResponse jwtResponse) {
        // Store token and role in session
        request.getSession().setAttribute("token", jwtResponse.getToken());
        request.getSession().setAttribute("role", jwtResponse.getRole());

        // Store user-specific ID in session based on role
        switch (jwtResponse.getRole().toUpperCase()) {
            case "ADMIN":
                request.getSession().setAttribute("userAId", jwtResponse.getUserId());
                break;
            case "STUDENT":
            case "BATCHMATE":
                request.getSession().setAttribute("userSId", jwtResponse.getUserId());
                break;
            case "LECTURER":
                request.getSession().setAttribute("userLId", jwtResponse.getUserId());
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + jwtResponse.getRole());
        }
    }

    private void redirectUser(HttpServletResponse response, String role) throws IOException {
        switch (role.toUpperCase()) {
            case "ADMIN":
                response.sendRedirect("adminDashboard.jsp");
                break;
            case "STUDENT":
            case "BATCHMATE":
                response.sendRedirect("student/dashboard");
                break;
            case "LEADER":
            case "MEMBER":
                response.sendRedirect("teamDashboard.jsp");
                break;
            case "LECTURER":
                response.sendRedirect("lecturer/dashboard");
                break;
            default:
                throw new IOException("Unknown role: " + role);
        }
    }
}
