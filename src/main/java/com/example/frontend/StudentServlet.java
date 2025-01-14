package com.example.frontend;

import com.example.frontend.dto.AnnouncementResponse;
import com.example.frontend.dto.InterviewResponse;
import com.example.frontend.dto.WorkshopResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
    private static final String BASE_URL = "http://localhost:8081/api/student";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Debugging: Check if userSId and batchId are present in the session
        String userSId = (String) request.getSession().getAttribute("userSId");
        String batchId = (String) request.getSession().getAttribute("batchId");
        System.out.println("Logged-in Student ID (userSId): " + userSId);
        System.out.println("Batch ID (batchId): " + batchId);

        if ((userSId == null || userSId.isEmpty()) && (batchId == null || batchId.isEmpty())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Student is not logged in. Please log in again.");
            return;
        }

        switch (action) {
            case "viewAnnouncementsByStudent":
                handleViewAnnouncements(request, response, true);
                break;
            case "viewAnnouncementsByBatch":
                handleViewAnnouncements(request, response, false);
                break;
            case "viewWorkshopsByStudent":
                handleViewWorkshops(request, response, true);
                break;
            case "viewWorkshopsByBatch":
                handleViewWorkshops(request, response, false);
                break;
            case "viewInterviewsByStudent":
                handleViewInterviews(request, response, true);
                break;
            case "viewInterviewsByBatch":
                handleViewInterviews(request, response, false);
                break;
            default:
                response.sendRedirect("studentDashboard.jsp");
                break;
        }
    }

    private void handleViewAnnouncements(HttpServletRequest request, HttpServletResponse response, boolean byStudent) throws IOException, ServletException {
        String url = byStudent ? BASE_URL + "/announcement/student" : BASE_URL + "/announcement/batch";
        try {
            List<AnnouncementResponse> announcements = fetchFromBackend(url, request, new TypeReference<List<AnnouncementResponse>>() {});
            System.out.println("Announcements fetched: " + (announcements != null ? announcements.size() : "null"));
            request.setAttribute("announcements", announcements);
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load announcements: " + e.getMessage());
        }
        request.getRequestDispatcher("student/viewAnnouncements.jsp").forward(request, response);
    }

    private void handleViewWorkshops(HttpServletRequest request, HttpServletResponse response, boolean byStudent) throws IOException, ServletException {
        String url = byStudent ? BASE_URL + "/workshop/student" : BASE_URL + "/workshop/batch";
        try {
            List<WorkshopResponse> workshops = fetchFromBackend(url, request, new TypeReference<List<WorkshopResponse>>() {});
            System.out.println("Workshops fetched: " + (workshops != null ? workshops.size() : "null"));
            request.setAttribute("workshops", workshops);
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load workshops: " + e.getMessage());
        }
        request.getRequestDispatcher("student/viewWorkshops.jsp").forward(request, response);
    }

    private void handleViewInterviews(HttpServletRequest request, HttpServletResponse response, boolean byStudent) throws IOException, ServletException {
        String url = byStudent ? BASE_URL + "/interview/student" : BASE_URL + "/interview/batch";
        try {
            List<InterviewResponse> interviews = fetchFromBackend(url, request, new TypeReference<List<InterviewResponse>>() {});
            System.out.println("Interviews fetched: " + (interviews != null ? interviews.size() : "null"));
            request.setAttribute("interviews", interviews);
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load interviews: " + e.getMessage());
        }
        request.getRequestDispatcher("student/viewInterviews.jsp").forward(request, response);
    }

    private <T> T fetchFromBackend(String backendUrl, HttpServletRequest request, TypeReference<T> typeReference) throws IOException {
        URL url = new URL(backendUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        // Attach the backend session ID
        String backendSessionId = (String) request.getSession().getAttribute("backendSessionId");
        if (backendSessionId != null) {
            connection.setRequestProperty("Cookie", backendSessionId);
        } else {
            throw new IOException("Backend session ID is missing. Ensure the student is logged in.");
        }

        // Debugging logs
        System.out.println("Request URL: " + url);
        System.out.println("Backend Session ID Sent: " + backendSessionId);

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == 200) {
            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                T response = mapper.readValue(is, typeReference);
                System.out.println("Response Data: " + response);
                return response;
            }
        } else if (responseCode == 401) {
            throw new IOException("Unauthorized: Backend session invalid or expired.");
        }

        throw new IOException("Failed to fetch data from backend. Response code: " + responseCode);
    }
}
