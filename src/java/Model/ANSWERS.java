package Model;

/**
 *
 * @author 22521
 */
public class ANSWERS {
    private int userId;
    private int quizId;
    private int answer;
    private String updatedAt;
    
    public ANSWERS() {}
    
    public ANSWERS(int userId, int quizId, int answer, String updatedAt) {
        this.userId = userId;
        this.quizId = quizId;
        this.answer = answer;
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

    public String getUpdatedAt(){
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

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}