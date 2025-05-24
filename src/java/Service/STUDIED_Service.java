package Service;

import Config.DBConnection;
import Model.STUDIES;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class STUDIED_Service {

    public List<STUDIES> getAllStudiedRecords() {
        List<STUDIES> studiedList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM STUDIES";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                STUDIES studied = new STUDIES();
                studied.setUserId(result.getInt("userId"));
                studied.setLessonId(result.getInt("lessonId"));
                studied.setCreatedAt(result.getTimestamp("createdAt"));
                studied.setUpdatedAt(result.getTimestamp("updatedAt"));
                studiedList.add(studied);
            }

            result.close();
            pstmt.close();
            conn.close();

            return studiedList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public STUDIES getStudiedRecordById(int id) {
        STUDIES studied = new STUDIES();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM STUDIES WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                studied.setUserId(result.getInt("userId"));
                studied.setLessonId(result.getInt("lessonId"));
                studied.setCreatedAt(result.getTimestamp("createdAt"));
                studied.setUpdatedAt(result.getTimestamp("updatedAt"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return studied;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public STUDIES getStudiedByLessonId(int lessonId) {
        STUDIES studied = null;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM STUDIES WHERE lessonId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, lessonId);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                studied.setUserId(result.getInt("userId"));
                studied.setLessonId(result.getInt("lessonId"));
                studied.setCreatedAt(result.getTimestamp("createdAt"));
                studied.setUpdatedAt(result.getTimestamp("updatedAt"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return studied;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void createStudiedRecord(STUDIES studied) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO STUDIES (userId, lessonId, createdAt, updatedAt) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, studied.getUserId());
            pstmt.setInt(2, studied.getLessonId());
            pstmt.setTimestamp(3, studied.getCreatedAt());
            pstmt.setTimestamp(4, studied.getUpdatedAt());
            pstmt.executeUpdate();

            String selectSql = "SELECT createdAt, updatedAt FROM STUDIES WHERE userId = ? AND lessonId = ?";
            PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
            selectPstmt.setInt(1, studied.getUserId());
            selectPstmt.setInt(2, studied.getLessonId());
            ResultSet result = selectPstmt.executeQuery();
            if (result.next()) {
                studied.setCreatedAt(result.getTimestamp("createdAt"));
                studied.setUpdatedAt(result.getTimestamp("updatedAt"));
            }

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void updateStudiedRecord(STUDIES studied) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE STUDIES SET userId = ?, lessonId = ?, createdAt = ?, updatedAt = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studied.getUserId());
            pstmt.setInt(2, studied.getLessonId());
            pstmt.setTimestamp(3, studied.getCreatedAt());
            pstmt.setTimestamp(4, studied.getUpdatedAt());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteStudiedRecord(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM STUDIES WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    
    public List<Map<String, Object>> getStudiedProgress(int userId) {
        List<Map<String, Object>> progressList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT lessonId, createdAt, updatedAt FROM STUDIES WHERE userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();  

            while (rs.next()) {
                Map<String, Object> progress = new LinkedHashMap<>();
                progress.put("lessonId", rs.getInt("lessonId"));
                progress.put("createdAt", rs.getTimestamp("createdAt"));
                progress.put("updatedAt", rs.getTimestamp("updatedAt"));
                progress.put("status", rs.getDouble("isCompleted") == 1.0 ? "1" : "undefined");

                progressList.add(progress);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
        return progressList;
}

}