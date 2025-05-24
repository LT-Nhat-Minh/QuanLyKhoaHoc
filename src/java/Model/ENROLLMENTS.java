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
    private int statusId; // e.g., "active", "completed", "cancelled"
    private String feedbackEnrollment;
    
    public ENROLLMENTS() {}
    
    public ENROLLMENTS(int userId, int courseId, Timestamp enrollmentDate, int statusId, String feedbackEnrollment) {
        this.userId = userId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.statusId = statusId;
        this.feedbackEnrollment = feedbackEnrollment;
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

    public int getStatusID() {
        return statusId;
    }
    
     public String getFeedbackEnrollment() {
        return feedbackEnrollment;
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

    public void setStatusID(int statusId) {
        this.statusId = statusId;
    }
    
     public void setFeedbackEnrollment(String feedbackEnrollment) {
        this.feedbackEnrollment = feedbackEnrollment;
    }
}