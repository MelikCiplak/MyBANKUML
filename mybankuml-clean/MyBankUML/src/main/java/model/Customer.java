package model;

public class Customer extends User {

    public Customer() {} 

    public Customer(String userID, String username, String passwordHash, String fullName) {
        // Enforce the specific Role.CUSTOMER here
        super(userID, username, passwordHash, fullName, Role.CUSTOMER);
    }

    @Override
    public String getDashboardRoute() {
        return "/dashboard"; // The logic for what this dashboard looks like handles in the Frontend JS
    }
}