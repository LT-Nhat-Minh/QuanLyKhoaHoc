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
import Model.STUDIED;
import Service.ANSWER_Service;
import Service.COURSES_Service;
import Service.ENROLLMENTS_Service;
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
                        // Get course by ID
                        int id = Integer.parseInt(request.getParameter("id"));

                        COURSES_Service courseService = new COURSES_Service();
                        COURSES course = courseService.getCourseById(id);
                        
                        ENROLLMENTS_Service enrollmentService = new ENROLLMENTS_Service();
                        ENROLLMENTS enrollment = enrollmentService.getEnrollmentByUserIdAndCourseId(userID, id);

                        // Check if the user is enrolled in the course
                        if (enrollment == null) {
                            throw new Exception("User is not enrolled in this course");
                        }

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(course));
                    } else {
                        // Get all courses
                        COURSES_Service courseService = new COURSES_Service();
                        List<COURSES> courseList = courseService.getAllCourses();
                        List<COURSES> resultList = new ArrayList<>();

                        for(COURSES course : courseList){
                            ENROLLMENTS_Service enrollmentService = new ENROLLMENTS_Service();
                            ENROLLMENTS enrollment = enrollmentService.getEnrollmentByUserIdAndCourseId(userID, course.getID());
                            if(enrollment != null){
                                resultList.add(course);
                            }
                        }

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(resultList));
                    }

                } else if (path.equals("/lessons")) {
                    if (request.getParameter("courseID") != null) {
                        int courseID = Integer.parseInt(request.getParameter("courseID"));
                        LESSONS_Service lessonService = new LESSONS_Service();
                        List<LESSONS> lessons = lessonService.getLessonByCourseID(courseID);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(lessons));
                    }
                } else if (path.equals("/quizzes")) {
                    if (request.getParameter("lessonID") != null) {
                        // Get all quizzes
                        int lessonID = Integer.parseInt(request.getParameter("lessonID"));
                        QUIZZES_Service quizService = new QUIZZES_Service();
                        List<QUIZZES> quiz = quizService.getQuizByLessonID(lessonID);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(quiz));
                    }
                } else if (path.equals("/quizzesWithScore")) {
                    String lessonParam = request.getParameter("lessonID");

                    if (lessonParam != null) {
                        // Case with lessonID: return quiz results
                        int lessonID = Integer.parseInt(lessonParam);
                        ANSWER_Service answerService = new ANSWER_Service();
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
        try {
            String doPath = request.getPathInfo();
            if(doPath.equals("/enrollments")){
                int userId = Integer.parseInt(request.getParameter("userId"));
                int courseId = Integer.parseInt(request.getParameter("courseId"));
                    // Validate input
                    if (userId <= 0 || courseId <= 0) {
                        throw new Exception("Missing or invalid userId or courseId");
                    }

                    ENROLLMENTS enrollment = new ENROLLMENTS();
                    enrollment.setUserId(userId);
                    enrollment.setCourseId(courseId);
                    ENROLLMENTS_Service service = new ENROLLMENTS_Service();
                    service.createEnrollment(enrollment);

                    // Trả về kết quả
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.getWriter().write(new Gson().toJson(enrollment));
                
            }
            else if(doPath.equals("/answers")){
                int userId = Integer.parseInt(request.getParameter("userId"));
                int quizId = Integer.parseInt(request.getParameter("quizId"));
                int answerValue = Integer.parseInt(request.getParameter("answer"));
                

                if (userId <= 0 || quizId <= 0 || answerValue >= 5 || answerValue <= 0) {
                    throw new Exception("Missing or invalid userId, quizId or answer");
                }

                ANSWERS newAnswer = new ANSWERS();
                newAnswer.setUserId(userId);
                newAnswer.setQuizId(quizId);
                newAnswer.setAnswer(answerValue);
                
               
                ANSWER_Service answerService = new ANSWER_Service();
                answerService.createAnswer(newAnswer);
                
                QUIZZES_Service quizS = new QUIZZES_Service();
                QUIZZES quizObj = quizS.getQuizById(newAnswer.getQuizId());
                
                List<QUIZZES> quizList = quizS.getQuizByLessonID(quizObj.getLessonID());
                
                boolean isLessonCompleted = true;
                for(QUIZZES q : quizList ){
                    if (answerService.getAnswerByQuizId(q.getID()) == null ) {
                        isLessonCompleted = false;
                        break;
                    }
                }
                
                if(!isLessonCompleted){
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.getWriter().write(new Gson().toJson(newAnswer));
                }
                
                else{
                    STUDIED_Service stuS = new STUDIED_Service();
                    
                    LESSONS_Service lessonS = new LESSONS_Service();
                    LESSONS lessonObj = lessonS.getLessonById(quizObj.getLessonID());
                    
                    List<LESSONS> lessonList = lessonS.getLessonByCourseID(lessonObj.getCourseID());
                    boolean isCourseCompleted = true;
                    for(LESSONS i : lessonList ){
                        STUDIED studiedObj = stuS.getStudiedByLessonId(i.getID());
                        
                        if (studiedObj.getIsCompleted() != 1 ) {
                            isCourseCompleted = false;
                            break;
                        }
                    }

                    if(!isCourseCompleted){
                        response.setStatus(HttpServletResponse.SC_CREATED);
                        response.getWriter().write(new Gson().toJson(newAnswer));
                    }
                    else{
                        
                        ENROLLMENTS_Service enrollS = new ENROLLMENTS_Service();
                        ENROLLMENTS enrollObj = enrollS.getEnrollmentByUserIdAndCourseId(userId, lessonObj.getCourseID());
                        
                        enrollObj.setStatusID(2);
                        
                        enrollS.updateEnrollment(enrollObj);
                        response.setStatus(HttpServletResponse.SC_CREATED);
                        response.getWriter().write(new Gson().toJson(newAnswer));
                        
                    }
                    
                    
                }
                
                
               
                // Trả về toàn bộ đối tượng answer đã tạo
               
        }
            
            
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
}
    
    
   @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {

    response.setContentType("application/json;charset=UTF-8");

    try {
        // Parse dữ liệu từ request body (x-www-form-urlencoded)
        Map<String, String> params = parseForm.parseFormUrlEncoded(request);

        int userId = Integer.parseInt(params.get("userId"));
        int courseId = Integer.parseInt(params.get("courseId"));
        String feedback = params.get("feedbackEnrollment");

        // Validate input
        if (userId <= 0 || courseId <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid userId, courseId\"}");
            return;
        }

        // Tạo đối tượng enrollment với thông tin cập nhật
        ENROLLMENTS enrollment = new ENROLLMENTS();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setFeedbackEnrollment(feedback);

        // Gọi service để cập nhật
        ENROLLMENTS_Service service = new ENROLLMENTS_Service();
        service.updateEnrollment(enrollment);

        // Giả sử bạn có hàm getEnrollmentById để lấy lại bản ghi đã cập nhật
        ENROLLMENTS updated = service.getEnrollmentByUserIdAndCourseId(userId, courseId);

      
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new Gson().toJson(updated));
        

    } catch (NumberFormatException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"error\":\"Invalid number format\"}");
    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
    }
}



}
