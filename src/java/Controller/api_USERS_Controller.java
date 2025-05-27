/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Utils.jwt;
import Service.USERS_Service;
import Model.USERS;
import Utils.parseForm;
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
import java.util.Map;
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
        if (jwt.validateToken(request, response)) {
            // check UserRoleID
            int UserRoleID = (int) request.getAttribute("roleID");
            if (UserRoleID == 3) { // 1 student, 2 teacher, 3 admin

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

        if(jwt.validateToken(request, response)) {
            //check UserRoleID
            int UserRoleID = (int) request.getAttribute("roleID");
            if(UserRoleID == 3) { // 1 student, 2 teacher, 3 admin
                try {
                    String userName = request.getParameter("userName");
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    int roleID = Integer.parseInt(request.getParameter("roleID"));

                    if (userName == null || email == null || password == null || roleID <= 0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Missing required parameters\"}");
                        return;
                    }

                    USERS_Service userService = new USERS_Service();
                    USERS user = new USERS(userName, password, email, roleID);
                    userService.createUser(user);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.getWriter().write(new Gson().toJson(user));

                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if(jwt.validateToken(request, response)) {
            //check roleID
            int UserRoleID = (int) request.getAttribute("roleID");
            if(UserRoleID == 3) { // 1 student, 2 teacher, 3 admin
                try {
                    Map<String, String> params = parseForm.parseFormUrlEncoded(request);
        
                    int id = Integer.parseInt(params.get("id"));
                    String userName = params.get("userName");
                    String email = params.get("email");
                    String password = params.get("password");
                    int roleID = Integer.parseInt(params.get("roleID"));
        
                    // Validate input
                    if (userName == null || email == null || password == null || roleID <= 0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Missing required parameters\"}");
                        return;
                    }
        
                    // Update user
                    USERS user = new USERS(id, userName, password, email, roleID);
                    USERS_Service userService = new USERS_Service();
                    userService.updateUser(user);
        
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(new Gson().toJson(user));
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if(jwt.validateToken(request, response)) {
            //check UserRoleID
            int UserRoleID = (int) request.getAttribute("roleID");
            if(UserRoleID == 3) { // 1 student, 2 teacher, 3 admin
                try {
                    if(request.getParameter("id") != null) {
                        int id = Integer.parseInt(request.getParameter("id"));
                        USERS_Service userService = new USERS_Service();
                        USERS user = userService.getUserById(id); //store user before deleting
                        userService.deleteUser(id);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("{\"message\": \"User deleted successfully\", \"user\": " + new Gson().toJson(user) + "}");
                    } else {
                        throw new Exception("User ID is required");
                    }
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
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
}
