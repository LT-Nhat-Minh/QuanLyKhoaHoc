package Service;

import Config.DBConnection;
import Model.ANSWERS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ANSWER_Service {

    public List<ANSWERS> getAllAnswer() {
        List<ANSWERS> scoreList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ANSWER";
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
    
        public ANSWERS getAnswerByQuizId(int quizId) {
        ANSWERS answerObj = null;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ANSWER WHERE quizId = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quizId);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                
                answerObj.setUserId(result.getInt("userId"));
                answerObj.setQuizId(result.getInt("quizId"));
                answerObj.setAnswer(result.getInt("answer"));
                answerObj.setUpdatedAt(result.getString("updatedAt"));
                
            }

            result.close();
            pstmt.close();
            conn.close();

            return answerObj;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    

    

    public void createAnswer(ANSWERS answer) {
        try {
            Connection conn = DBConnection.getConnection();

            // Chèn dữ liệu, không truyền updatedAt vì SQL mặc định GETDATE()
            String sql = "INSERT INTO ANSWER (userId, quizId, answer) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, answer.getUserId());
            pstmt.setInt(2, answer.getQuizId());
            pstmt.setInt(3, answer.getAnswer());
            pstmt.executeUpdate();
            pstmt.close();

            // Truy vấn updatedAt mới nhất cho cặp userId, quizId
            String selectSql = "SELECT updatedAt FROM ANSWER WHERE userId = ? AND quizId = ? ORDER BY updatedAt DESC";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, answer.getUserId());
            selectStmt.setInt(2, answer.getQuizId());
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                answer.setUpdatedAt(rs.getString("updatedAt"));
            }

            rs.close();
            selectStmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
}

    
   public Map<String, Object> getStudentLessonScores(int lessonId) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> quizResults = new ArrayList<>();
        double totalScore = 0;
        int totalQuizzes = 0;

        try {
            Connection conn = DBConnection.getConnection();
            // 2. Truy vấn tất cả quiz và answer trong một lần
            String sql = "SELECT q.id AS quizId, q.title, q.question, q.correctAnswer, a.answer " +
                        "FROM QUIZZES q " +
                        "LEFT JOIN ANSWER a ON q.id = a.quizId " +
                        "WHERE q.lessonID = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, lessonId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int quizId = rs.getInt("quizId");
                    int correctAnswer = rs.getInt("correctAnswer");
                    Integer studentAnswer = rs.getObject("answer", Integer.class); // Có thể là null

                    // 3. Tính điểm (nếu học sinh đã nộp đáp án)
                    boolean isCompleted = (studentAnswer != null);
                    boolean isCorrect = isCompleted && (studentAnswer == correctAnswer);
                    double score = isCorrect ? 1.0 : 0.0;

                    // 4. Thêm vào kết quả
                    Map<String, Object> quizResult = new LinkedHashMap<>();
                    quizResult.put("quizId", quizId);
                    quizResult.put("quizTitle", rs.getString("title"));                    
                    quizResult.put("quizQuestion", rs.getString("question"));
                    quizResult.put("correctAnswer", isCompleted ? correctAnswer: "Complete quiz to see correctAnswer");
                    quizResult.put("studentAnswer", isCompleted ? studentAnswer : "null");
                    quizResults.add(quizResult);

                    if (isCompleted) {
                        totalScore += score;
                        totalQuizzes++;
                    }
                }
                
            result.put("totalScore", totalScore);
            result.put("quizzes", quizResults);
            result.put("totalQuizzes", quizResults.size()); // Tổng số quiz trong lesson
            result.put("completedQuizzes", totalQuizzes);  // Số quiz đã làm    
            result.put("correctQuiz", (int)totalScore);
            result.put("status", totalQuizzes > 0 ? "1" : "undefined");

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
        return result;
    }

    
}