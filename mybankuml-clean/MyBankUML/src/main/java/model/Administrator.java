package model;

public class Administrator extends User {

    public Administrator() {} 

    public Administrator(String userID, String username, String passwordHash, String fullName) {
        super(userID, username, passwordHash, fullName, Role.ADMIN);
    }

    @Override
    public String getDashboardRoute() {
        return "/dashboard"; // Frontend reveals Admin tabs
    }
}