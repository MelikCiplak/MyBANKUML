import application.AuthenticationManager;
import model.Customer;
import model.User;
import util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {
    private MockDatabase db;
    private AuthenticationManager authManager;

    @BeforeEach
    public void setUp() {
        db = new MockDatabase();
        authManager = new AuthenticationManager(db);

        // Add a test user with hashed password "pass123"
        String hash = SecurityUtils.hashPassword("pass123");
        User user = new Customer("U001", "testuser", hash, "Test User");
        db.saveUser(user);
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        String token = authManager.login("testuser", "pass123");
        assertNotNull(token);
    }

    @Test
    public void testWrongPassword() {
        Exception exception = assertThrows(Exception.class, () -> {
            authManager.login("testuser", "wrongpass");
        });
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    public void testAccountLockout() {
        // Fail 5 times
        for (int i = 0; i < 5; i++) {
            try { authManager.login("testuser", "wrong"); } catch (Exception e) {}
        }

        // 6th attempt should throw Locked exception
        Exception exception = assertThrows(Exception.class, () -> {
            authManager.login("testuser", "pass123"); // Even with correct pass
        });
        
        assertTrue(exception.getMessage().contains("locked"));
        assertEquals(User.Status.LOCKED, db.findUserByUsername("testuser").get().getStatus());
    }
}