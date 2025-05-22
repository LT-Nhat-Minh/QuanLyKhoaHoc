package Service;

import Config.DBConnection;
import Model.QUIZZES;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QUIZZES_Service {

    public List<QUIZZES> getAllQuizzes() {
        List<QUIZZES> quizList = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM QUIZZES";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                QUIZZES quiz = new QUIZZES();
                quiz.setID(result.getInt("id"));
                quiz.setLessonID(result.getInt("lessonID"));
                quiz.setTitle(result.getString("title"));
                quiz.setQuestion(result.getString("question"));
                quizList.add(quiz);
            }

            result.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return quizList;
    }

    public QUIZZES getQuizById(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM QUIZZES WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                QUIZZES quiz = new QUIZZES();
                quiz.setID(result.getInt("id"));
                quiz.setLessonID(result.getInt("lessonID"));
                quiz.setTitle(result.getString("title"));
                quiz.setQuestion(result.getString("question"));
                return quiz;
            }

            result.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public void createQuiz(QUIZZES quiz) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO QUIZZES (lessonID, title, question) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quiz.getLessonID());
            pstmt.setString(2, quiz.getTitle());
            pstmt.setString(3, quiz.getQuestion());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateQuiz(QUIZZES quiz) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE QUIZZES SET getLessonID = ?, title = ?, question = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quiz.getLessonID());
            pstmt.setString(2, quiz.getTitle());
            pstmt.setString(3, quiz.getQuestion());
            pstmt.setInt(4, quiz.getID());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteQuiz(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM QUIZZES WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
