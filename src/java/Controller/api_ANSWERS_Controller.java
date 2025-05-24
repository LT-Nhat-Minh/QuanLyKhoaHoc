package Controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import Model.ANSWERS;
import Service.ANSWERS_Service;

/**
 *
 * @author minhk
 */
@WebServlet(name = "api_ANSWERS_Controller", urlPatterns = {"/api/answers"})
public class api_ANSWERS_Controller extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json;charset=UTF-8");
                try {
                        List<ANSWERS> answersList = new ArrayList<>();
                        ANSWERS_Service answersService = new ANSWERS_Service();

                        if (request.getParameter("userId") != null && request.getParameter("quizId") != null) {
                                // Get answers by userId and quizId
                                int userId = Integer.parseInt(request.getParameter("userId"));
                                int quizId = Integer.parseInt(request.getParameter("quizId"));
                                answersList.add(answersService.getAnswerByUserIdAndQuizId(userId, quizId));

                        } else if (request.getParameter("quizId") != null) {
                                // Get all answers for a specific quiz
                                int quizId = Integer.parseInt(request.getParameter("quizId"));
                                answersList = answersService.getAnswersByQuizId(quizId);

                        } else if (request.getParameter("userId") != null) {
                                // Get all answers for a specific user
                                int userId = Integer.parseInt(request.getParameter("userId"));
                                answersList = answersService.getAnswersByUserId(userId);

                        } else {
                                // Get all answers
                                answersList = answersService.getAllAnswers();
                        }
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(answersList));

                } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                }
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                // Implementation for POST request
        }
}
