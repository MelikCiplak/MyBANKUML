package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    public Transaction() {} 

    public enum Type { DEPOSIT, WITHDRAWAL, TRANSFER }

    private String transactionID;
    private String sourceAccountNumber;
    private String targetAccountNumber; // Nullable (only for transfers)
    private BigDecimal amount;
    private Type type;
    private String timestamp;

    public Transaction(String transactionID, String sourceAccountNumber, String targetAccountNumber, BigDecimal amount, Type type) {
        this.transactionID = transactionID;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now().toString();
    }

    // Getters
    public String getTransactionID() { return transactionID; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public String getTargetAccountNumber() { return targetAccountNumber; }
    public BigDecimal getAmount() { return amount; }
    public Type getType() { return type; }
    public String getTimestamp() { return timestamp; }
    
    // Setters
    public void setTransactionID(String transactionID) { this.transactionID = transactionID; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public void setTargetAccountNumber(String targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setType(Type type) { this.type = type; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}