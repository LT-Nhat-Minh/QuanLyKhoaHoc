/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author minhk
 */
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;

public class jwt {
    //validate token with java-jwt
    public static boolean validateToken(HttpServletRequest request){
        // Get the token from the Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        // Check if the header is present and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false; // Invalid header
        }

        // Extract the token by removing the "Bearer " prefix
        String token = authorizationHeader.substring(7);

        try {
            // Verify the token
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("my-very-secure-and-long-secret-key-1234567890"))
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            // Print claims for debugging
            System.out.println("userName: " + decodedJWT.getClaim("userName").asString());
            System.out.println("email: " + decodedJWT.getClaim("email").asString());
            System.out.println("roleID: " + decodedJWT.getClaim("roleID").asInt());
            System.out.println("id: " + decodedJWT.getClaim("id").asInt());

            //attach id to request for authorization and authentication
            request.setAttribute("id", decodedJWT.getClaim("id").asInt());
            request.setAttribute("userName", decodedJWT.getClaim("userName").asString());
            request.setAttribute("email", decodedJWT.getClaim("email").asString());
            request.setAttribute("roleID", decodedJWT.getClaim("roleID").asInt());

            return true; // Token is valid
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            System.out.println("Invalid token: " + exception.getMessage());
            return false;
        }
    }
}
