package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    public Account() {} 

    public enum AccountType { CHECKING, SAVINGS }

    private String accountNumber;
    private String ownerUserID; // Foreign key linking to User
    private AccountType type;
    private BigDecimal balance;
    private String creationDate;

    public Account(String accountNumber, String ownerUserID, AccountType type, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.ownerUserID = ownerUserID;
        this.type = type;
        this.balance = balance;
        this.creationDate = LocalDateTime.now().toString();
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerUserID() { return ownerUserID; }
    public AccountType getType() { return type; }
    public BigDecimal getBalance() { return balance; }
    public String getCreationDate() { return creationDate; }
    
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setOwnerUserID(String ownerUserID) { this.ownerUserID = ownerUserID; }
    public void setType(AccountType type) { this.type = type; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }
    
    // Setter used by AccountManager after validation
    public void setBalance(BigDecimal balance) { 
        this.balance = balance; 
    }
}