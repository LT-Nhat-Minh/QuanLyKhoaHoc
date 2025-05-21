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
            String idParam = request.getParameter("id");
            LESSONS_Service lessonService = new LESSONS_Service();
            response.setCharacterEncoding("UTF-8");

            if (idParam != null) {
                // Lấy bài học theo ID
                try {
                    int id = Integer.parseInt(idParam);
                    LESSONS lesson = lessonService.getLessonById(id);
                    if (lesson != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(lesson));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\": \"Lesson not found\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid lesson id\"}");
                }
            } else {
                // Lấy tất cả bài học
                List<LESSONS> lessonList = lessonService.getAllLessons();

                if (lessonList != null && !lessonList.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(new Gson().toJson(lessonList));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"No lessons found\"}");
                }
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
                response.getWriter().write("{\"message\": \"Course created successfully\"}");

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Error creating course: " + e.getMessage() + "\"}");
            }
        }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing lesson ID\"}");
                return;
            }

            int lessonId = Integer.parseInt(idParam); // Có thể gây NumberFormatException
            LESSONS_Service lessonService = new LESSONS_Service();
            lessonService.deleteLesson(lessonId); // Giả sử phương thức cần int courseId

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Lesson deleted successfully\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid lesson ID format\"}");
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    @Override
        protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing lesson ID\"}");
                return;
            }

            int lessonId = Integer.parseInt(idParam);

            // Đọc dữ liệu JSON từ body request
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            LESSONS lesson = gson.fromJson(sb.toString(), LESSONS.class);
            lesson.setID(lessonId); // Gán ID từ tham số URL

            LESSONS_Service lessonService = new LESSONS_Service();
            lessonService.updateLesson(lesson);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Lesson updated successfully\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid lesson ID format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
