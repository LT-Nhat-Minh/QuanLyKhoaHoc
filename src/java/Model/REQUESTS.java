/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author minhk
 */
public class REQUESTS {
    private int id;
    private String title;
    private String description;
    private int userID;
    private int status;
    private String createdAt;
    private String updatedAt;
    
    public REQUESTS() {
    }

    public REQUESTS(int id, String title, String description, int userID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userID = userID;
    }

    public REQUESTS(int id, String title, String description, int userID, int status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userID = userID;
        this.status = status;
    }

    public REQUESTS(int id, String title, String description, int userID, int status, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userID = userID;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getUserID() {
        return userID;
    }

    public int getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
