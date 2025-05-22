package Service;

import Config.DBConnection;
import Model.ANSWERS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ANSWER_Service {

    public List<ANSWERS> getAllScores() {
        List<ANSWERS> scoreList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM SCORED";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                ANSWERS score = new ANSWERS();
                score.setUserId(result.getInt("userId"));
                score.setQuizId(result.getInt("quizId"));
                score.setAnswer(result.getInt("answer"));
                score.setUpdatedAt(result.getString("updatedAt"));
                scoreList.add(score);
            }

            result.close();
            pstmt.close();
            conn.close();

            return scoreList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    

    public void createScore(ANSWERS score) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO SCORED (userId, quizId, score, completionTime) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, score.getUserId());
            pstmt.setInt(2, score.getQuizId());
            pstmt.setDouble(3, score.getAnswer());
            pstmt.setString(4, score.getUpdatedAt());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void updateScore(ANSWERS score) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE SCORED SET userId = ?, quizId = ?, score = ?, completionTime = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, score.getUserId());
            pstmt.setInt(2, score.getQuizId());
            pstmt.setDouble(3, score.getAnswer());
            pstmt.setString(4, score.getUpdatedAt());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteScore(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM SCORED WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}