package model;

public class Teller extends User {

    public Teller() {} 

    public Teller(String userID, String username, String passwordHash, String fullName) {
        super(userID, username, passwordHash, fullName, Role.TELLER);
    }

    @Override
    public String getDashboardRoute() {
        return "/dashboard"; // Frontend reveals Teller tabs
    }
}