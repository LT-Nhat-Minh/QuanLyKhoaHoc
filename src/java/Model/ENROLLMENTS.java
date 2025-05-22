package Model;

import java.sql.Timestamp;

/**
 *
 * @author 22521
 */
public class ENROLLMENTS {
    
    private int userId;
    private int courseId;
    private Timestamp enrollmentDate;
    private String status; // e.g., "active", "completed", "cancelled"
    
    public ENROLLMENTS() {}
    
    public ENROLLMENTS(int userId, int courseId, Timestamp enrollmentDate, String status) {
        this.userId = userId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    

    public int getUserId() {
        return userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public Timestamp getEnrollmentDate() {
        return enrollmentDate;
    }

    public String getStatus() {
        return status;
    }



    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setEnrollmentDate(Timestamp enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}