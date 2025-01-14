package com.example.frontend;

import com.example.frontend.dto.AdminDTO;
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

@WebServlet("/admin/createAdmin")
public class CreateAdminServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/admin/add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Create AdminDTO object from form data
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setUserName(req.getParameter("userName"));
            adminDTO.setEmail(req.getParameter("email"));
            adminDTO.setPassword(req.getParameter("password"));

            // Call backend API to create admin
            boolean success = createAdminInBackend(adminDTO);

            if (success) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Admin+created+successfully");
            } else {
                req.setAttribute("error", "Failed to create admin.");
                req.getRequestDispatcher("/createAdmin.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/createAdmin.jsp").forward(req, resp);
        }
    }

    private boolean createAdminInBackend(AdminDTO adminDTO) throws IOException {
        URL url = new URL(BACKEND_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);


        // Send JSON data to backend
        ObjectMapper mapper = new ObjectMapper();
        try (OutputStream os = connection.getOutputStream()) {
            os.write(mapper.writeValueAsBytes(adminDTO));
        }

        // Log backend response
        int responseCode = connection.getResponseCode();
        System.out.println("Backend Response Code: " + responseCode);

        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
