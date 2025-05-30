package Model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

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
    private Boolean isBanned; // New field

    public USERS() {}

    public USERS(String userName, String email, String password, int roleID, boolean isBanned) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roleID = roleID;
        this.isBanned = isBanned; // Initialize new field
    }

    public USERS(String userName, String email, String password, int roleID) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roleID = roleID;
    }

    public USERS(int id, String userName, String password, String email, int roleID) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.roleID = roleID;
    }

    public USERS(int id, String userName, String password, String email, int roleID, boolean isBanned) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.roleID = roleID;
        this.isBanned = isBanned; // Initialize new field
    }

    public int getID() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getRoleID() {
        return roleID;
    }

    public Boolean getIsBanned() {
        return isBanned; // Getter for isBanned
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

    public void setIsBanned(boolean isBanned) {
        this.isBanned = isBanned; // Setter for isBanned
    }

    public String generateToken() {
        try {
            String token = JWT.create()
                .withClaim("id", id)
                .withClaim("userName", userName)
                .withClaim("email", email)
                .withClaim("roleID", roleID)
                .withClaim("isBanned", isBanned) // Include isBanned in the token
                .sign(Algorithm.HMAC256("my-very-secure-and-long-secret-key-1234567890"));
            return token;
        } catch (Exception e) {
            System.out.println("Error generating token: " + e.getMessage());
            return null;
        }
    }
}
