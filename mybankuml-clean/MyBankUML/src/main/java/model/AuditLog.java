package model;

import java.time.LocalDateTime;

public class AuditLog {

    public AuditLog() {} 

    private String logID;
    private String adminID;      // Who performed the action
    private String action;       // e.g., "CREATED_USER", "CHANGED_ROLE"
    private String targetUserID; // Who was affected
    private String timestamp;

    public AuditLog(String logID, String adminID, String action, String targetUserID) {
        this.logID = logID;
        this.adminID = adminID;
        this.action = action;
        this.targetUserID = targetUserID;
        this.timestamp = LocalDateTime.now().toString();
    }

    // Getters
    public String getLogID() { return logID; }
    public String getAdminID() { return adminID; }
    public String getAction() { return action; }
    public String getTargetUserID() { return targetUserID; }
    public String getTimestamp() { return timestamp; }
}