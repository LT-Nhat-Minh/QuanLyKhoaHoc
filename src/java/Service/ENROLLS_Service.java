package Service;

import Config.DBConnection;
import Model.ENROLLS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ENROLLS_Service {

    public List<ENROLLS> getAllENROLLS() {
        List<ENROLLS> enrollmentList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ENROLLS";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                ENROLLS enrollment = new ENROLLS();
                enrollment.setUserId(result.getInt("userId"));
                enrollment.setCourseId(result.getInt("courseId"));
                enrollment.setCreatedAt(result.getTimestamp("createdAt"));
                enrollment.setUpdatedAt(result.getTimestamp("updatedAt"));
                enrollment.setFeedbackEnrollment(result.getString("feedbackEnrollment"));
                enrollment.setRating(result.getInt("rating"));
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

    public List<ENROLLS> getENROLLSByCourseId(int courseId) {
        List<ENROLLS> enrollmentList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ENROLLS WHERE courseId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                ENROLLS enrollment = new ENROLLS();
                enrollment.setUserId(result.getInt("userId"));
                enrollment.setCourseId(result.getInt("courseId"));
                enrollment.setCreatedAt(result.getTimestamp("createdAt"));
                enrollment.setUpdatedAt(result.getTimestamp("updatedAt"));
                enrollment.setFeedbackEnrollment(result.getString("feedbackEnrollment"));
                enrollment.setRating(result.getInt("rating"));
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

    public ENROLLS getEnrollmentById(int id) {
        ENROLLS enrollment = new ENROLLS();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM ENROLLS WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                enrollment.setUserId(result.getInt("userId"));
                enrollment.setCourseId(result.getInt("courseId"));
                enrollment.setCreatedAt(result.getTimestamp("createdAt"));
                enrollment.setUpdatedAt(result.getTimestamp("updatedAt"));
                enrollment.setFeedbackEnrollment(result.getString("feedbackEnrollment"));
                enrollment.setRating(result.getInt("rating"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return enrollment;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public ENROLLS createEnrollment(ENROLLS enrollment) {
        try {
            Connection conn = DBConnection.getConnection();

            String checkSql = "SELECT 1 FROM ENROLLS WHERE userId = ? AND courseId = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, enrollment.getUserId());
            pstmt.setInt(2, enrollment.getCourseId());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                throw new RuntimeException("User is already enrolled in this course");
            }

            String insertSql = "INSERT INTO ENROLLS (userId, courseId) VALUES (?, ?)";
            pstmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, enrollment.getUserId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.executeUpdate();

            String selectSql = "SELECT * FROM ENROLLS WHERE userId = ? AND courseId = ?";
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setInt(1, enrollment.getUserId());
            pstmt.setInt(2, enrollment.getCourseId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                enrollment.setCreatedAt(rs.getTimestamp("createdAt"));
                enrollment.setUpdatedAt(rs.getTimestamp("updatedAt"));
                enrollment.setFeedbackEnrollment(rs.getString("feedbackEnrollment") == null ? "" : rs.getString("feedbackEnrollment"));
                enrollment.setRating(rs.getInt("rating") == 0 ? 0 : rs.getInt("rating"));
            }

            rs.close();
            pstmt.close();
            conn.close();

            return enrollment;
        } catch (Exception e) {
            throw new RuntimeException("Error creating enrollment: " + e.getMessage());
        }
    }

    public void updateEnrollment(ENROLLS enrollment) {
    try (Connection conn = DBConnection.getConnection()) {
        //check if the enrollment exists
        String checkSql = "SELECT 1 FROM ENROLLS WHERE userId = ? AND courseId = ?";
        PreparedStatement pstmt = conn.prepareStatement(checkSql);
        pstmt.setInt(1, enrollment.getUserId());
        pstmt.setInt(2, enrollment.getCourseId());
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            throw new RuntimeException("Enrollment does not exist for userId: " + enrollment.getUserId() + " and courseId: " + enrollment.getCourseId());
        }
        rs.close();

        //update the enrollment
        String sql = "UPDATE ENROLLS SET feedbackEnrollment = ?, rating = ? WHERE userId = ? AND courseId = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, enrollment.getFeedbackEnrollment() != null ? enrollment.getFeedbackEnrollment() : "");
        pstmt.setInt(2, enrollment.getRating());
        pstmt.setInt(3, enrollment.getUserId());
        pstmt.setInt(4, enrollment.getCourseId());
        pstmt.executeUpdate();

        String selectSql = "SELECT * FROM ENROLLS WHERE userId = ? AND courseId = ?";
        pstmt = conn.prepareStatement(selectSql);
        pstmt.setInt(1, enrollment.getUserId());
        pstmt.setInt(2, enrollment.getCourseId());
        rs = pstmt.executeQuery();
        if (rs.next()) {
            enrollment.setCreatedAt(rs.getTimestamp("createdAt"));
            enrollment.setUpdatedAt(rs.getTimestamp("updatedAt"));
            enrollment.setFeedbackEnrollment(rs.getString("feedbackEnrollment") == null ? "" : rs.getString("feedbackEnrollment"));
            enrollment.setRating(rs.getInt("rating"));
        }
        rs.close();
        pstmt.close();
        conn.close();
    } catch (Exception e) {
        throw new RuntimeException("Error: " + e.getMessage());
    }
}


    public void deleteEnrollment(int userId, int courseId) {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "DELETE FROM ENROLLS WHERE userId = ? AND courseId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, courseId);
        pstmt.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("Error: " + e.getMessage());
    }
}
    
    public ENROLLS getEnrollmentByUserIdAndCourseId(int userId, int courseId) {
    ENROLLS enrollment = null;

    try {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM ENROLLS WHERE userId = ? AND courseId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, courseId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            
            enrollment = new ENROLLS();
            enrollment.setUserId(rs.getInt("userId"));
            enrollment.setCourseId(rs.getInt("courseId"));
            enrollment.setCreatedAt(rs.getTimestamp("createdAt"));
            enrollment.setUpdatedAt(rs.getTimestamp("updatedAt"));
            enrollment.setFeedbackEnrollment(rs.getString("feedbackEnrollment"));
            enrollment.setRating(rs.getInt("rating"));
        }

    } catch (Exception e) {
        throw new RuntimeException("Error getting enrollment: " + e.getMessage());
    }

    return enrollment;
}

    


}