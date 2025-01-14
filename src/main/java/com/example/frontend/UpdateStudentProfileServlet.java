package com.example.frontend;

import com.example.frontend.dto.StudentProfileUpdateRequest;
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


@WebServlet("/student/updateProfile")
public class UpdateStudentProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String backendUrl = "http://localhost:8081/api/student/profile/update";

        try {
            // Retrieve backend session ID from the HTTP session
            String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");

            if (backendSessionId == null) {
                resp.sendRedirect("/frontend_war/login.jsp?error=Session+expired.+Please+log+in+again.");
                return;
            }

            // Create the update request payload
            StudentProfileUpdateRequest profileRequest = new StudentProfileUpdateRequest();
            profileRequest.setName(req.getParameter("name"));
            profileRequest.setEmail(req.getParameter("email"));
            profileRequest.setContact(req.getParameter("contact"));
            profileRequest.setPhoto(req.getParameter("photo"));

            // Send the update request to the backend
            boolean success = sendUpdateRequestToBackend(backendUrl, profileRequest, backendSessionId);

            if (success) {
                resp.sendRedirect("/frontend_war/student/dashboard?message=Profile+updated+successfully");
            } else {
                resp.sendRedirect("/frontend_war/student/updateStudentProfile.jsp?error=Failed+to+update+profile");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/frontend_war/student/updateStudentProfile.jsp?error=An+error+occurred+while+updating+profile");
        }
    }

    private boolean sendUpdateRequestToBackend(String backendUrl, StudentProfileUpdateRequest profileRequest, String backendSessionId) throws IOException {
        URL url = new URL(backendUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Cookie", backendSessionId); // Attach the session ID as a cookie
        connection.setDoOutput(true);

        // Serialize the request body
        ObjectMapper mapper = new ObjectMapper();
        try (OutputStream os = connection.getOutputStream()) {
            os.write(mapper.writeValueAsBytes(profileRequest));
        }

        // Check the response status
        int responseCode = connection.getResponseCode();
        System.out.println("Backend Response Code: " + responseCode);

        return responseCode == 200;
    }
}
