package Controller;

import Service.QUIZZES_Service;
import Model.QUIZZES;
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

@WebServlet(name = "api_QUIZZES_Controller", urlPatterns = {"/api/quizzes"})
public class api_QUIZZES_Controller extends HttpServlet {

    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                // get quiz by ID
                int id = Integer.parseInt(idParam);
                QUIZZES_Service quizService = new QUIZZES_Service();
                QUIZZES lesson = quizService.getQuizById(id);
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(lesson));
            } 
            else if(request.getParameter("lessonID") != null ) {
                
                int lessonID = Integer.parseInt(request.getParameter("lessonID"));
                QUIZZES_Service quizService = new QUIZZES_Service();
                List<QUIZZES> quiz = quizService.getQuizByLessonID(lessonID);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(quiz));
                
            }        
            
            else {
                // get all quizzes
                QUIZZES_Service QUIZZES_Service = new QUIZZES_Service();
                List<QUIZZES> lessonList = QUIZZES_Service.getAllQuizzes();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(lessonList));
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
        try{
            int lessonId = Integer.parseInt(request.getParameter("lessonId"));
            String title = request.getParameter("title");
            String question = request.getParameter("question");
            int correctAnswer = Integer.parseInt(request.getParameter("correctAnswer"));

            // Validate input
            if (title == null || question == null) {
                throw new Exception("Missing required fields");
            }

            // Create quiz
            QUIZZES quiz = new QUIZZES(lessonId, title, question, correctAnswer);
            QUIZZES_Service quizService = new QUIZZES_Service();
            quizService.createQuiz(quiz);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(new Gson().toJson(quiz));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
        protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try{
            // Manually parse form data from body (x-www-form-urlencoded)
            Map<String, String> params = parseForm.parseFormUrlEncoded(request);
            
            System.out.println("params: " + params);

            int id = Integer.parseInt(params.get("id"));
            int lessonID = Integer.parseInt(request.getParameter("lessonID"));
            String title = request.getParameter("title");
            String question = request.getParameter("question");
            int correctAnswer = Integer.parseInt(request.getParameter("correctAnswer"));

            // Validate data
            if (title == null || question == null) {
                throw new Exception("Missing required fields");
            }

            // Update quiz
            QUIZZES quiz = new QUIZZES(lessonID, title, question, correctAnswer);
            QUIZZES_Service quizService = new QUIZZES_Service();
            quizService.updateQuiz(quiz);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new Gson().toJson(quiz));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
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

            int quizId = Integer.parseInt(idParam); // Có thể gây NumberFormatException
            QUIZZES_Service quizService = new QUIZZES_Service();
            QUIZZES quiz = quizService.getQuizById(quizId); //store the quiz before deleting
            quizService.deleteQuiz(quizId); // Giả sử phương thức cần int courseId

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Lesson deleted successfully\", \"quiz\":" + new Gson().toJson(quiz) + "}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}
