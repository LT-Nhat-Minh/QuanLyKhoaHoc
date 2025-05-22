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

@WebServlet(name = "api_COURSES_Controller", urlPatterns = {"/api/courses"})
public class api_COURSES_Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            if (request.getParameter("id") != null) {
                // Get course by ID
                int id = Integer.parseInt(request.getParameter("id"));

                COURSES_Service courseService = new COURSES_Service();
                COURSES course = courseService.getCourseById(id);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(course));
            } else {
                // Get all courses
                System.out.println("get all courses");
                COURSES_Service courseService = new COURSES_Service();
                List<COURSES> courseList = courseService.getAllCourses();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(courseList));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));

            // Validate input
            if (title == null || description == null || price <= 0) {
                throw new Exception("Missing required fields");
            }

            // Create course
            COURSES course = new COURSES(title, description, price);
            COURSES_Service courseService = new COURSES_Service();
            courseService.createCourse(course);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(new Gson().toJson(course));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            if (request.getParameter("id") != null) {
                // Get all parameters
                int id = Integer.parseInt(request.getParameter("id"));
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));

                // Validate input
                if (title == null || description == null || price <= 0) {
                    throw new Exception("Missing required fields");
                }

                COURSES course = new COURSES(id, title, description, price);
                COURSES_Service courseService = new COURSES_Service();
                courseService.updateCourse(course);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(course));
            } else {
                throw new Exception("Course ID is required");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Error updating course: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            if (request.getParameter("id") != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                COURSES_Service courseService = new COURSES_Service();
                COURSES course = courseService.getCourseById(id); // Store the course to be deleted
                courseService.deleteCourse(id);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Course deleted successfully\", \"course\": " + new Gson().toJson(course) + "}");
            } else {
                throw new Exception("Course ID is required");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Error deleting course: " + e.getMessage() + "\"}");
        }
    }
}
