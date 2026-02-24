package atrady.backend.atradybackend.common.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper= new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("statusCode", HttpStatus.UNAUTHORIZED.value());
        errorDetails.put("status", HttpStatus.UNAUTHORIZED);
        errorDetails.put("message", authenticationException.getMessage());
        objectMapper.writeValue(response.getOutputStream(), errorDetails);
    }

}
