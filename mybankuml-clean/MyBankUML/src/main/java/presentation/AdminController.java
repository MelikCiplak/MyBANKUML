package presentation;

import application.AdminManager;
import application.AuthenticationManager;
import application.RoleManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import model.User;
import model.Customer;
import model.Teller;
import model.Administrator;
import util.SecurityUtils;

import java.util.Map;

public class AdminController {
    private AdminManager adminManager;
    private AuthenticationManager authManager;
    private RoleManager roleManager;
    private ObjectMapper mapper = new ObjectMapper();

    public AdminController(AdminManager admin, AuthenticationManager auth, RoleManager role) {
        this.adminManager = admin;
        this.authManager = auth;
        this.roleManager = role;
    }

    // ... createUser method remains the same ... 
    public void createUser(Context ctx) {
        // (Keep previous implementation)
        try {
            String token = ctx.header("Authorization");
            User admin = authManager.getUserByToken(token);
            if (!roleManager.canAccess(admin, RoleManager.Feature.MANAGE_USERS)) {
                ctx.status(403).json(Map.of("error", "Forbidden")); return;
            }
            @SuppressWarnings("unchecked")
            Map<String, String> req = mapper.readValue(ctx.body(), Map.class);
            String roleStr = req.get("role");
            String uuid = SecurityUtils.generateUUID();
            String userPass = req.get("password"); 
            User newUser;
            if (roleStr != null && roleStr.equalsIgnoreCase("TELLER")) {
                newUser = new Teller(uuid, req.get("username"), null, req.get("name"));
            } else if (roleStr != null && roleStr.equalsIgnoreCase("ADMIN")) {
                newUser = new Administrator(uuid, req.get("username"), null, req.get("name"));
            } else {
                newUser = new Customer(uuid, req.get("username"), null, req.get("name"));
            }
            adminManager.createUser(admin, newUser, userPass);
            ctx.status(201).json(Map.of("message", "User created successfully"));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", e.getMessage() != null ? e.getMessage() : "Error"));
        }
    }

    // NEW: Update User (Status, Role, or 2FA)
    public void updateUser(Context ctx) {
        try {
            String token = ctx.header("Authorization");
            User admin = authManager.getUserByToken(token);

            if (!roleManager.canAccess(admin, RoleManager.Feature.MANAGE_USERS)) {
                ctx.status(403).json(Map.of("error", "Forbidden"));
                return;
            }

            String targetUserId = ctx.pathParam("id");
            @SuppressWarnings("unchecked")
            Map<String, Object> req = mapper.readValue(ctx.body(), Map.class);

            if (req.containsKey("status")) {
                adminManager.updateUserStatus(admin, targetUserId, (String) req.get("status"));
            }
            if (req.containsKey("role")) {
                adminManager.updateUserRole(admin, targetUserId, (String) req.get("role"));
            }
            if (req.containsKey("twoFactorEnabled")) {
                adminManager.toggle2FA(admin, targetUserId, (boolean) req.get("twoFactorEnabled"));
            }

            ctx.json(Map.of("message", "User updated successfully"));

        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }
}