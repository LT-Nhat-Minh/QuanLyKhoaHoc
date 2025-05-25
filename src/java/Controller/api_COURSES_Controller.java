package Controller;

import Service.COURSES_Service;
import Model.COURSES;
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
            int createdByUserID = Integer.parseInt(request.getParameter("createdByUserID"));

            // Validate input
            if (title == null || description == null || price <= 0) {
                throw new Exception("Missing required fields");
            }

            // Create course
            COURSES course = new COURSES(title, description, price, createdByUserID);
            COURSES_Service courseService = new COURSES_Service();
            courseService.createCourse(course);

            //Kiểm tra xem lesson đã đủ quizzi có câu trả lời hay chưa
            //Nếu đủ rồi thì cập nhật bảng STUDIED trạng thái là đã hoàn thành LESSON

            //Kiểm tra xem course đã đủ lesson mà học sinh đã hoàn thành hay chưa
            //Nếu đủ rồi thì cập nhật bảng ENROLLMEN trạng thái là đã hoàn thành COURSE

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
        // Manually parse form data from body (x-www-form-urlencoded)
        Map<String, String> params = parseForm.parseFormUrlEncoded(request);
    

        int id = Integer.parseInt(params.get("id"));
        String title = params.get("title");
        String description = params.get("description");
        double price = Double.parseDouble(params.get("price"));

        // Validate data
        if (title == null || description == null || price <= 0) {
            throw new Exception("Missing required fields");
        }

        // Update course
        if (id > 0) {
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
