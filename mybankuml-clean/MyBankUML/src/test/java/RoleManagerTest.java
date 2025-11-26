import application.RoleManager;
import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleManagerTest {
    private RoleManager roleManager = new RoleManager();

    @Test
    public void testCustomerPermissions() {
        User customer = new Customer("U1", "cust", "hash", "Name");
        
        assertTrue(roleManager.canAccess(customer, RoleManager.Feature.VIEW_OWN_ACCOUNT));
        assertFalse(roleManager.canAccess(customer, RoleManager.Feature.MANAGE_USERS));
        assertFalse(roleManager.canAccess(customer, RoleManager.Feature.SEARCH_CUSTOMERS));
    }

    @Test
    public void testTellerPermissions() {
        User teller = new Teller("U2", "teller", "hash", "Name");

        assertTrue(roleManager.canAccess(teller, RoleManager.Feature.SEARCH_CUSTOMERS));
        assertTrue(roleManager.canAccess(teller, RoleManager.Feature.PROCESS_TRANSACTION));
        assertFalse(roleManager.canAccess(teller, RoleManager.Feature.MANAGE_USERS));
    }

    @Test
    public void testAdminPermissions() {
        User admin = new Administrator("U3", "admin", "hash", "Name");

        assertTrue(roleManager.canAccess(admin, RoleManager.Feature.MANAGE_USERS));
        assertTrue(roleManager.canAccess(admin, RoleManager.Feature.VIEW_AUDIT_LOGS));
    }
}