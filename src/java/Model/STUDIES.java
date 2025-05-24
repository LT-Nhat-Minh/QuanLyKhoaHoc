package Model;

import java.sql.Timestamp;

/**
 *
 * @author 22521
 */
public class STUDIES {
    private int userId;
    private int lessonId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public STUDIES() {}

    
    
    public STUDIES( int userId, int lessonId, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public int getUserId() {
        return userId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }



    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    
}