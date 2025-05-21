package Controller;

import Service.LESSONS_Service;
import Model.LESSONS;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "api_LESSONS_Controller", urlPatterns = {"/api/lessons/*"})
public class api_LESSONS_Controller extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo.equals("/get_all_lessons")) {
                getAllLessons(response);
            } else if (pathInfo.startsWith("/get_lesson_byID/")) {
                getLessonById(pathInfo, response);
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\":\"API endpoint not found\"}");
            }

        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void getAllLessons(HttpServletResponse response) throws IOException {
        LESSONS_Service lessonService = new LESSONS_Service();
        List<LESSONS> lessonList = lessonService.getAllLessons();

        if (lessonList != null && !lessonList.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(lessonList));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"No lessons found\"}");
        }
    }

    private void getLessonById(String pathInfo, HttpServletResponse response) throws IOException {
        try {
            String[] parts = pathInfo.split("/");
            int id = Integer.parseInt(parts[2]);

            LESSONS_Service lessonService = new LESSONS_Service();
            LESSONS lesson = lessonService.getLessonById(id);

            if (lesson != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(lesson));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Lesson not found\"}");
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid lesson ID format\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/create_lesson")) {
            try (BufferedReader reader = request.getReader()) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String json = sb.toString().trim();
                if (json.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Empty request body\"}");
                    return;
                }

                LESSONS lesson;
                try {
                    lesson = gson.fromJson(json, LESSONS.class);
                } catch (JsonSyntaxException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid JSON format: " + e.getMessage() + "\"}");
                    return;
                }

                if (lesson == null || lesson.getTitle() == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Missing required field (title)\"}");
                    return;
                }

                LESSONS_Service lessonService = new LESSONS_Service();
                lessonService.createLesson(lesson);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"message\": \"Lesson created successfully\"}");

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Error creating lesson: " + e.getMessage() + "\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"API endpoint not found\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo.startsWith("/delete_lesson/")) {
                deleteLesson(pathInfo, response);
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\":\"API endpoint not found\"}");
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void deleteLesson(String pathInfo, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] parts = pathInfo.split("/");
            int id = Integer.parseInt(parts[2]);

            LESSONS_Service lessonService = new LESSONS_Service();
            lessonService.deleteLesson(id);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Lesson deleted successfully\"}");

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid lesson ID format\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo.startsWith("/update_lesson/")) {
                updateLesson(pathInfo, response, request);
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\":\"API endpoint not found\"}");
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void updateLesson(String pathInfo, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            String[] parts = pathInfo.split("/");
            int id = Integer.parseInt(parts[2]);

            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            LESSONS lesson = gson.fromJson(sb.toString(), LESSONS.class);
            lesson.setID(id); // Gán ID từ path

            LESSONS_Service lessonService = new LESSONS_Service();
            lessonService.updateLesson(lesson);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Lesson updated successfully\"}");

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid lesson ID format\"}");
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private void handleError(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(500);
        response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        e.printStackTrace();
    }
}
