package Service;

import Config.DBConnection;
import Model.ENROLLMENTS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ENROLLMENTS_Service {

    public List<ENROLLMENTS> getAllEnrollments() {
        List<ENROLLMENTS> enrollmentList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ENROLLMENTS";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                ENROLLMENTS enrollment = new ENROLLMENTS();
                enrollment.setUserId(result.getInt("userId"));
                enrollment.setCourseId(result.getInt("courseId"));
                enrollment.setEnrollmentDate(result.getTimestamp("enrollmentDate"));
                enrollment.setStatus(result.getString("status"));
                enrollmentList.add(enrollment);
            }

            result.close();
            pstmt.close();
            conn.close();

            return enrollmentList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public ENROLLMENTS getEnrollmentById(int id) {
        ENROLLMENTS enrollment = new ENROLLMENTS();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ENROLLMENTS WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                enrollment.setUserId(result.getInt("userId"));
                enrollment.setCourseId(result.getInt("courseId"));
                enrollment.setEnrollmentDate(result.getTimestamp("enrollmentDate"));
                enrollment.setStatus(result.getString("status"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return enrollment;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void createEnrollment(ENROLLMENTS enrollment) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO ENROLLMENTS (userId, courseId, enrollmentDate, status) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, enrollment.getUserId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setTimestamp(3, enrollment.getEnrollmentDate());
            pstmt.setString(4, enrollment.getStatus());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void updateEnrollment(ENROLLMENTS enrollment) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE ENROLLMENTS SET userId = ?, courseId = ?, enrollmentDate = ?, status = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, enrollment.getUserId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setTimestamp(3, enrollment.getEnrollmentDate());
            pstmt.setString(4, enrollment.getStatus());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteEnrollment(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM ENROLLMENTS WHERE id = ?";
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