/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author minhk
 */
public class parseForm {
    public static Map<String, String> parseFormUrlEncoded(HttpServletRequest request) throws IOException {
        String line = request.getReader().readLine();
        Map<String, String> params = new HashMap<>();
        
        String [] parts = line.split("&");
        for(String part : parts) {
            String[] keyValue = part.split("=");
            try {
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                params.put(key, value);
            } catch (UnsupportedEncodingException e) {
                System.out.println("Error decoding URL parameter: " + e.getMessage());
                return null;
            }
        }
        
        return params;
    } 
}
