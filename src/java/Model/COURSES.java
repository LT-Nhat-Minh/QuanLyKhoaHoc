/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author 22521
 */
public class COURSES {
    protected int id;
    private String title;    
    private String description;
    private double price;
    private int createdByUserID;
    
    public COURSES(){}

    public COURSES(String title, String description, double price, int createdByUserID){
        this.title = title;
        this.description = description;
        this.price = price;
        this.createdByUserID = createdByUserID;
    }
    
    public COURSES(int id, String title, String description, double price, int createdByUserID){
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.createdByUserID = createdByUserID;
    }
    
    public int getID(){
        return id;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getDescription(){
        return description;
    } 
    
    public double getPrice(){
        return price;
    }
    
    public int getCreatedUserByID(){
        return createdByUserID;
    }
    
    public void setID( int id){
        this.id = id;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setDescription(String description){
        this.description = description;
    } 
    
    public void setPrice(double price){
        this.price = price;
    }
    
    public void setCreatedUserByID( int createdUserByID){
        this.createdByUserID = createdUserByID;
    }
}

