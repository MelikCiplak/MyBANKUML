import application.AccountManager;
import model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private MockDatabase db;
    private AccountManager accountManager;

    @BeforeEach
    public void setUp() {
        db = new MockDatabase();
        accountManager = new AccountManager(db);

        // Setup Account with $500.00
        Account acc = new Account("A100", "U001", Account.AccountType.CHECKING, new BigDecimal("500.00"));
        db.saveAccount(acc);
    }

    @Test
    public void testDeposit() throws Exception {
        accountManager.deposit("A100", new BigDecimal("100.00"));
        assertEquals(new BigDecimal("600.00"), db.findAccountByNumber("A100").get().getBalance());
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        accountManager.withdraw("A100", new BigDecimal("100.00"));
        assertEquals(new BigDecimal("400.00"), db.findAccountByNumber("A100").get().getBalance());
    }

    @Test
    public void testInsufficientFunds() {
        Exception exception = assertThrows(Exception.class, () -> {
            accountManager.withdraw("A100", new BigDecimal("500.01"));
        });
        assertEquals("Insufficient Funds", exception.getMessage());
    }

    @Test
    public void testNegativeAmount() {
        assertThrows(Exception.class, () -> accountManager.deposit("A100", new BigDecimal("-50")));
    }
}