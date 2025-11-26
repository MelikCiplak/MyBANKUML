package application;

import data.DatabaseRepository;
import model.Account;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchManager {
    private DatabaseRepository database;

    public SearchManager(DatabaseRepository database) {
        this.database = database;
    }

    /**
     * Searches for users based on exact ID or partial Name match.
     */
    public List<User> searchUsers(String query) {
        List<User> allUsers = database.findAllUsers();
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }

        String lowerQuery = query.toLowerCase();

        return allUsers.stream()
                .filter(u -> u.getUserID().toLowerCase().equals(lowerQuery) || 
                             u.getUsername().toLowerCase().contains(lowerQuery) ||
                             u.getFullName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Finds specific account details for a Teller.
     */
    public Account searchAccountByNumber(String accountNumber) throws Exception {
        return database.findAccountByNumber(accountNumber)
                .orElseThrow(() -> new Exception("Account not found"));
    }
}