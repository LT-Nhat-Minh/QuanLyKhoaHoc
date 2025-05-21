/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author 22521
 */
public class USERS {
    private int id;
    private String userName;
    private String password;
    private String email;
    private int roleID;
    
    public USERS(){}
    
    public USERS(int id, String userName, String password,String email, int roleID){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.roleID = roleID;  
    }
    
   public int getID(){
       return id;
   }
   
   public String getUserName(){
       return userName;
   }
   
   public String getPassword(){
       return password;
   }
   
   public String getEmail(){
       return email;
   }
   
   public int getRoleID(){
       return roleID;
   }
   
   public void setID(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

}
