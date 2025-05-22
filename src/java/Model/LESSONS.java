/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author 22521
 */
public class LESSONS {
    protected int id;
    private String title;    
    private String content;
    private String videoURL;
    
    public LESSONS(){}

    public LESSONS(String title, String content, String videoURL){
        this.title = title;
        this.content = content;
        this.videoURL = videoURL;
    }

    public LESSONS(int id, String title, String content, String videoURL){
        this.id = id;
        this.title = title;
        this.content = content;
        this.videoURL = videoURL;
    }
    
    public int getID(){
        return id;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getContent(){
        return content;
    } 
    
    public String getVideoURL(){
        return videoURL;
    }
    
    public void setID( int id){
        this.id = id;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setContent(String content){
        this.content = content;
    } 
    
    public void setVideoURL(String videoURL){
        this.videoURL = videoURL;
    }
    
    
}
