/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Config.DBConnection;
import java.util.List;
import Model.USERS;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author minhk
 */
public class USERS_Service {
    public List<USERS> getAllUsers() {
        List<USERS> userList = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM USERS";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                USERS user = new USERS();
                user.setID(result.getInt("id"));
                user.setUserName(result.getString(""));
                user.setPassword(result.getString("password"));
                user.setEmail(result.getString("email"));
                user.setRoleID(result.getInt("roleID"));
                userList.add(user);
            }

            result.close();
            pstmt.close();
            conn.close();

            return userList;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public USERS getUserById(int id) {
        // Implement the logic to retrieve a user by ID from the database
        // For example, you can use JDBC or an ORM framework like Hibernate
        // to fetch the data and return a USERS object.
        return null;
    }

    public void createUser(USERS user) {
        // Implement the logic to create a new user in the database
        // For example, you can use JDBC or an ORM framework like Hibernate
        // to insert the data into the database.
    }

    public void updateUser(USERS user) {
        // Implement the logic to update an existing user in the database
        // For example, you can use JDBC or an ORM framework like Hibernate
        // to update the data in the database.
    }

    public void deleteUser(int id) {
        // Implement the logic to delete a user by ID from the database
        // For example, you can use JDBC or an ORM framework like Hibernate
        // to delete the data from the database.
    }
}
