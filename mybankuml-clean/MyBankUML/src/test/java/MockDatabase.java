import data.DatabaseRepository;
import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockDatabase implements DatabaseRepository {
    public List<User> users = new ArrayList<>();
    public List<Account> accounts = new ArrayList<>();
    public List<Transaction> transactions = new ArrayList<>();
    public List<AuditLog> auditLogs = new ArrayList<>();

    // --- USER ---
    @Override
    public Optional<User> findUserByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }
    @Override
    public Optional<User> findUserByID(String id) {
        return users.stream().filter(u -> u.getUserID().equals(id)).findFirst();
    }
    @Override
    public List<User> findAllUsers() { return new ArrayList<>(users); }
    @Override
    public void saveUser(User user) {
        users.removeIf(u -> u.getUserID().equals(user.getUserID()));
        users.add(user);
    }

    // --- ACCOUNT ---
    @Override
    public List<Account> findAccountsByUserID(String userID) {
        return accounts.stream().filter(a -> a.getOwnerUserID().equals(userID)).collect(Collectors.toList());
    }
    @Override
    public Optional<Account> findAccountByNumber(String accountNumber) {
        return accounts.stream().filter(a -> a.getAccountNumber().equals(accountNumber)).findFirst();
    }
    @Override
    public void saveAccount(Account account) {
        accounts.removeIf(a -> a.getAccountNumber().equals(account.getAccountNumber()));
        accounts.add(account);
    }

    // --- TRANSACTION ---
    @Override
    public void logTransaction(Transaction transaction) { transactions.add(transaction); }
    @Override
    public List<Transaction> findTransactionsByAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getSourceAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    // --- AUDIT ---
    @Override
    public void logAudit(AuditLog log) { auditLogs.add(log); }
    @Override
    public List<AuditLog> findAllAuditLogs() { return new ArrayList<>(auditLogs); }
}