package Controller.Features;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Model.COURSES;
import Model.ENROLLS;
import Model.USERS;
import Service.COURSES_Service;
import Service.ENROLLS_Service;
import Service.USERS_Service;
import Utils.jwt;
import Utils.parseForm;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author minhk
 */
@WebServlet(urlPatterns = {"/api/admin/*"})
public class api_ADMIN_Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if (jwt.validateToken(request, response)) {
            int userRoleID = (int) request.getAttribute("roleID");
            if (userRoleID == 3) { // 1 student, 2 teacher, 3 admin
                String path = request.getPathInfo();
                switch (path) {
                    case "/users":
                        // Handle user-related admin actions
                        response.getWriter().write("{\"message\": \"Admin access to users\"}");
                        break;
                    case "/teachers":
                        String sort = request.getParameter("sort");
                        if (sort != null && sort.contains("studentCount")) {
                            // http://localhost:8080/QuanLyKhoaHoc/api/admin/teacher?sort=studentCount,desc

                            getTopTeachersByStudentCount(response, sort);
                        } else if (sort != null && sort.contains("courseRating")) {
                            //http://localhost:8080/QuanLyKhoaHoc/api/admin/teachers?sort=courseRating,desc

                            getTopTeachersByCourseRating(response, sort);
                        } else if (sort != null && sort.equals("courseCount,desc")) {
                            //http://localhost:8080/QuanLyKhoaHoc/api/admin/teachers?sort=courseCount,desc

                            getTopTeachersByCourseCount(response, sort);
                        } else {
                            // Default case or other sorting options can be handled here
                            response.getWriter().write("{\"message\": \"Admin access to teachers\"}");
                        }
                        break;
                    case "/quizzes":
                        // Handle quiz-related admin actions
                        response.getWriter().write("{\"message\": \"Admin access to quizzes\"}");
                        break;
                    case "/lessons":
                        // Handle lesson-related admin actions
                        response.getWriter().write("{\"message\": \"Admin access to lessons\"}");
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"message\": \"Resource not found\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"You are not authorized to access this resource\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if (jwt.validateToken(request, response)) {
            int userRoleID = (int) request.getAttribute("roleID");
            if (userRoleID == 3) { // 1 student, 2 teacher, 3 admin
                String path = request.getPathInfo();
                switch (path) {
                    case "/users":
                        // Handle user creation or updates
                        response.getWriter().write("{\"message\": \"Admin access to create or update users\"}");
                        break;
                    case "/teachers":
                        // Handle teacher-related admin actions
                        response.getWriter().write("{\"message\": \"Admin access to teachers\"}");
                        break;
                    case "/quizzes":
                        // Handle quiz creation or updates
                        response.getWriter().write("{\"message\": \"Admin access to create or update quizzes\"}");
                        break;
                    case "/lessons":
                        // Handle lesson creation or updates
                        response.getWriter().write("{\"message\": \"Admin access to create or update lessons\"}");
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"message\": \"Resource not found\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"You are not authorized to access this resource\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if (jwt.validateToken(request, response)) {
            int userRoleID = (int) request.getAttribute("roleID");
            if (userRoleID == 3) { // 1 student, 2 teacher, 3 admin
                String path = request.getPathInfo();
                switch (path) {
                    case "/users":
                        // Handle user updates
                        Map<String, String> params = parseForm.parseFormUrlEncoded(request);

                        int userId = Integer.parseInt(params.get("id"));

                        if( userId <= 0) {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().write("{\"error\": \"Missing required parameters\"}");
                            return;
                        }

                        USERS_Service userService = new USERS_Service();
                        USERS user = userService.getUserById(userId);
                        if (user == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().write("{\"error\": \"User not found\"}");
                            return;
                        }

                        if(params.containsKey("password")) {
                            String password = params.get("password");
                            if (password != null && !password.isEmpty()) {
                                user.setPassword(password);
                            }
                        }

                        if(params.containsKey("roleID")) {
                            int roleID = Integer.parseInt(params.get("roleID"));
                            if (roleID > 0 && roleID <= 3) { // Assuming valid role IDs are 1, 2, or 3
                                user.setRoleID(roleID);
                            } else {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.getWriter().write("{\"error\": \"Invalid role ID\"}");
                                return;
                            }
                        }

                        if(params.containsKey("isBanned")) {
                            Boolean isBanned = Integer.parseInt(params.get("isBanned")) == 1;
                            System.out.println("isBanned: " + isBanned);
                            user.setIsBanned(isBanned);
                        }

                        userService.updateUser(user);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(user));

                        break;
                    case "/teachers":
                        // Handle teacher updates
                        response.getWriter().write("{\"message\": \"Admin access to update teachers\"}");
                        break;
                    case "/quizzes":
                        // Handle quiz updates
                        response.getWriter().write("{\"message\": \"Admin access to update quizzes\"}");
                        break;
                    case "/lessons":
                        // Handle lesson updates
                        response.getWriter().write("{\"message\": \"Admin access to update lessons\"}");
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"message\": \"Resource not found\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"You are not authorized to access this resource\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }

    private void getTopTeachersByStudentCount(HttpServletResponse response, String sort) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();
        USERS_Service userService = new USERS_Service();
        List<USERS> teachers = userService.getUsersByRoleID(2);
        // get all courses taught by these teachers
        COURSES_Service coursesService = new COURSES_Service();
        Map<USERS, List<COURSES>> courseListByTeacher = new HashMap<>();
        for (USERS teacher : teachers) {
            List<COURSES> courses = coursesService.getCoursesByCreatedByUserID(teacher.getID());
            courseListByTeacher.put(teacher, courses);
        }
        for (Map.Entry<USERS, List<COURSES>> entry : courseListByTeacher.entrySet()) {
            USERS teacher = entry.getKey();
            List<COURSES> courses = entry.getValue();
            List<Integer> studentIDList = new ArrayList<>();
            for (COURSES course : courses) {
                ENROLLS_Service ENROLLSService = new ENROLLS_Service();
                List<ENROLLS> enrollList = ENROLLSService.getENROLLSByCourseId(course.getID());
                // check if the student is already in the list
                for (ENROLLS i : enrollList) {
                    if (!studentIDList.contains(i.getUserId())) {
                        studentIDList.add(i.getUserId());
                    }
                }
            }
            results.add(Map.of(
                    "teacherID", teacher.getID(),
                    "students", studentIDList,
                    "studentCount", studentIDList.size()
            ));
        }

        if(sort.contains("desc")) {
            results.sort((a, b) -> Integer.compare((Integer) b.get("studentCount"), (Integer) a.get("studentCount")));
        } else if (sort.contains("asc")) {
            results.sort((a, b) -> Integer.compare((Integer) a.get("studentCount"), (Integer) b.get("studentCount")));
        } else {
            // no sorting specified
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new Gson().toJson(results));
    }

    private void getTopTeachersByCourseRating(HttpServletResponse response, String sort) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();
        USERS_Service userService = new USERS_Service();
        List<USERS> teachers = userService.getUsersByRoleID(2);

        // get all courses taught by these teachers
        COURSES_Service coursesService = new COURSES_Service();
        for (USERS teacher : teachers) {
            List<COURSES> courses = coursesService.getCoursesByCreatedByUserID(teacher.getID());
            double totalRating = 0.0;
            int courseCount = 0;
            int ratingCount = 0;

            ENROLLS_Service enrollsService = new ENROLLS_Service();
            for(COURSES course : courses) {
                List<ENROLLS> enrolls = enrollsService.getENROLLSByCourseId(course.getID());
                double totalEnrollRating = 0.0;
                int enrollRatingCount = 0;
                for(ENROLLS enroll : enrolls) {
                    if (enroll.getRating() > 0) {
                        totalEnrollRating += enroll.getRating();
                        enrollRatingCount++;
                    }
                }

                double avgCourseRating = enrollRatingCount > 0 ? totalEnrollRating / enrollRatingCount : 0.0;
                if (avgCourseRating > 0) {
                    totalRating += avgCourseRating;
                    courseCount++;
                    ratingCount += enrollRatingCount;
                }
            }

            if (courseCount > 0) {
                double avgRating = totalRating / courseCount;
                avgRating = Math.round(avgRating * 100.0) / 100.0; // làm tròn 2 chữ số
                results.add(Map.of(
                        "teacherID", teacher.getID(),
                        "avgRating", avgRating,
                        "courseCount", courseCount,
                        "ratingCount", ratingCount 
                ));
            } else {
                results.add(Map.of(
                        "teacherID", teacher.getID(),
                        "avgRating", 0.0,
                        "courseCount", 0,
                        "ratingCount", 0
                ));
            }
        }

        if(sort.contains("desc")) {
            results.sort((a, b) -> Double.compare((Double) b.get("avgRating"), (Double) a.get("avgRating")));
        } else if (sort.contains("asc")) {
            results.sort((a, b) -> Double.compare((Double) a.get("avgRating"), (Double) b.get("avgRating")));
        } else {
            // no sorting specified
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new Gson().toJson(results));
    }

    private void getTopTeachersByCourseCount(HttpServletResponse response, String sort) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();
        USERS_Service userService = new USERS_Service();
        List<USERS> teachers = userService.getUsersByRoleID(2);

        // get all courses taught by these teachers
        COURSES_Service coursesService = new COURSES_Service();
        for (USERS teacher : teachers) {
            List<COURSES> courses = coursesService.getCoursesByCreatedByUserID(teacher.getID());
            List<Integer> courseIDs = new ArrayList<>();
            for (COURSES course : courses) {
                courseIDs.add(course.getID());
            }
            results.add(Map.of(
                    "teacherID", teacher.getID(),
                    "courses", courseIDs,
                    "courseCount", courses.size()
            ));
        }

        if(sort.contains("desc")) {
            results.sort((a, b) -> Integer.compare((Integer) b.get("courseCount"), (Integer) a.get("courseCount")));
        } else if (sort.contains("asc")) {
            results.sort((a, b) -> Integer.compare((Integer) a.get("courseCount"), (Integer) b.get("courseCount")));
        } else {
            // no sorting specified
        }
        

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new Gson().toJson(results));
    }
}

