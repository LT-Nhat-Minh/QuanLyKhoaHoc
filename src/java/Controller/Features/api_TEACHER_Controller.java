/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Features;

import Model.COURSES;
import Model.ENROLLS;
import Model.LESSONS;
import Model.QUIZZES;
import Model.USERS;
import Service.COURSES_Service;
import Service.ENROLLS_Service;
import Service.Features.TEACHER_Service;
import Service.LESSONS_Service;
import Service.QUIZZES_Service;
import Service.USERS_Service;
import Utils.jwt;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author minhk
 */
@WebServlet(name = "api_TEACHER_Controller", urlPatterns = {"/api/teacher/*"})
public class api_TEACHER_Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if(jwt.validateToken(request)) {
            int teacherID = (int) request.getAttribute("id");
            int userRoleID = (int) request.getAttribute("roleID");
            if (userRoleID >= 2) { // 1 student, 2 teacher, 3 admin
                String path = request.getPathInfo();
                switch (path) {
                    case "/student":
                        if (request.getParameter("quizID") != null) {
                            getTopStudentByQuizScore(request, response, teacherID);
                            
                        } else if (request.getParameter("lessonID") != null) {
                            getTopStudentByLessonScore(request, response, teacherID);

                        } else if (request.getParameter("courseID") != null) {
                            getTopStudentByCourseScore(request, response, teacherID);
                            
                        } else {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().write("{\"error\":\"Missing required parameters\"}");
                        }
                        break;
                    case "/courses":
                        getTopCourseByAvgStudentScore(request, response, teacherID);
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\":\"Invalid endpoint\"}");
                        break;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"You do not have permission to access this resource\"}");
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
        String path = request.getPathInfo();

        if (jwt.validateToken(request)) {
            int userRoleID = (int) request.getAttribute("roleID");

            if (userRoleID >= 2) { // 1 student, 2 teacher, 3 admin
                int userId = (int) request.getAttribute("id");
                switch (path) {
                    case "/courses":
                        {
                            // Create a new course
                            String title = request.getParameter("title");
                            String description = request.getParameter("description");
                            Double price = Double.parseDouble(request.getParameter("price"));
                            // Validate input
                            if (title == null || description == null || price <= 0) {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.getWriter().write("{\"error\":\"Missing required fields\"}");
                                return;
                            }   // Create course
                            COURSES_Service courseService = new COURSES_Service();
                            COURSES course = new COURSES(title, description, price, userId);
                            courseService.createCourse(course);

                            response.setStatus(HttpServletResponse.SC_CREATED);
                            response.getWriter().write(new Gson().toJson(course));
                            break;
                        }
                // Create a new lesson
                    case "/lessons":
                        {
                            int courseID = Integer.parseInt(request.getParameter("courseID"));
                            // check if the course exists and is created by the teacher
                            COURSES_Service courseServiceCheck = new COURSES_Service();
                            COURSES courseObj = courseServiceCheck.getCourseById(courseID);

                            if (courseObj == null || courseObj.getCreatedByUserID() != userId) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.getWriter().write("{\"message\": \"You are not the owner of this course\"}");
                                return;
                            }

                            String lessonTitle = request.getParameter("title");
                            String lessonContent = request.getParameter("content");
                            String lessonVideoURL = request.getParameter("videoURL");

                            // Validate data
                            if (courseID <= 0 || lessonTitle == null || lessonContent == null || lessonVideoURL == null) {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.getWriter().write("{\"error\":\"Missing required fields\"}");
                                return;
                            }

                            // Create lesson
                            LESSONS_Service lessonService = new LESSONS_Service();
                            LESSONS lesson = new LESSONS(courseID, lessonTitle, lessonContent, lessonVideoURL);
                            lessonService.createLesson(lesson);

                            response.setStatus(HttpServletResponse.SC_CREATED);
                            response.getWriter().write(new Gson().toJson(lesson));
                            break;
                        }
                // Create a new quiz
                    case "/quizzies":
                        {
                            int lessonID = Integer.parseInt(request.getParameter("lessonID"));

                            // check if the lesson exists and is created by the teacher
                            LESSONS_Service lessonServiceCheck = new LESSONS_Service();
                            LESSONS lessonObj = lessonServiceCheck.getLessonById(lessonID);
                            COURSES_Service courseService = new COURSES_Service();
                            COURSES courseObj = courseService.getCourseById(lessonObj.getCourseID());
                            if (courseObj == null || courseObj.getCreatedByUserID() != userId) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.getWriter().write("{\"message\": \"You are not the owner of this course\"}");
                                return;
                            }

                            String quizTitle = request.getParameter("title");
                            String question = request.getParameter("question");
                            int correctAnswer = Integer.parseInt(request.getParameter("correctAnswer"));

                            // Validate data
                            if (lessonID <= 0 || quizTitle == null || question == null || correctAnswer < 0) {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.getWriter().write("{\"error\":\"Missing required fields\"}");
                                return;
                            }

                            // Create quiz
                            QUIZZES_Service quizService = new QUIZZES_Service();
                            QUIZZES quiz = new QUIZZES(lessonID, quizTitle, question, correctAnswer);
                            quizService.createQuiz(quiz);

                            response.setStatus(HttpServletResponse.SC_CREATED);
                            response.getWriter().write(new Gson().toJson(quiz));
                            break;
                        }
                    default:
                        System.out.println("path: " + path);
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\":\"Invalid endpoint\"}");
                        break;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"You do not have permission to access this resource\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }

    private void getTopStudentByCourseScore(HttpServletRequest request, HttpServletResponse response, int teacherID) throws IOException {
        int courseID = Integer.parseInt(request.getParameter("courseID"));
        // Get all students who have enrolled in the course
        // COURSES_ID -> ENROLLS -> STUDENTS -> COURSES
        COURSES_Service courseService = new COURSES_Service();
        COURSES courseObj = courseService.getCourseById(courseID);
        if (courseObj == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"message\": \"Course not found\"}");
            return;
        }

        // Filter courseList created by teacher
        if (courseObj.getCreatedByUserID() != teacherID) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"message\": \"You are not the owner of this course\"}");
            return;
        }

        ENROLLS_Service ENROLLService = new ENROLLS_Service();
        List<ENROLLS> enrollmentList = ENROLLService.getENROLLSByCourseId(courseObj.getID());
        List<USERS> studentList = new ArrayList<>();
        for (ENROLLS enrollment : enrollmentList) {
            // Get student details by ID
            USERS_Service userService = new USERS_Service();
            USERS student = userService.getUserById(enrollment.getUserId());
            // Add
            studentList.add(student);
        }

        List<Map<String, Object>> filteredCoursesAllStudent = new ArrayList<>();
        for (USERS student : studentList) {
            TEACHER_Service teacherService = new TEACHER_Service();
            List<Map<String, Object>> filteredCourses = teacherService.filteredCoursesByLessons(List.of(courseObj) ,student.getID());
            for (Map<String, Object> i : filteredCourses) {
                filteredCoursesAllStudent.add(i);
            }
        }

        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            if (sort.contains("score")) {
                if (sort.contains("asc")) {
                    filteredCoursesAllStudent.sort(
                    (a, b) -> Double.compare((Double) a.get("score"), (Double) b.get("score"))
                    );
                } else if (sort.contains("desc")) {
                    filteredCoursesAllStudent.sort(
                    (a, b) -> Double.compare((Double) b.get("score"), (Double) a.get("score"))
                    );
                } else {
                    // No sorting specified, do nothing
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new Gson().toJson(filteredCoursesAllStudent));
    }

    private void getTopStudentByLessonScore(HttpServletRequest request, HttpServletResponse response, int teacherID) throws IOException {
        int lessonID = Integer.parseInt(request.getParameter("lessonID"));
        // Get all students who have enrolled in the course of the lesson
        // LESSONS_ID -> COURSES_ID -> ENROLLS -> STUDENTS -> LESSONS
        LESSONS_Service lessonService = new LESSONS_Service();
        LESSONS lessonObj = lessonService.getLessonById(lessonID);

        COURSES_Service courseService = new COURSES_Service();
        COURSES courseObj = courseService.getCourseById(lessonObj.getCourseID());

        // Filter courseList created by teacher
        if (courseObj.getCreatedByUserID() != teacherID) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"message\": \"You are not the owner of this course\"}");
            return;
        }

        ENROLLS_Service ENROLLService = new ENROLLS_Service();
        List<ENROLLS> enrollmentList = ENROLLService.getENROLLSByCourseId(courseObj.getID());

        List<USERS> studentList = new ArrayList<>();
        for (ENROLLS enrollment : enrollmentList) {
            // Get student details by ID
            USERS_Service userService = new USERS_Service();
            USERS student = userService.getUserById(enrollment.getUserId());
            // Add
            studentList.add(student);
        }

        List<Map<String, Object>> filteredLessonsAllStudent = new ArrayList<>();
        for (USERS student : studentList) {
            TEACHER_Service teacherService = new TEACHER_Service();
            List<Map<String, Object>> filteredLessons = teacherService.filteredLessonsByQuizzesAnswer(List.of(lessonObj), student.getID());
            filteredLessonsAllStudent.addAll(filteredLessons);
        }

        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            if (sort.contains("score")) {
                if (sort.contains("asc")) {
                    filteredLessonsAllStudent.sort(
                    (a, b) -> Double.compare((Double) a.get("score"), (Double) b.get("score"))
                    );
                } else if (sort.contains("desc")) {
                    filteredLessonsAllStudent.sort(
                    (a, b) -> Double.compare((Double) b.get("score"), (Double) a.get("score"))
                    );
                } else {
                    // No sorting specified, do nothing
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new Gson().toJson(filteredLessonsAllStudent));
    }

    private void getTopStudentByQuizScore(HttpServletRequest request, HttpServletResponse response, int teacherID) throws IOException {
        int quizID = Integer.parseInt(request.getParameter("quizID"));

        // Get all students who have taken the quiz
        // QUIZZES_ID -> LESSONS_ID -> COURSES_ID -> ENROLLS -> STUDENTS -> QUIZZES
        QUIZZES_Service quizService = new QUIZZES_Service();
        QUIZZES quizObj = quizService.getQuizById(quizID);

        LESSONS_Service lessonService = new LESSONS_Service();
        LESSONS lessonObj = lessonService.getLessonById(quizObj.getLessonID());

        COURSES_Service courseService = new COURSES_Service();
        COURSES courseObj = courseService.getCourseById(lessonObj.getCourseID());

        // Filter courseList created by teacher
        if (courseObj.getCreatedByUserID() != teacherID) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"message\": \"You are not the owner of this course\"}");
            return;
        }

        ENROLLS_Service ENROLLService = new ENROLLS_Service();
        List<ENROLLS> enrollmentList = ENROLLService.getENROLLSByCourseId(courseObj.getID());

        List<USERS> studentList = new ArrayList<>();
        for (ENROLLS enrollment : enrollmentList) {
            // Get student details by ID
            USERS_Service userService = new USERS_Service();
            USERS student = userService.getUserById(enrollment.getUserId());
            studentList.add(student);
        }

        List<Map<String, Object>> filteredQuizzesAllStudent = new ArrayList<>();
        for (USERS student : studentList) {
            TEACHER_Service teacherService = new TEACHER_Service();
            List<Map<String, Object>> filteredQuizzes = teacherService.filteredQuizzesByAnswer(List.of(quizObj), student.getID());
            filteredQuizzesAllStudent.addAll(filteredQuizzes);
        }

        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            if (sort.contains("score")) {
                if (sort.contains("asc")) {
                    filteredQuizzesAllStudent.sort(
                    (a, b) -> Double.compare(
                        ((Number) a.get("score")).doubleValue(), 
                        ((Number) b.get("score")).doubleValue()
                    )
                    );
                } else if (sort.contains("desc")) {
                    filteredQuizzesAllStudent.sort(
                    (a, b) -> Double.compare(
                        ((Number) b.get("score")).doubleValue(), 
                        ((Number) a.get("score")).doubleValue()
                    )
                    );
                } else {
                    // No sorting specified, do nothing
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new Gson().toJson(filteredQuizzesAllStudent));
    }

    private void getTopCourseByAvgStudentScore(HttpServletRequest request, HttpServletResponse response, int teacherID) throws IOException {
        //TeacherID -> COURSES -> ENROLLS -> STUDENTS -> LESSONS -> QUIZZES -> AVG_SCORE
        List<Map<String, Object>> results = new ArrayList<>();
        COURSES_Service courseService = new COURSES_Service();
        List<COURSES> courses = courseService.getCoursesByCreatedByUserID(teacherID);
        if (courses.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"message\": \"No courses found for this teacher\"}");
            return;
        }
        for(COURSES course : courses) {
            // Get all students who have enrolled in the course
            ENROLLS_Service ENROLLSService = new ENROLLS_Service();
            List<ENROLLS> enrollList = ENROLLSService.getENROLLSByCourseId(course.getID());

            List<Integer> studentIDList = new ArrayList<>();
            for( ENROLLS enrollment : enrollList) {
                // Get student details by ID
                USERS_Service userService = new USERS_Service();
                USERS student = userService.getUserById(enrollment.getUserId());
                if (student != null && !studentIDList.contains(student.getID())) {
                    studentIDList.add(student.getID());
                }
            }

            // Calculate average score for the course
            List<List<Map<String,Object>>> filteredCourses = new ArrayList<>();
            for(int studentID : studentIDList) {
                filteredCourses.add(
                    new TEACHER_Service().filteredCoursesByLessons(List.of(course), studentID)
                );
            }
            
            double totalScore = 0.0;
            int count = 0;
            for(List<Map<String, Object>> courseList : filteredCourses) {
                for(Map<String, Object> courseData : courseList){
                    if(courseData != null && courseData.containsKey("status") && "completed".equals(courseData.get("status"))) {
                        Object scoreObj = courseData.get("score");
                        if (scoreObj instanceof Number) {
                            totalScore += ((Number) scoreObj).doubleValue();
                            count++;
                        }
                    }
                }
                double averageScore = count > 0 ? totalScore / count : 0.0;
                averageScore = Math.round(averageScore * 100.0) / 100.0; // Round to 2 decimal places
                results.add(Map.of(
                    "course", courseList.get(0).get("course"),
                    "averageScore", averageScore,
                    "studentCount", count
                ));
            }
            
        }

        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            if (sort.contains("averageScore")) {
                if (sort.contains("asc")) {
                    results.sort((a, b) -> Double.compare((Double) a.get("averageScore"), (Double) b.get("averageScore")));
                } else if (sort.contains("desc")) {
                    results.sort((a, b) -> Double.compare((Double) b.get("averageScore"), (Double) a.get("averageScore")));
                } else {
                    // No sorting specified, sort descending by default
                    results.sort((a, b) -> Double.compare((Double) b.get("averageScore"), (Double) a.get("averageScore")));
                }
            } else if (sort.contains("studentCount")) {
                if (sort.contains("asc")) {
                    results.sort((a, b) -> Integer.compare((Integer) a.get("studentCount"), (Integer) b.get("studentCount")));
                } else if (sort.contains("desc")) {
                    results.sort((a, b) -> Integer.compare((Integer) b.get("studentCount"), (Integer) a.get("studentCount")));
                } else {
                    // No sorting specified, sort descending by default
                    results.sort((a, b) -> Integer.compare((Integer) b.get("studentCount"), (Integer) a.get("studentCount")));
                }
            } else {
                // No sorting specified, do nothing
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(new Gson().toJson(results));
    }
}
