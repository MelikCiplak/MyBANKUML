package presentation;

import application.AuthenticationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import java.util.Map;

public class AuthController {

    private AuthenticationManager authManager;
    private ObjectMapper mapper = new ObjectMapper();

    public AuthController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    public void login(Context ctx) {
        try {
            // Jackson parsing
            @SuppressWarnings("unchecked")
            Map<String, String> creds = mapper.readValue(ctx.body(), Map.class);
            
            String token = authManager.login(creds.get("username"), creds.get("password"));
            
            ctx.json(Map.of(
                "token", token,
                "role", authManager.getUserByToken(token).getRole(), 
                "message", "Login successful"
            ));
        } catch (Exception e) {
            ctx.status(401).json(Map.of("error", e.getMessage() != null ? e.getMessage() : "Authentication failed"));
        }
    }

    public void logout(Context ctx) {
        String token = ctx.header("Authorization");
        if (token != null) {
            authManager.logout(token);
        }
        ctx.status(200).json(Map.of("message", "Logged out"));
    }
}