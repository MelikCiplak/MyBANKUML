package data;

import model.Account;
import model.AuditLog;
import model.Transaction;
import model.User;
import java.util.List;
import java.util.Optional;

public interface DatabaseRepository {
    // User Operations
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByID(String id);
    List<User> findAllUsers(); // For Admin/Tellers
    void saveUser(User user); // Handles Create and Update
    
    // Account Operations
    List<Account> findAccountsByUserID(String userID);
    Optional<Account> findAccountByNumber(String accountNumber);
    void saveAccount(Account account); // Handles Balance Updates

    // Transaction Operations
    void logTransaction(Transaction transaction);
    List<Transaction> findTransactionsByAccount(String accountNumber);

    // Audit Operations
    void logAudit(AuditLog log);
    List<AuditLog> findAllAuditLogs();
}