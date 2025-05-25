/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;
import Model.ANSWERS;
import Model.COURSES;
import Model.ENROLLMENTS;
import Model.LESSONS;
import Model.QUIZZES;
import Model.STUDIES;
import Service.ANSWERS_Service;
import Service.COURSES_Service;
import Service.ENROLLMENTS_Service;
import Service.Features.STUDENT_Service;
import Service.LESSONS_Service;
import Service.QUIZZES_Service;
import Service.STUDIED_Service;
import Utils.jwt;
import Utils.parseForm;
import com.google.gson.Gson;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import java.util.Map;
//import Service.ANSWER_Service;
//import Service.STUDIES_Service;
//import java.util.List;
/**
 *
 * @author 22521
 */
@WebServlet(name = "api_FEATURE_STUDENT_Controller", urlPatterns = {"/api/student/*"})
public class api_FEATURE_STUDENT_Controller extends HttpServlet {


    private final Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String path = request.getPathInfo();
        if (jwt.validateToken(request)) {
            int userID = (int) request.getAttribute("id");
            try {
                if (path == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\"Missing endpoint path \"}");
                    return;
                }

                if (path.equals("/courses")) {
                    if (request.getParameter("id") != null) {
                        // get course of the users by courseID
                        int courseID = Integer.parseInt(request.getParameter("id"));
                        COURSES_Service courseService = new COURSES_Service();
                        COURSES course = courseService.getCourseById(courseID);

                        // check if the user is enrolled in the course
                        ENROLLMENTS_Service enrollmentService = new ENROLLMENTS_Service();
                        ENROLLMENTS enrollment = enrollmentService.getEnrollmentByUserIdAndCourseId(userID, courseID);
                        if (enrollment != null) {
                            List<Map<String, Object>> filteredCourses = new STUDENT_Service().filteredCoursesByLessons(List.of(course), userID);
                            if (filteredCourses.isEmpty()) {
                                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                                response.getWriter().write("{\"message\": \"No lessons found in this course for the user.\"}");
                            } else {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write(new Gson().toJson(filteredCourses));
                            }
                        } else {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"message\": \"User still not enroll this course.\"}");
                        }
                    } else {
                        // Get all courses with filtering userID on ENROLLMENTS
                        COURSES_Service courseService = new COURSES_Service();
                        List<COURSES> courseList = courseService.getCoursesByUserId(userID);

                        if (courseList.isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                            response.getWriter().write("{\"message\": \"No courses found for the user.\"}");
                        } else {
                            List<Map<String, Object>> filteredCourses = new STUDENT_Service().filteredCoursesByLessons(courseList, userID);
                            if (filteredCourses.isEmpty()) {
                                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                                response.getWriter().write("{\"message\": \"No lessons found in the courses for the user.\"}");
                            } else {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write(new Gson().toJson(filteredCourses));
                            }
                        }

                    }

                } else if (path.equals("/lessons")) {
                    if (request.getParameter("courseID") != null) {
                        // Get all LESSONS of the user by courseID
                        // USERID -> COURSES_LIST -> LESSONS_LIST
                        // Get all courses the user is enrolled in
                        int courseID = Integer.parseInt(request.getParameter("courseID"));
                        COURSES_Service courseService = new COURSES_Service();
                        List<COURSES> courseList = courseService.getCoursesByUserId(userID);

                        // Filter courses by courseID
                        courseList.removeIf(course -> course.getID() != courseID);

                        if (courseList.isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                            response.getWriter().write("{\"message\": \"No courses found for the user.\"}");
                        } else {
                            // Get all lessons from the enrollments
                            LESSONS_Service lessonService = new LESSONS_Service();
                            List<LESSONS> lessonList = lessonService.getLessonByCoursesList(courseList);

                            // Filter lessons based on uses's quiz answers
                            List<Map<String, Object>> filteredLessons = new STUDENT_Service().filteredLessonsByQuizzesAnswer(lessonList, userID);

                            if( filteredLessons.isEmpty()) {
                                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                                response.getWriter().write("{\"message\": \"No lessons found in this course for the user.\"}");
                            } else {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write(new Gson().toJson(filteredLessons));
                            }
                        } 
                    } else {
                        // Get all lessons of the user
                        // USERID -> COURSES_LIST -> LESSONS_LIST
                        // Get all courses the user is enrolled in
                        COURSES_Service courseService = new COURSES_Service();
                        List<COURSES> courseList = courseService.getCoursesByUserId(userID);

                        // Get all lessons from the enrollments
                        LESSONS_Service lessonService = new LESSONS_Service();
                        List<LESSONS> lessonList = lessonService.getLessonByCoursesList(courseList);

                        // Filter lessons based on user's quiz answers
                        List<Map<String, Object>> filteredLessons = new STUDENT_Service().filteredLessonsByQuizzesAnswer(lessonList, userID);

                        if (filteredLessons.isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                            response.getWriter().write("{\"message\": \"No lessons found for the user.\"}");
                        } else {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write(new Gson().toJson(filteredLessons));
                        }
                    }
                } else if (path.equals("/quizzes")) {
                    if (request.getParameter("lessonID") != null) {
                        // Get all QUIZZES of the user by lessonID
                        // USERID -> COURSES_LIST -> LESSONS_LIST -> QUIZZES_LIST
                        // Get all courses the user is enrolled in
                        int lessonID = Integer.parseInt(request.getParameter("lessonID"));
                        COURSES_Service courseService = new COURSES_Service();
                        List<COURSES> courseList = courseService.getCoursesByUserId(userID);

                        // Get all lessons from the enrollments
                        LESSONS_Service lessonService = new LESSONS_Service();
                        List<LESSONS> lessonList = lessonService.getLessonByCoursesList(courseList);

                        // Filter lessons by lessonID
                        lessonList.removeIf(lesson -> lesson.getID() != lessonID);

                        // Get all quizzes from the lessons
                        QUIZZES_Service quizService = new QUIZZES_Service();
                        List<QUIZZES> quizList = quizService.getQuizByLessonsList(lessonList);

                        // Filter quizzes based on user answers
                        List<Map<String, Object>> filteredQuizzes = new STUDENT_Service().filteredQuizzesByAnswer(quizList, userID);
                        
                        if( filteredQuizzes.isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                            response.getWriter().write("{\"message\": \"No quizzes found in this lesson for the user.\"}");
                        } else {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write(new Gson().toJson(filteredQuizzes));
                        }
                    } else {
                        // Get all QUIZZES of the user
                        // USERID -> COURSES_LIST -> LESSONS_LIST -> QUIZZES_LIST
                        // Get all courses the user is enrolled in
                        COURSES_Service courseService = new COURSES_Service();
                        List<COURSES> courseList = courseService.getCoursesByUserId(userID);

                        // Get all lessons from the enrollments
                        LESSONS_Service lessonService = new LESSONS_Service();
                        List<LESSONS> lessonList = lessonService.getLessonByCoursesList(courseList);

                        // Get all quizzes from the lessons
                        QUIZZES_Service quizService = new QUIZZES_Service();
                        List<QUIZZES> quizList = quizService.getQuizByLessonsList(lessonList);

                        // Filter quizzes based on user answers
                        List<Map<String, Object>> filteredQuizzes = new STUDENT_Service().filteredQuizzesByAnswer(quizList, userID);

                        if (filteredQuizzes.isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                            response.getWriter().write("{\"message\": \"No quizzes found for the user.\"}");
                        } else {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write(new Gson().toJson(filteredQuizzes));
                        }
                    }
                } else if (path.equals("/quizzesWithScore")) {
                    String lessonParam = request.getParameter("lessonID");

                    if (lessonParam != null) {
                        // Case with lessonID: return quiz results
                        int lessonID = Integer.parseInt(lessonParam);
                        ANSWERS_Service answerService = new ANSWERS_Service();
                        Map<String, Object> scoreData = answerService.getStudentLessonScores(lessonID);
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(scoreData));
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\"Unknown endpoint: " + path + "\"}");
                }

            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\"Invalid userID or lessonID\"}");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\"" + e.getMessage() + "\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
//        throws ServletException, IOException {
//    response.setContentType("application/json;charset=UTF-8");
//      String path = request.getPathInfo();
//      try {
//        if (path == null) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("{\"error\":\"Missing endpoint path \"}");
//            return;
//        }
//
//        if (path.equals("/courses")) {
//            if (request.getParameter("id") != null) {
//                // Get course by ID
//                int id = Integer.parseInt(request.getParameter("id"));
//
//                COURSES_Service courseService = new COURSES_Service();
//                COURSES course = courseService.getCourseById(id);
//
//                response.setStatus(HttpServletResponse.SC_OK);
//                response.getWriter().write(new Gson().toJson(course));
//            } else {
//                // Get all courses
//                COURSES_Service courseService = new COURSES_Service();
//                List<COURSES> courseList = courseService.getAllCourses();
//
//                response.setStatus(HttpServletResponse.SC_OK);
//                response.getWriter().write(new Gson().toJson(courseList));
//            }
//
//        } else if (path.equals("/lessons")) {
//           if(request.getParameter("courseID") != null ) {
//                int courseID = Integer.parseInt(request.getParameter("courseID"));
//                LESSONS_Service lessonService = new LESSONS_Service();
//                List<LESSONS> lessons = lessonService.getLessonByCourseID(courseID);
//
//                response.setStatus(HttpServletResponse.SC_OK);
//                response.getWriter().write(new Gson().toJson(lessons));
//
//        }}
//        else if (path.equals("/quizzes")) {
//            if(request.getParameter("lessonID") != null ) {
//                // Get all lessons
//                int lessonID = Integer.parseInt(request.getParameter("lessonID"));
//                QUIZZES_Service quizService = new QUIZZES_Service();
//                List<QUIZZES> quiz = quizService.getLessonByQuizID(lessonID);
//
//                response.setStatus(HttpServletResponse.SC_OK);
//                response.getWriter().write(new Gson().toJson(quiz));            
//            }
//        }
//        else if (path.equals("/quizzesWithScore")) {
//               String lessonParam = request.getParameter("lessonID");
//
//
//            if (lessonParam != null) {
//                // Trường hợp có lessonID: trả kết quả quiz
//                int lessonID = Integer.parseInt(lessonParam);
//                ANSWER_Service answerService = new ANSWER_Service();
//                Map<String, Object> scoreData = answerService.getStudentLessonScores(lessonID);
//                response.setStatus(HttpServletResponse.SC_OK);
//                response.getWriter().write(new Gson().toJson(scoreData));
//
//            }
//          }
//        else {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            response.getWriter().write("{\"error\":\"Unknown endpoint: " + path + "\"}");
//        }
//
//    } catch (NumberFormatException e) {
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        response.getWriter().write("{\"error\":\"Invalid userID or lessonID\"}");
//    } catch (Exception e) {
//        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
//    }
//
//    }   
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if (jwt.validateToken(request)) {
            try {
                int userID = (int) request.getAttribute("id");
                String doPath = request.getPathInfo();
                if (doPath.equals("/enrollments")) {
                    int courseID = Integer.parseInt(request.getParameter("courseID"));
                    // Validate input
                    if (courseID <= 0) {
                        throw new Exception("Missing or invalid courseID");
                    }

                    ENROLLMENTS enrollment = new ENROLLMENTS();
                    enrollment.setUserId(userID);
                    enrollment.setCourseId(courseID);
                    ENROLLMENTS_Service service = new ENROLLMENTS_Service();
                    service.createEnrollment(enrollment);

                    // Return result
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.getWriter().write(new Gson().toJson(enrollment));

                } else if (doPath.equals("/answers")) {
                    int quizID = Integer.parseInt(request.getParameter("quizID"));
                    int answerValue = Integer.parseInt(request.getParameter("answer"));

                    
                    // Validate input
                    if (userID <= 0 || quizID <= 0 || answerValue >= 5 || answerValue <= 0) {
                        throw new Exception("Missing or invalid userId, quizID or answer");
                    }

                    // check if the quiz exists
                    QUIZZES_Service quizService = new QUIZZES_Service();
                    QUIZZES quiz = quizService.getQuizById(quizID);
                    if (quiz == null) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\":\"Quiz not found\"}");
                        return;
                    }

                    // check if the user are in the course of the lesson of the quiz
                    LESSONS_Service lessonService = new LESSONS_Service();
                    LESSONS lesson = lessonService.getLessonById(quiz.getLessonID());
                    COURSES_Service courseService = new COURSES_Service();
                    COURSES course = courseService.getCourseById(lesson.getCourseID());
                    ENROLLMENTS_Service enrollmentService = new ENROLLMENTS_Service();
                    ENROLLMENTS enrollment = enrollmentService.getEnrollmentByUserIdAndCourseId(userID, course.getID());
                    if (enrollment == null) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("{\"error\":\"User is not in the course of this quiz\"}");
                        return;
                    }

                    ANSWERS newAnswer = new ANSWERS();
                    newAnswer.setUserId(userID);
                    newAnswer.setQuizId(quizID);
                    newAnswer.setAnswer(answerValue);

                    ANSWERS_Service answerService = new ANSWERS_Service();
                    answerService.createAnswer(newAnswer);

                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.getWriter().write(new Gson().toJson(newAnswer));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\":\"Unknown endpoint: " + doPath + "\"}");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
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
     if (jwt.validateToken(request)) {
        try {
                int userID = (int) request.getAttribute("id");
    
        // Parse dữ liệu từ request body (x-www-form-urlencoded)
            Map<String, String> params = parseForm.parseFormUrlEncoded(request);

            int courseId = Integer.parseInt(params.get("courseId"));
            String feedback = params.get("feedbackEnrollment");

            // Validate input
            if (courseId <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid userId, courseId\"}");
                return;
            }

            // Tạo đối tượng enrollment với thông tin cập nhật
            ENROLLMENTS enrollment = new ENROLLMENTS();
            enrollment.setUserId(userID);
            enrollment.setCourseId(courseId);
            enrollment.setFeedbackEnrollment(feedback);

            // Gọi service để cập nhật
            ENROLLMENTS_Service service = new ENROLLMENTS_Service();
            service.updateEnrollment(enrollment);

            // Giả sử bạn có hàm getEnrollmentById để lấy lại bản ghi đã cập nhật
            ENROLLMENTS updated = service.getEnrollmentByUserIdAndCourseId(userID, courseId);


                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(updated));


        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid number format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }}
}



}
