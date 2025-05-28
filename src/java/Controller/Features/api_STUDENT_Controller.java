/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Features;
import Model.ANSWERS;
import Model.COURSES;
import Model.ENROLLS;
import Model.LESSONS;
import Model.QUIZZES;
import Model.STUDIES;
import Service.ANSWERS_Service;
import Service.COURSES_Service;
import Service.COURSES_USERS_BANNED_Service;
import Service.ENROLLS_Service;
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
public class api_STUDENT_Controller extends HttpServlet {


    private final Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String path = request.getPathInfo();
        if (jwt.validateToken(request, response)) {
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
                        ENROLLS_Service ENROLLService = new ENROLLS_Service();
                        ENROLLS enrollment = ENROLLService.getEnrollmentByUserIdAndCourseId(userID, courseID);
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
                        // Get all courses with filtering userID on ENROLLS
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
                            // Get all lessons from the ENROLLS
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

                        // Get all lessons from the ENROLLS
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

                        // Get all lessons from the ENROLLS
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

                        // Get all lessons from the ENROLLS
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if (jwt.validateToken(request, response)) {
            try {
                int userID = (int) request.getAttribute("id");
                String doPath = request.getPathInfo();
                if (doPath.equals("/enrolls")) {
                    int courseID = Integer.parseInt(request.getParameter("courseID"));
                    // Validate input
                    if (courseID <= 0) {
                        throw new Exception("Missing or invalid courseID");
                    }

                    ENROLLS enrollment = new ENROLLS();
                    enrollment.setUserId(userID);
                    enrollment.setCourseId(courseID);
                    ENROLLS_Service service = new ENROLLS_Service();
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

                    //check if the user is banned from the course of the quiz
                    COURSES_USERS_BANNED_Service bannedService = new COURSES_USERS_BANNED_Service();
                    List<Integer> bannedUserIDs = bannedService.getAllBannedUsersFromCourse(course.getID());
                    if (bannedUserIDs.contains(userID)) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("{\"error\":\"User is banned from the course of this quiz\"}");
                        return;
                    }

                    ENROLLS_Service ENROLLService = new ENROLLS_Service();
                    ENROLLS enrollment = ENROLLService.getEnrollmentByUserIdAndCourseId(userID, course.getID());
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
            if (jwt.validateToken(request, response)) {
                try {
                    String path = request.getPathInfo();
                    if ("/enrolls".equals(path)) {
                    int userID = (int) request.getAttribute("id");

                    // Parse data from request body (x-www-form-urlencoded)
                    Map<String, String> params = parseForm.parseFormUrlEncoded(request);

                    int courseId = Integer.parseInt(params.get("courseID"));
                    String feedback = params.get("feedbackEnrollment");
                    int rating = Integer.parseInt(params.get("rating"));

                    System.out.println("userID: " + userID);
                    System.out.println("courseId: " + courseId);
                    System.out.println("feedback: " + feedback);
                    System.out.println("rating: " + rating);

                    // Validate input
                    if (courseId <= 0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\":\"Invalid userId, courseId\"}");
                        return;
                    }

                    // Create enrollment object with updated information
                    ENROLLS enrollment = new ENROLLS();
                    enrollment.setUserId(userID);
                    enrollment.setCourseId(courseId);
                    enrollment.setFeedbackEnrollment(feedback);
                    enrollment.setRating(rating);

                    // Call service to update
                    ENROLLS_Service service = new ENROLLS_Service();
                    service.updateEnrollment(enrollment);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(new Gson().toJson(enrollment));
                    } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\":\"Unknown endpoint: " + path + "\"}");
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
}
