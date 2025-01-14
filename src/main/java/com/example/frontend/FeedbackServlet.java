package com.example.frontend;

import com.example.frontend.dto.Batch;
import com.example.frontend.dto.Student;
import com.example.frontend.dto.FeedbackDTO;
import com.example.frontend.dto.FeedbackRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackServlet.class);

    private static final String GET_BATCHES_URL = "http://localhost:8081/api/batch/getAll";
    private static final String GET_STUDENTS_BY_BATCH = "http://localhost:8081/api/student/getAll/";
    private static final String CREATE_INDIVIDUAL_URL = "http://localhost:8081/api/feedback/add/individual/";
    private static final String CREATE_BATCHWISE_URL = "http://localhost:8081/api/feedback/add/batchwise/";
    private static final String UPDATE_URL = "http://localhost:8081/api/feedback/update/";
    private static final String DELETE_URL = "http://localhost:8081/api/feedback/delete/";
    private static final String VIEW_BY_BATCH_URL = "http://localhost:8081/api/feedback/view/batch/";
    private static final String VIEW_BY_STUDENT_URL = "http://localhost:8081/api/feedback/view/student/";
    private static final String VIEW_BY_FEEDBACK_URL = "http://localhost:8081/api/feedback/get/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        logger.debug("Received GET request with action: {}", action);

        if ("getBatches".equals(action)) {
            // Load all batches for feedback creation
            try {
                List<Batch> batches = fetchAllBatches(request);
                request.setAttribute("batchList", batches);
                request.getRequestDispatcher("feedback/createFeedback.jsp").forward(request, response);
            } catch (IOException e) {
                logger.error("Error fetching batches: {}", e.getMessage());
                request.setAttribute("message", "Error fetching batches.");
                request.getRequestDispatcher("feedback/createFeedback.jsp").forward(request, response);
            }

        } else if ("loadStudents".equals(action)) {
            // Load students for a specific batch during feedback creation
            String batchId = request.getParameter("batchId");
            logger.debug("Loading students for Batch ID: {}", batchId);
            try {
                List<Batch> batches = fetchAllBatches(request);
                List<Student> students = fetchAllStudents(batchId, request);

                request.setAttribute("batchList", batches);
                request.setAttribute("studentList", students);
                request.getRequestDispatcher("feedback/createFeedback.jsp").forward(request, response);
            } catch (IOException e) {
                logger.error("Error fetching students: {}", e.getMessage());
                request.setAttribute("message", "Error fetching students.");
                request.getRequestDispatcher("feedback/createFeedback.jsp").forward(request, response);
            }

        } else if ("viewByBatch".equals(action)) {
            // View feedback batchwise
            String batchId = request.getParameter("batchId");
            logger.debug("Viewing feedback for Batch ID: {}", batchId);
            try {
                List<Batch> batches = fetchAllBatches(request);
                List<FeedbackDTO> feedbackList = viewFeedbackByBatch(batchId, request);

                request.setAttribute("batchList", batches);
                request.setAttribute("feedbackList", feedbackList);
                request.setAttribute("selectedBatchId", batchId);
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
            } catch (IOException e) {
                logger.error("Error viewing feedback by batch: {}", e.getMessage());
                request.setAttribute("message", "Error viewing feedback by batch.");
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
            }

        } else if ("loadStudentsView".equals(action)) {
            // Load students for a selected batch in view feedback
            String batchId = request.getParameter("batchId");
            logger.debug("Loading students for Batch ID in view: {}", batchId);
            try {
                List<Batch> batches = fetchAllBatches(request);
                List<Student> students = fetchAllStudents(batchId, request);

                request.setAttribute("batchList", batches);
                request.setAttribute("studentList", students);
                request.setAttribute("selectedBatchId", batchId);
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
            } catch (IOException e) {
                logger.error("Error loading students for view: {}", e.getMessage());
                request.setAttribute("message", "Error loading students for view.");
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
            }

        } else if ("viewByStudent".equals(action)) {
            // View feedback by student
            String batchId = request.getParameter("batchId");
            String studentId = request.getParameter("studentId");
            logger.debug("Viewing feedback for Student ID: {}, Batch ID: {}", studentId, batchId);
            try {
                List<Batch> batches = fetchAllBatches(request);
                List<Student> students = (batchId != null && !batchId.trim().isEmpty()) ? fetchAllStudents(batchId, request) : null;
                List<FeedbackDTO> feedbackList = viewFeedbackByStudent(studentId, request);

                request.setAttribute("batchList", batches);
                request.setAttribute("studentList", students);
                request.setAttribute("feedbackList", feedbackList);
                request.setAttribute("selectedBatchId", batchId);
                request.setAttribute("selectedStudentId", studentId);
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
            } catch (IOException e) {
                logger.error("Error viewing feedback by student: {}", e.getMessage());
                request.setAttribute("message", "Error viewing feedback by student.");
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
            }

        } else if ("edit".equals(action)) {
            // Edit feedback
            String feedbackId = request.getParameter("id");
            logger.debug("Editing Feedback with ID: {}", feedbackId);

            if (feedbackId == null || feedbackId.trim().isEmpty()) {
                logger.error("Feedback ID is missing for edit action.");
                request.setAttribute("message", "Invalid Feedback ID.");
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
                return;
            }

            try {
                FeedbackDTO feedback = findFeedbackById(feedbackId, request);
                if (feedback == null) {
                    logger.error("Feedback not found for ID: {}", feedbackId);
                    request.setAttribute("message", "Feedback not found.");
                    request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
                    return;
                }

                request.setAttribute("feedback", feedback);
                request.getRequestDispatcher("feedback/editFeedback.jsp").forward(request, response);
            } catch (IOException e) {
                logger.error("Error finding feedback by ID: {}", e.getMessage());
                request.setAttribute("message", "Error finding feedback.");
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
            }

        } else if ("delete".equals(action)) {
            // Delete feedback
            String feedbackId = request.getParameter("id");
            logger.debug("Deleting Feedback with ID: {}", feedbackId);

            if (feedbackId == null || feedbackId.trim().isEmpty()) {
                logger.error("Feedback ID is missing for delete action.");
                request.setAttribute("message", "Invalid Feedback ID.");
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
                return;
            }

            try {
                boolean success = deleteFeedback(feedbackId, request);
                logger.debug("Deletion of Feedback ID {} was {}", feedbackId, success ? "successful" : "unsuccessful");
                request.setAttribute("message", success ? "Feedback deleted successfully." : "Failed to delete feedback.");
                response.sendRedirect("FeedbackServlet?action=viewByBatch&msg=" + (success ? "Deleted" : "Failed"));
            } catch (IOException e) {
                logger.error("Error deleting feedback: {}", e.getMessage());
                request.setAttribute("message", "Error deleting feedback.");
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
            }

        } else {
            // Default action
            logger.debug("Default action: Redirecting to getBatches");
            response.sendRedirect("FeedbackServlet?action=getBatches");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.debug("Received POST request with action: {}", action);

        if ("add".equals(action)) {
            // Add feedback for batchwise or individual
            String batchId = request.getParameter("batchId");
            String studentId = request.getParameter("studentId");
            String content = request.getParameter("content");
            String pointsStr = request.getParameter("points");

            logger.debug("Adding Feedback: Batch ID: {}, Student ID: {}, Content: {}, Points: {}", batchId, studentId, content, pointsStr);

            if (content == null || content.trim().isEmpty() || pointsStr == null || pointsStr.trim().isEmpty()) {
                logger.error("Invalid input data for adding feedback.");
                request.setAttribute("message", "Content and Points are required.");
                request.getRequestDispatcher("feedback/createFeedback.jsp").forward(request, response);
                return;
            }

            int points;
            try {
                points = Integer.parseInt(pointsStr);
            } catch (NumberFormatException e) {
                logger.error("Invalid points value: {}", pointsStr);
                request.setAttribute("message", "Points must be a valid number.");
                request.getRequestDispatcher("feedback/createFeedback.jsp").forward(request, response);
                return;
            }

            FeedbackRequest body = new FeedbackRequest(content, points);

            boolean success;
            if (studentId != null && !studentId.trim().isEmpty()) {
                success = createIndividualFeedback(studentId, body, request);
            } else {
                success = createBatchwiseFeedback(batchId, body, request);
            }
            logger.debug("Creation of Feedback was {}", success ? "successful" : "unsuccessful");
            request.setAttribute("message", success ? "Feedback added successfully!" : "Failed to add feedback.");
            request.getRequestDispatcher("feedback/createFeedback.jsp").forward(request, response);

        } else if ("update".equals(action)) {
            // Update feedback
            String feedbackId = request.getParameter("feedbackId");
            String content = request.getParameter("content");
            String pointsStr = request.getParameter("points");

            logger.debug("Updating Feedback: ID: {}, Content: {}, Points: {}", feedbackId, content, pointsStr);

            if (feedbackId == null || feedbackId.trim().isEmpty()) {
                logger.error("Feedback ID is missing for update.");
                request.setAttribute("message", "Feedback ID is missing.");
                request.getRequestDispatcher("feedback/viewFeedback.jsp").forward(request, response);
                return;
            }

            if (content == null || content.trim().isEmpty() || pointsStr == null || pointsStr.trim().isEmpty()) {
                logger.error("Invalid input data for updating feedback.");
                request.setAttribute("message", "Content and Points are required.");
                request.getRequestDispatcher("feedback/editFeedback.jsp").forward(request, response);
                return;
            }

            int points;
            try {
                points = Integer.parseInt(pointsStr);
            } catch (NumberFormatException e) {
                logger.error("Invalid points value: {}", pointsStr);
                request.setAttribute("message", "Points must be a valid number.");
                request.getRequestDispatcher("feedback/editFeedback.jsp").forward(request, response);
                return;
            }

            FeedbackRequest body = new FeedbackRequest(content, points);
            try {
                boolean success = updateFeedback(feedbackId, body, request);
                logger.debug("Update of Feedback ID {} was {}", feedbackId, success ? "successful" : "unsuccessful");
                request.setAttribute("message", success ? "Feedback updated successfully!" : "Failed to update feedback.");
                response.sendRedirect("FeedbackServlet?action=viewByBatch&msg=" + (success ? "Updated" : "Failed"));
            } catch (IOException e) {
                logger.error("Error updating feedback: {}", e.getMessage());
                request.setAttribute("message", "Error updating feedback.");
                request.getRequestDispatcher("feedback/editFeedback.jsp").forward(request, response);
            }
        }
    }


    // Fetch all batches
    private List<Batch> fetchAllBatches(HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GET_BATCHES_URL).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            Batch[] batchArray = mapper.readValue(conn.getInputStream(), Batch[].class);
            logger.debug("Fetched {} batches", batchArray.length);
            return Arrays.asList(batchArray);
        }
        throw new IOException("Failed to fetch batches: " + conn.getResponseCode());
    }

    // Fetch students for a batch
    private List<Student> fetchAllStudents(String batchId, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GET_STUDENTS_BY_BATCH + batchId).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            Student[] studentArray = mapper.readValue(conn.getInputStream(), Student[].class);
            logger.debug("Fetched {} students for Batch ID {}", studentArray.length, batchId);
            return Arrays.asList(studentArray);
        }
        throw new IOException("Failed to fetch students: " + conn.getResponseCode());
    }

    // View feedback by batch
    private List<FeedbackDTO> viewFeedbackByBatch(String batchId, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(VIEW_BY_BATCH_URL + batchId).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            FeedbackDTO[] feedbackArray = mapper.readValue(conn.getInputStream(), FeedbackDTO[].class);
            logger.debug("Fetched {} feedback entries for Batch ID {}", feedbackArray.length, batchId);
            return Arrays.asList(feedbackArray);
        }
        throw new IOException("Failed to fetch feedback by batch: " + conn.getResponseCode());
    }

    // View feedback by student
    private List<FeedbackDTO> viewFeedbackByStudent(String studentId, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(VIEW_BY_STUDENT_URL + studentId).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            FeedbackDTO[] feedbackArray = mapper.readValue(conn.getInputStream(), FeedbackDTO[].class);
            logger.debug("Fetched {} feedback entries for Student ID {}", feedbackArray.length, studentId);
            return Arrays.asList(feedbackArray);
        }
        throw new IOException("Failed to fetch feedback by student: " + conn.getResponseCode());
    }

    private FeedbackDTO findFeedbackById(String feedbackId, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(VIEW_BY_FEEDBACK_URL + feedbackId).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            FeedbackDTO feedback = mapper.readValue(conn.getInputStream(), FeedbackDTO.class);
            logger.debug("Fetched Feedback: {}", feedback);
            return feedback;
        } else if (conn.getResponseCode() == 404) {
            logger.warn("Feedback not found with ID: {}", feedbackId);
            return null;
        }
        throw new IOException("Failed to fetch feedback for edit: " + conn.getResponseCode());
    }

    // Create feedback for individual
    private boolean createIndividualFeedback(String studentId, FeedbackRequest body, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(CREATE_INDIVIDUAL_URL + studentId).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(conn.getOutputStream(), body);
        int responseCode = conn.getResponseCode();
        logger.debug("Create Individual Feedback Response Code: {}", responseCode);
        return responseCode == 200 || responseCode == 201;
    }

    // Create feedback batchwise
    private boolean createBatchwiseFeedback(String batchId, FeedbackRequest body, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(CREATE_BATCHWISE_URL + batchId).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(conn.getOutputStream(), body);
        int responseCode = conn.getResponseCode();
        logger.debug("Create Batchwise Feedback Response Code: {}", responseCode);
        return responseCode == 200 || responseCode == 201;
    }

    // Update feedback
    private boolean updateFeedback(String feedbackId, FeedbackRequest body, HttpServletRequest req) throws IOException {
        logger.debug("Updating Feedback with ID: {}", feedbackId);

        if (feedbackId == null || feedbackId.trim().isEmpty()) {
            logger.error("Feedback ID is missing for update.");
            throw new IllegalArgumentException("Feedback ID is missing.");
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(UPDATE_URL + feedbackId).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(conn.getOutputStream(), body);
        int responseCode = conn.getResponseCode();
        logger.debug("Update Feedback Response Code: {}", responseCode);
        return responseCode == 200 || responseCode == 204;
    }


    // Delete feedback
    private boolean deleteFeedback(String feedbackId, HttpServletRequest req) throws IOException {
        logger.debug("Deleting Feedback with ID: {}", feedbackId);

        if (feedbackId == null || feedbackId.trim().isEmpty()) {
            logger.error("Feedback ID is missing for deletion.");
            throw new IllegalArgumentException("Feedback ID is missing.");
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(DELETE_URL + feedbackId).openConnection();
        conn.setRequestMethod("DELETE");
        addSessionCookie(conn, req);

        conn.connect();
        int responseCode = conn.getResponseCode();
        logger.debug("Delete Feedback Response Code: {}", responseCode);
        return responseCode == 200 || responseCode == 204;
    }

    // Add session cookie to request
    private void addSessionCookie(HttpURLConnection conn, HttpServletRequest req) {
        String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");
        if (backendSessionId != null && !backendSessionId.isEmpty()) {
            conn.setRequestProperty("Cookie", backendSessionId);
            logger.debug("Added session cookie to backend request.");
        }
    }
}
