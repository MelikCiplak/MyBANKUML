package application;

import model.User;

public class RoleManager {

    public enum Feature {
        VIEW_OWN_ACCOUNT,
        SEARCH_CUSTOMERS,   // Teller/Admin only
        PROCESS_TRANSACTION, // Customer (own) or Teller (others)
        MANAGE_USERS,       // Admin only
        VIEW_AUDIT_LOGS     // Admin only
    }

    public boolean canAccess(User user, Feature feature) {
        switch (user.getRole()) {
            case CUSTOMER:
                return feature == Feature.VIEW_OWN_ACCOUNT 
                    || feature == Feature.PROCESS_TRANSACTION;
            
            case TELLER:
                return feature == Feature.SEARCH_CUSTOMERS 
                    || feature == Feature.PROCESS_TRANSACTION; // On behalf of others
            
            case ADMIN:
                return feature == Feature.MANAGE_USERS 
                    || feature == Feature.VIEW_AUDIT_LOGS 
                    || feature == Feature.SEARCH_CUSTOMERS;
            
            default:
                return false;
        }
    }
}