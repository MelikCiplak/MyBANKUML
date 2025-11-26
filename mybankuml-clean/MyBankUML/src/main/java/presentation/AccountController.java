package presentation;

import application.AccountManager;
import application.AuthenticationManager;
import application.RoleManager;
import application.TransactionManager;
import com.fasterxml.jackson.databind.ObjectMapper; // Switched from Gson
import io.javalin.http.Context;
import model.User;

import java.math.BigDecimal;
import java.util.Map;

public class AccountController {
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    private AuthenticationManager authManager;
    private RoleManager roleManager;
    private ObjectMapper mapper = new ObjectMapper(); // Switched from Gson

    public AccountController(AccountManager am, TransactionManager tm, AuthenticationManager auth, RoleManager role) {
        this.accountManager = am;
        this.transactionManager = tm;
        this.authManager = auth;
        this.roleManager = role;
    }

    public void handleTransaction(Context ctx) {
        try {
            // 1. Verify Session
            String token = ctx.header("Authorization");
            User user = authManager.getUserByToken(token);

            // 2. Check Permissions (RBAC)
            if (!roleManager.canAccess(user, RoleManager.Feature.PROCESS_TRANSACTION)) {
                throw new Exception("Access Denied");
            }

            // 3. Parse Request (Using Jackson)
            // Body: { "type": "DEPOSIT", "accountNumber": "123", "amount": 50.00, "targetAccount": "456" }
            @SuppressWarnings("unchecked")
            Map<String, Object> req = mapper.readValue(ctx.body(), Map.class);
            
            String type = (String) req.get("type");
            String accNum = (String) req.get("accountNumber");
            
            // Handle numeric conversion safely for BigDecimal
            Object amountObj = req.get("amount");
            if (amountObj == null) throw new Exception("Amount is required");
            BigDecimal amount = new BigDecimal(String.valueOf(amountObj));

            // 4. Execute Logic
            if (type == null) throw new Exception("Transaction type is required");

            switch (type.toUpperCase()) {
                case "DEPOSIT":
                    accountManager.deposit(accNum, amount);
                    break;
                case "WITHDRAWAL":
                    accountManager.withdraw(accNum, amount);
                    break;
                case "TRANSFER":
                    String target = (String) req.get("targetAccount");
                    if (target == null) throw new Exception("Target account required for transfer");
                    transactionManager.transfer(accNum, target, amount);
                    break;
                default:
                    throw new Exception("Invalid transaction type");
            }

            ctx.status(200).json(Map.of("message", "Transaction Successful"));

        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", e.getMessage() != null ? e.getMessage() : "Transaction Failed"));
        }
    }
}