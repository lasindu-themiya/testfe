package com.example.frontend;

import com.example.frontend.dto.Batch;
import com.example.frontend.dto.Portfolio;
import com.example.frontend.dto.Student;
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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebServlet("/PortfolioViewServlet")
public class PortfolioViewServlet extends HttpServlet {

    // Backend endpoints
    private static final String GET_BATCHES_URL = "http://localhost:8081/api/lecturer/get/batches";
    private static final String GET_STUDENTS_BY_BATCH_URL = "http://localhost:8081/api/lecturer/search/students/";
    private static final String GET_PORTFOLIO_URL = "http://localhost:8081/api/lecturer/portfolio/view/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("getBatches".equals(action)) {
            // Load all batches
            List<Batch> batchList = fetchAllBatches(request);
            request.setAttribute("batchList", batchList);
            request.getRequestDispatcher("portfolio/lecturerViewPortfolio.jsp").forward(request, response);

        } else if ("loadStudents".equals(action)) {
            // Load students of a specific batch
            String batchId = request.getParameter("batchId");
            List<Batch> batchList = fetchAllBatches(request);
            List<Student> studentList = fetchAllStudents(batchId, request);

            request.setAttribute("batchList", batchList);
            request.setAttribute("studentList", studentList);
            request.getRequestDispatcher("portfolio/lecturerViewPortfolio.jsp").forward(request, response);

        }else if ("viewPortfolio".equals(action)) {
            // View portfolio for a specific student
            String studentId = request.getParameter("studentId");
            List<Portfolio> portfolioList = fetchPortfolio(studentId, request);

            List<Batch> batchList = fetchAllBatches(request);
            request.setAttribute("batchList", batchList);
            request.setAttribute("portfolioList", portfolioList);
            request.getRequestDispatcher("portfolio/lecturerViewPortfolio.jsp").forward(request, response);
        }
        else {
            // Default action: Load batches
            response.sendRedirect("PortfolioViewServlet?action=getBatches");
        }
    }
    private List<Batch> fetchAllBatches(HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GET_BATCHES_URL).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (InputStream is = conn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                // Parse the response as a list of strings
                List<String> batchIds = mapper.readValue(is, new TypeReference<List<String>>() {});
                // Wrap each string into a Batch object
                return batchIds.stream()
                        .map(id -> {
                            Batch batch = new Batch();
                            batch.setId(id);
                            batch.setName("Batch " + id); // Optional: Add a placeholder name
                            return batch;
                        })
                        .toList();
            }
        }
        throw new IOException("Failed to fetch batches. Code: " + responseCode);
    }


    private List<Student> fetchAllStudents(String batchId, HttpServletRequest req) throws IOException {
        String fullUrl = GET_STUDENTS_BY_BATCH_URL + batchId;
        HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = conn.getInputStream()) {
                // Parse the response as a list of strings
                List<String> studentIds = mapper.readValue(is, new TypeReference<List<String>>() {});
                // Wrap each string into a Student object
                return studentIds.stream()
                        .map(id -> {
                            Student student = new Student();
                            student.setId(id);
                            student.setName("Student " + id); // Optional placeholder name
                            return student;
                        })
                        .toList();
            }
        }
        throw new IOException("Failed to fetch students. Response code: " + conn.getResponseCode());
    }


    private List<Portfolio> fetchPortfolio(String studentId, HttpServletRequest req) throws IOException {
        String fullUrl = GET_PORTFOLIO_URL + studentId;
        HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = conn.getInputStream()) {
                // Parse the response as a list of Portfolio objects
                return mapper.readValue(is, new TypeReference<List<Portfolio>>() {});
            }
        }
        throw new IOException("Failed to fetch portfolio. Response code: " + conn.getResponseCode());
    }


    private void addSessionCookie(HttpURLConnection conn, HttpServletRequest req) {
        String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");
        if (backendSessionId != null && !backendSessionId.isEmpty()) {
            conn.setRequestProperty("Cookie", backendSessionId);
        }
    }
}
