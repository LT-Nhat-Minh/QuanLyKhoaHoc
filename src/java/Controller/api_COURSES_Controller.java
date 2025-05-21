package Controller;

import Service.COURSES_Service;
import Model.COURSES;
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

@WebServlet(name = "api_COURSES_Controller", urlPatterns = {"/api/courses/*"})
public class api_COURSES_Controller extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo.equals("/get_all_courses")) {
                getAllCourses(response);
            } else if (pathInfo.startsWith("/get_course_byID/")) {
                getCourseById(pathInfo, response);
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\":\"API endpoint not found\"}");
            }

        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void getAllCourses(HttpServletResponse response) throws IOException {
        COURSES_Service courseService = new COURSES_Service();
        List<COURSES> courseList = courseService.getAllCourses();

        if (courseList != null && !courseList.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(courseList));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"No courses found\"}");
        }
    }

    private void getCourseById(String pathInfo, HttpServletResponse response) throws IOException {
        try {
            String[] parts = pathInfo.split("/");
            int id = Integer.parseInt(parts[2]);

            COURSES_Service courseService = new COURSES_Service();
            COURSES course = courseService.getCourseById(id);

            if (course != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(course));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Course not found\"}");
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid course ID format\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/create_course")) {
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

                COURSES course;
                try {
                    course = gson.fromJson(json, COURSES.class);
                } catch (JsonSyntaxException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid JSON format: " + e.getMessage() + "\"}");
                    return;
                }

                if (course == null || course.getTitle() == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Missing required field (title)\"}");
                    return;
                }

                COURSES_Service courseService = new COURSES_Service();
                courseService.createCourse(course);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"message\": \"Course created successfully\"}");

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Error creating course: " + e.getMessage() + "\"}");
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
            if (pathInfo.startsWith("/delete_course/")) {
                deleteCourse(pathInfo, response);
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\":\"API endpoint not found\"}");
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void deleteCourse(String pathInfo, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] parts = pathInfo.split("/");
            int id = Integer.parseInt(parts[2]);

            COURSES_Service courseService = new COURSES_Service();
            courseService.deleteCourse(id);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Course deleted successfully\"}");

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid course ID format\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo.startsWith("/update_course/")) {
                updateCourse(pathInfo, response, request);
            } else {
                response.setStatus(404);
                response.getWriter().write("{\"error\":\"API endpoint not found\"}");
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void updateCourse(String pathInfo, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            String[] parts = pathInfo.split("/");
            int id = Integer.parseInt(parts[2]);

            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            COURSES course = gson.fromJson(sb.toString(), COURSES.class);
            course.setID(id); // Gán ID từ path

            COURSES_Service courseService = new COURSES_Service();
            courseService.updateCourse(course);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Course updated successfully\"}");

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"Invalid course ID format\"}");
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
