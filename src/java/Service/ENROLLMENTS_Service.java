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
                enrollment.setCreatedAt(result.getTimestamp("createdAt"));
                enrollment.setUpdatedAt(result.getTimestamp("updatedAt"));
                enrollment.setFeedbackEnrollment(result.getString("feedbackEnrollment"));
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

    public List<ENROLLMENTS> getEnrollmentsByCourseId(int courseId) {
        List<ENROLLMENTS> enrollmentList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ENROLLMENTS WHERE courseId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                ENROLLMENTS enrollment = new ENROLLMENTS();
                enrollment.setUserId(result.getInt("userId"));
                enrollment.setCourseId(result.getInt("courseId"));
                enrollment.setCreatedAt(result.getTimestamp("createdAt"));
                enrollment.setUpdatedAt(result.getTimestamp("updatedAt"));
                enrollment.setFeedbackEnrollment(result.getString("feedbackEnrollment"));
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
                enrollment.setCreatedAt(result.getTimestamp("createdAt"));
                enrollment.setUpdatedAt(result.getTimestamp("updatedAt"));
                enrollment.setFeedbackEnrollment(result.getString("feedbackEnrollment"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return enrollment;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public ENROLLMENTS createEnrollment(ENROLLMENTS enrollment) {
        try {
            Connection conn = DBConnection.getConnection();

            String checkSql = "SELECT 1 FROM ENROLLMENTS WHERE userId = ? AND courseId = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, enrollment.getUserId());
            pstmt.setInt(2, enrollment.getCourseId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                throw new RuntimeException("User is already enrolled in this course");
            }

            String insertSql = "INSERT INTO ENROLLMENTS (userId, courseId) VALUES (?, ?)";
            pstmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, enrollment.getUserId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.executeUpdate();

            String selectSql = "SELECT * FROM ENROLLMENTS WHERE userId = ? AND courseId = ?";
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setInt(1, enrollment.getUserId());
            pstmt.setInt(2, enrollment.getCourseId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                enrollment.setCreatedAt(rs.getTimestamp("createdAt"));
                enrollment.setUpdatedAt(rs.getTimestamp("updatedAt"));
                enrollment.setFeedbackEnrollment(rs.getString("feedbackEnrollment"));
            }

            rs.close();
            pstmt.close();
            conn.close();

            return enrollment;
        } catch (Exception e) {
            throw new RuntimeException("Error creating enrollment: " + e.getMessage());
        }
    }

    public void updateEnrollment(ENROLLMENTS enrollment) {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "UPDATE ENROLLMENTS SET feedbackEnrollment = ? WHERE userId = ? AND courseId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, enrollment.getFeedbackEnrollment());
        pstmt.setInt(2, enrollment.getUserId());
        pstmt.setInt(3, enrollment.getCourseId());
        pstmt.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("Error: " + e.getMessage());
    }
}


    public void deleteEnrollment(int userId, int courseId) {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "DELETE FROM ENROLLMENTS WHERE userId = ? AND courseId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, courseId);
        pstmt.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("Error: " + e.getMessage());
    }
}
    
    public ENROLLMENTS getEnrollmentByUserIdAndCourseId(int userId, int courseId) {
    ENROLLMENTS enrollment = null;

    try {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM ENROLLMENTS WHERE userId = ? AND courseId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, courseId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            
            enrollment = new ENROLLMENTS();
            enrollment.setUserId(rs.getInt("userId"));
            enrollment.setCourseId(rs.getInt("courseId"));
            enrollment.setCreatedAt(rs.getTimestamp("createdAt"));
            enrollment.setUpdatedAt(rs.getTimestamp("updatedAt"));
            enrollment.setFeedbackEnrollment(rs.getString("feedbackEnrollment"));
            
        }

    } catch (Exception e) {
        throw new RuntimeException("Error getting enrollment: " + e.getMessage());
    }

    return enrollment;
}

    


}