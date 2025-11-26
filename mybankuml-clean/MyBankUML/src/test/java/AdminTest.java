import application.AdminManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {
    private MockDatabase db;
    private AdminManager adminManager;
    private User adminUser;

    @BeforeEach
    public void setUp() {
        db = new MockDatabase();
        adminManager = new AdminManager(db);
        adminUser = new Administrator("A01", "admin", "hash", "Super Admin");
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        User newUser = new Teller("T01", "newteller", null, "Teller Name");
        
        adminManager.createUser(adminUser, newUser, "password123");

        // Verify User saved
        assertTrue(db.findUserByUsername("newteller").isPresent());
        
        // Verify Password hashed
        String storedHash = db.findUserByUsername("newteller").get().getPasswordHash();
        assertNotEquals("password123", storedHash);
        assertTrue(storedHash.startsWith("$2a$"));
    }

    @Test
    public void testAuditLogCreated() throws Exception {
        User newUser = new Customer("C01", "cust", null, "Cust Name");
        adminManager.createUser(adminUser, newUser, "pass");

        assertEquals(1, db.auditLogs.size());
        assertEquals("CREATE_USER", db.auditLogs.get(0).getAction());
        assertEquals("A01", db.auditLogs.get(0).getAdminID());
    }

    @Test
    public void testDuplicateUsername() {
        User existing = new Customer("C1", "dup", "hash", "Name");
        db.saveUser(existing);

        User duplicate = new Teller("T1", "dup", null, "Other Name");
        
        Exception e = assertThrows(Exception.class, () -> {
            adminManager.createUser(adminUser, duplicate, "pass");
        });
        assertEquals("Username already exists", e.getMessage());
    }
}