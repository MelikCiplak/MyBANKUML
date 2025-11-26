package application;

import data.DatabaseRepository;
import model.AuditLog;
import model.User;
import util.SecurityUtils;

public class AdminManager {
    private DatabaseRepository database;

    public AdminManager(DatabaseRepository database) {
        this.database = database;
    }

    // ... createUser method remains the same ...
    public void createUser(User adminUser, User newUser, String rawPassword) throws Exception {
        if (database.findUserByUsername(newUser.getUsername()).isPresent()) {
            throw new Exception("Username already exists");
        }
        String hashedPassword = SecurityUtils.hashPassword(rawPassword);
        newUser.setPasswordHash(hashedPassword);
        database.saveUser(newUser);

        logAudit(adminUser, "CREATE_USER", newUser.getUserID());
    }

    // NEW: Update User Status (Active/Inactive)
    public void updateUserStatus(User adminUser, String targetUserId, String statusStr) throws Exception {
        User target = getUser(targetUserId);
        try {
            User.Status newStatus = User.Status.valueOf(statusStr.toUpperCase());
            target.setStatus(newStatus);
            database.saveUser(target);
            logAudit(adminUser, "UPDATE_STATUS_" + newStatus, targetUserId);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid Status");
        }
    }

    // NEW: Update User Role
    public void updateUserRole(User adminUser, String targetUserId, String roleStr) throws Exception {
        User target = getUser(targetUserId);
        try {
            User.Role newRole = User.Role.valueOf(roleStr.toUpperCase());
            target.setRole(newRole); 
            // Note: In a full DB system we might need to cast/convert the object type, 
            // but with Jackson JSON, changing the enum and saving is usually sufficient 
            // if the fields are compatible.
            database.saveUser(target);
            logAudit(adminUser, "UPDATE_ROLE_" + newRole, targetUserId);
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid Role");
        }
    }

    // NEW: Toggle 2FA (Cosmetic)
    public void toggle2FA(User adminUser, String targetUserId, boolean enabled) throws Exception {
        User target = getUser(targetUserId);
        target.setTwoFactorEnabled(enabled);
        database.saveUser(target);
        logAudit(adminUser, "TOGGLE_2FA_" + enabled, targetUserId);
    }

    private User getUser(String id) throws Exception {
        return database.findUserByID(id).orElseThrow(() -> new Exception("User not found"));
    }

    private void logAudit(User admin, String action, String targetId) {
        AuditLog log = new AuditLog(SecurityUtils.generateUUID(), admin.getUserID(), action, targetId);
        database.logAudit(log);
    }
}