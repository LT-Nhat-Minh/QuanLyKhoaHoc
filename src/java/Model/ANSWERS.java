package Model;

import java.sql.Timestamp;

/**
 *
 * @author 22521
 */
public class ANSWERS {
    private int userId;
    private int quizId;
    private int answer;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ANSWERS() {}

    public ANSWERS(int userId, int quizId, int answer, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.quizId = quizId;
        this.answer = answer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getAnswer() {
        return answer;
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

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}