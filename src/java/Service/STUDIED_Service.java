package Service;

import Config.DBConnection;
import Model.STUDIES;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class STUDIED_Service {

    public List<STUDIES> getAllStudiedRecords() {
        List<STUDIES> studiedList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM STUDIED";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                STUDIES studied = new STUDIES();
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

    public STUDIES getStudiedRecordById(int id) {
        STUDIES studied = new STUDIES();
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

    public void createStudiedRecord(STUDIES studied) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO STUDIED (userId, lessonId, studiedDate, isCompleted) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studied.getUserId());
            pstmt.setInt(2, studied.getLessonId());
            pstmt.setTimestamp(3, studied.getStudiedDate());
            pstmt.setDouble(4, studied.isCompleted());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void updateStudiedRecord(STUDIES studied) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE STUDIED SET userId = ?, lessonId = ?, studiedDate = ?, isCompleted = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studied.getUserId());
            pstmt.setInt(2, studied.getLessonId());
            pstmt.setTimestamp(3, studied.getStudiedDate());
            pstmt.setDouble(4, studied.isCompleted());
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
}