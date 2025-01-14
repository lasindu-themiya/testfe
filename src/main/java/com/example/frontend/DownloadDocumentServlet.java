package com.example.frontend;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;


@WebServlet("/messages/DownloadDocumentServlet")
public class DownloadDocumentServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/lecturer/download-document/";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String backendSessionId = (String) session.getAttribute("backendSessionId");

        if (backendSessionId == null || backendSessionId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access.");
            return;
        }

        String documentId = request.getParameter("documentId");

        if (documentId == null || documentId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Document ID is required.");
            return;
        }

        HttpURLConnection conn = null;

        try {
            // Connect to backend
            URL url = new URL(BACKEND_URL + documentId);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Cookie", "JSESSIONID=" + backendSessionId);
            conn.setDoOutput(true);

            // Get the response from the backend
            InputStream backendInput = conn.getInputStream();

            // Set response headers for file download
            String fileName = Paths.get(documentId).getFileName().toString();
            response.setContentType(conn.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // Write backend response to the client
            OutputStream clientOutput = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = backendInput.read(buffer)) != -1) {
                clientOutput.write(buffer, 0, bytesRead);
            }

            clientOutput.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error downloading document: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
