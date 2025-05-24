package Model;

import java.sql.Timestamp;

/**
 *
 * @author 22521
 */
public class STUDIED {
    private int userId;
    private int lessonId;
    private Timestamp studiedDate;
    private double isCompleted;
    
    public STUDIED() {}

    
    
    public STUDIED( int userId, int lessonId, Timestamp studiedDate, double isCompleted) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.studiedDate = studiedDate;
        this.isCompleted = isCompleted;
    }


    public int getUserId() {
        return userId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public Timestamp getStudiedDate() {
        return studiedDate;
    }

    public double isCompleted() {
        return isCompleted;
    }



    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public void setStudiedDate(Timestamp studiedDate) {
        this.studiedDate = studiedDate;
    }

    public void setCompleted(double isCompleted) {
        this.isCompleted = isCompleted;
    }
} 