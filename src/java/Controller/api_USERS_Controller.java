/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Utils.jwt;
import Service.USERS_Service;
import Model.USERS;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author minhk
 */
@WebServlet(name = "api_USERS_Controller", urlPatterns = {"/api/users"})
public class api_USERS_Controller extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // check validate token        
        if (jwt.validateToken(request)) {
            System.out.println("Token is valid!");

            // check roleID
            int roleID = (int) request.getAttribute("roleID");
            if (roleID == 3) { // 1 student, 2 teacher, 3 admin

                // Check if get user by id, the request will have a query id
                String idParam = request.getParameter("id");
                
                response.setContentType("application/json;charset=UTF-8");

                if (idParam != null) {
                    // Get user by id
                    try {
                        int id = Integer.parseInt(idParam);

                        USERS_Service userService = new USERS_Service();
                        USERS user = userService.getUserById(id);
                        if (user != null) {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write(new Gson().toJson(user));
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().write("{\"error\": \"User not found\"}");
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Invalid user id\"}");
                    }
                } else {
                    // Get all users
                    System.out.println("Get all users");
                    USERS_Service userService = new USERS_Service();
                    List<USERS> userList = userService.getAllUsers();
                    System.out.println(userList);
                    if (userList != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(userList));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\": \"No users found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"You do not have permission to access this resource\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            String userName = request.getParameter("userName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String roleID = request.getParameter("roleID");

            if (userName == null || email == null || password == null || roleID == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing required parameters\"}");
                return;
            }

            USERS_Service userService = new USERS_Service();
            USERS user = new USERS(userName, email, password, Integer.parseInt(roleID));
            userService.createUser(user);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(new Gson().toJson(user));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
