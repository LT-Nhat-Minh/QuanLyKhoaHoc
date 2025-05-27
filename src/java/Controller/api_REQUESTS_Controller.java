package Controller;

import Service.REQUESTS_Service;
import Model.REQUESTS;
import Utils.jwt;
import Utils.parseForm;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author minhk
 */
@WebServlet(name = "api_REQUESTS_Controller", urlPatterns = {"/api/requests"})
public class api_REQUESTS_Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            if (request.getParameter("id") != null) {
                // Get request by ID
                System.out.println("Get request by ID");
                int id = Integer.parseInt(request.getParameter("id"));
                REQUESTS_Service requestService = new REQUESTS_Service();
                REQUESTS requestObj = requestService.getRequestById(id);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(requestObj));
            } else {
                // Get all requests
                System.out.println("Get all requests");
                REQUESTS_Service requestService = new REQUESTS_Service();
                List<REQUESTS> requestList = requestService.getAllRequests();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(requestList));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if(jwt.validateToken(request, response)){
            try{
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                int userID = (int) request.getAttribute("id");

                // Validate input
                if (title == null || description == null) {
                    throw new Exception("Missing required fields");
                }

                // Create request
                REQUESTS requestObj = new REQUESTS(0, title, description, userID, 1);
                REQUESTS_Service requestService = new REQUESTS_Service();
                requestService.createRequest(requestObj);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Request created successfully" + "\", \"request\": " + new Gson().toJson(requestObj) + "}");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
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
        if(jwt.validateToken(request, response)){
            int UserRoleID = (int) request.getAttribute("roleID");
            if(UserRoleID == 3) { // 1 student, 2 teacher, 3 admin
                try {
                    Map<String, String> params = parseForm.parseFormUrlEncoded(request);
                    int id = Integer.parseInt(params.get("id"));

                    //get information by id
                    REQUESTS_Service requestService = new REQUESTS_Service();
                    REQUESTS requestObj = requestService.getRequestById(id);

                    if (requestObj == null) {
                        throw new Exception("Request not found with the provided ID");
                    }

                    System.out.println("Get request" + new Gson().toJson(requestObj));

                    int status = Integer.parseInt(params.get("status"));
                    requestObj.setStatus(status);

                    // Update request
                    requestService.updateRequest(requestObj);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Request updated successfully" + "\", \"request\": " + new Gson().toJson(requestObj) + "}");
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                }
            } else {
                // User is not admin
                try{
                    Map<String, String> params = parseForm.parseFormUrlEncoded(request);
                    int id = Integer.parseInt(params.get("id"));
                    String title = params.get("title");
                    String description = params.get("description");
                    int userID = (int) request.getAttribute("id");

                    //get information about status from request by id then update title and description
                    REQUESTS_Service requestService = new REQUESTS_Service();
                    REQUESTS requestObj = requestService.getRequestById(id);

                    int status = requestObj.getStatus();

                    // Validate data
                    if (title == null || description == null || userID <= 0) {
                        throw new Exception("Missing required fields");
                    }

                    if(status != 1){
                        throw new Exception("You cannot update this request because it has been received and processed");
                    }

                    // Update request
                    requestObj.setTitle(title);
                    requestObj.setDescription(description);
                    requestService.updateRequest(requestObj);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Request updated successfully" + "\", \"request\": " + new Gson().toJson(requestObj) + "}");
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
                }
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
        if(jwt.validateToken(request, response)){
            int UserRoleID = (int) request.getAttribute("roleID");
            try {
                int requestID = Integer.parseInt(request.getParameter("id"));
                REQUESTS_Service requestService = new REQUESTS_Service();
                REQUESTS requestObj = requestService.getRequestById(requestID);
                if(requestID > 0){
                    if(UserRoleID == 3){
                        //admin is deleting request
                        requestService.deleteRequest(requestID);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("{\"message\": \"Request deleted successfully" + "\", \"request\": " + new Gson().toJson(requestObj) + "}");
                    } else {
                        //user are deleting their own request
                        //check if this user is the owner of this request
                        if(requestObj.getUserID() != (int) request.getAttribute("id")) {
                            throw new Exception("You cannot delete this request because you are not the owner of this request");
                        } else {
                            //check if this request is still not processed so user can delete it
                            if(requestObj.getStatus() == 1) {
                                requestService.deleteRequest(requestID);

                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("{\"message\": \"Request deleted successfully" + "\", \"request\": " + new Gson().toJson(requestObj) + "}");
                            } else {
                                throw new Exception("You cannot delete this request because it has been received and processed");
                            }
                        }
                    }
                } else {
                    throw new Exception("Request ID is required");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            }
            
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }
}
