package Service;

import Config.DBConnection;
import Model.STUDIED;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class STUDIED_Service {

    public List<STUDIED> getAllStudiedRecords() {
        List<STUDIED> studiedList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM STUDIED";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                STUDIED studied = new STUDIED();
                studied.setUserId(result.getInt("userId"));
                studied.setLessonId(result.getInt("lessonId"));
                studied.setStudiedDate(result.getTimestamp("studiedDate"));
                studied.setCompleted(result.getDouble("isCompleted"));
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

    public STUDIED getStudiedRecordById(int id) {
        STUDIED studied = new STUDIED();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM STUDIED WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                studied.setUserId(result.getInt("userId"));
                studied.setLessonId(result.getInt("lessonId"));
                studied.setStudiedDate(result.getTimestamp("studiedDate"));
                studied.setCompleted(result.getDouble("isCompleted"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return studied;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public STUDIED getStudiedByLessonId(int lessonId) {
        STUDIED studied = null;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM STUDIED WHERE lessonId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, lessonId);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                studied.setUserId(result.getInt("userId"));
                studied.setLessonId(result.getInt("lessonId"));
                studied.setStudiedDate(result.getTimestamp("studiedDate"));
                studied.setCompleted(result.getDouble("isCompleted"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return studied;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void createStudiedRecord(STUDIED studied) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO STUDIED (userId, lessonId, studiedDate, isCompleted) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studied.getUserId());
            pstmt.setInt(2, studied.getLessonId());
            pstmt.setTimestamp(3, studied.getStudiedDate());
            pstmt.setDouble(4, studied.getIsCompleted());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void updateStudiedRecord(STUDIED studied) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE STUDIED SET userId = ?, lessonId = ?, studiedDate = ?, isCompleted = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studied.getUserId());
            pstmt.setInt(2, studied.getLessonId());
            pstmt.setTimestamp(3, studied.getStudiedDate());
            pstmt.setDouble(4, studied.getIsCompleted());
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
            String sql = "DELETE FROM STUDIED WHERE id = ?";
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
            String sql = "SELECT lessonId, studiedDate, isCompleted FROM STUDIED WHERE userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();  

            while (rs.next()) {
                Map<String, Object> progress = new LinkedHashMap<>();
                progress.put("lessonId", rs.getInt("lessonId"));
                progress.put("studiedDate", rs.getTimestamp("studiedDate"));
                progress.put("isCompleted", rs.getDouble("isCompleted"));
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