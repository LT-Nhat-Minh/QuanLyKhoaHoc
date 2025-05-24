package Controller;

import Model.COURSES;
import Service.LESSONS_Service;
import Model.LESSONS;
import Utils.parseForm;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "api_LESSONS_Controller", urlPatterns = {"/api/lessons"})
public class api_LESSONS_Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            if (request.getParameter("id") != null) {
                // Get lesson by ID
                int id = Integer.parseInt(request.getParameter("id"));
                LESSONS_Service lessonService = new LESSONS_Service();
                LESSONS lesson = lessonService.getLessonById(id);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(lesson));
            } else if(request.getParameter("courseID") != null ) {
                
                int courseID = Integer.parseInt(request.getParameter("courseID"));
                LESSONS_Service lessonService = new LESSONS_Service();
                List<LESSONS> lessons = lessonService.getLessonByCourseID(courseID);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(lessons));
                
            }
            else{
                LESSONS_Service lessonService = new LESSONS_Service();
                List<LESSONS> lessonList = lessonService.getAllLessons();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(lessonList));
                
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            int courseID = Integer.parseInt(request.getParameter("courseID"));
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String videoURL = request.getParameter("videoURL");

            // Validate input
            if (title == null || content == null || videoURL == null) {
                throw new Exception("Missing required fields");
            }

            // Create lesson
            LESSONS lesson = new LESSONS(courseID, title, content, videoURL);
            LESSONS_Service lessonService = new LESSONS_Service();
            lessonService.createLesson(lesson);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(new Gson().toJson(lesson));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            // Manually parse form data from body (x-www-form-urlencoded)
            Map<String, String> params = parseForm.parseFormUrlEncoded(request);

            int id = Integer.parseInt(params.get("id"));
            String title = params.get("title");
            String content = params.get("content");
            String videoURL = params.get("videoURL");

            // Validate input
            if (title == null || content == null || videoURL == null) {
                throw new Exception("Missing required fields");
            }

            // Update lesson
            LESSONS lesson = new LESSONS(id, title, content, videoURL);
            LESSONS_Service lessonService = new LESSONS_Service();
            lessonService.updateLesson(lesson);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new Gson().toJson(lesson));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            if (request.getParameter("id") != null) {
                // Delete lesson by ID
                int id = Integer.parseInt(request.getParameter("id"));
                LESSONS_Service lessonService = new LESSONS_Service();
                LESSONS lesson = lessonService.getLessonById(id);
                lessonService.deleteLesson(id);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Lesson deleted successfully\", \"lesson\": " + new Gson().toJson(lesson) + "}");
            } else {
                throw new Exception("Missing lesson ID");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
