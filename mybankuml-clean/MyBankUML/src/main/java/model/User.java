package model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "role",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Customer.class, name = "CUSTOMER"),
    @JsonSubTypes.Type(value = Teller.class, name = "TELLER"),
    @JsonSubTypes.Type(value = Administrator.class, name = "ADMIN")
})
public abstract class User implements Serializable {
    public enum Role { CUSTOMER, TELLER, ADMIN }
    public enum Status { ACTIVE, LOCKED, INACTIVE }

    private String userID;
    private String username;
    private String passwordHash;
    private String fullName;
    private Role role;
    private Status status;
    private int failedLoginAttempts;
    private boolean twoFactorEnabled; 

    public User() {}

    public User(String userID, String username, String passwordHash, String fullName, Role role) {
        this.userID = userID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.status = Status.ACTIVE;
        this.failedLoginAttempts = 0;
        this.twoFactorEnabled = false;
    }

    // Getters
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public Status getStatus() { return status; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public boolean isTwoFactorEnabled() { return twoFactorEnabled; }

    // Setters
    public void setStatus(Status status) { this.status = status; }
    public void incrementFailedAttempts() { this.failedLoginAttempts++; }
    public void resetFailedAttempts() { this.failedLoginAttempts = 0; }
    public void setRole(Role role) { this.role = role; }
    public void setTwoFactorEnabled(boolean enabled) { this.twoFactorEnabled = enabled; }
    
    // CRITICAL FOR ADMIN TEST
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public abstract String getDashboardRoute();
}