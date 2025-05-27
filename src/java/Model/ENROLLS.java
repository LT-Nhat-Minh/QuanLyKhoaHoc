package Model;

import java.sql.Timestamp;

/**
 *
 * @author 22521
 */
public class ENROLLS {
    
    private int userId;
    private int courseId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String feedbackEnrollment;
    private int rating; // Thêm thuộc tính rating
    
    public ENROLLS() {}
    
    public ENROLLS(int userId, int courseId, Timestamp createdAt, Timestamp updatedAt, String feedbackEnrollment, int rating) {
        this.userId = userId;
        this.courseId = courseId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.feedbackEnrollment = feedbackEnrollment;
        this.rating = rating; // Gán giá trị cho rating
    }

    public int getUserId() {
        return userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public String getFeedbackEnrollment() {
        return feedbackEnrollment;
    }
    
    public int getRating() { // Getter cho rating
        return rating;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setFeedbackEnrollment(String feedbackEnrollment) {
        this.feedbackEnrollment = feedbackEnrollment;
    }
    
    public void setRating(int rating) { // Setter cho rating
        this.rating = rating;
    }
}