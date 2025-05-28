/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author minhk
 */
public class COURSES_USERS_BANNED {
    private int courseID;
    private int bannedUserID;

    public COURSES_USERS_BANNED() {}

    public COURSES_USERS_BANNED(int courseID, int bannedUserID) {
        this.courseID = courseID;
        this.bannedUserID = bannedUserID;
    }

    public int getCourseID() {
        return courseID;
    }

    public int getBannedUserID() {
        return bannedUserID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setBannedUserID(int bannedUserID) {
        this.bannedUserID = bannedUserID;
    }
}
