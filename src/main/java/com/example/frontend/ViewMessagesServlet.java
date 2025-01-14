package com.example.frontend;

import com.example.frontend.dto.MessageDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@WebServlet("/messages/ViewMessagesServlet")
public class ViewMessagesServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/lecturer/view-messages";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Fetch messages from the backend
            List<MessageDTO> messages = fetchMessagesFromBackend(request);

            if (messages != null && !messages.isEmpty()) {
                // Pass messages to the JSP
                request.setAttribute("messages", messages);

                // Forward the request to the JSP
                request.getRequestDispatcher("viewMessages.jsp").forward(request, response);
            } else {
                System.out.println("No messages retrieved.");
                request.setAttribute("messages", null);
                request.getRequestDispatcher("messages/viewMessages.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("../lecturerDashboard.jsp?error=An+error+occurred+while+fetching+messages");
        }
    }

    private List<MessageDTO> fetchMessagesFromBackend(HttpServletRequest req) throws IOException {
        URL url = new URL(BACKEND_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");
        if (backendSessionId != null) {
            // Extract only the session ID part
            String sessionIdValue = backendSessionId.split(";")[0]; // Splits on ";"
            connection.setRequestProperty("Cookie", sessionIdValue);
            System.out.println("Backend Session ID attached: " + sessionIdValue);
        } else {
            System.out.println("No Backend Session ID found.");
        }


        connection.connect();

        System.out.println("Response Code from Backend: " + connection.getResponseCode());

        if (connection.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            List<MessageDTO> messages = mapper.readValue(connection.getInputStream(), new TypeReference<List<MessageDTO>>() {});
            System.out.println("Messages fetched: " + messages.size());
            return messages;
        } else {
            System.out.println("Failed to fetch messages. Response Code: " + connection.getResponseCode());
            throw new RuntimeException("Failed to fetch messages. Response Code: " + connection.getResponseCode());
        }
    }
}
