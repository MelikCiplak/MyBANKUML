import application.SearchManager;
import model.Customer;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SearchManagerTest {
    private MockDatabase db;
    private SearchManager searchManager;

    @BeforeEach
    public void setUp() {
        db = new MockDatabase();
        searchManager = new SearchManager(db);

        db.saveUser(new Customer("U001", "mike_scott", "hash", "Michael Scott"));
        db.saveUser(new Customer("U002", "dwight_s", "hash", "Dwight Schrute"));
        db.saveUser(new Customer("U003", "jim_halpert", "hash", "Jim Halpert"));
    }

    @Test
    public void testSearchByExactID() {
        List<User> results = searchManager.searchUsers("U001");
        assertEquals(1, results.size());
        assertEquals("Michael Scott", results.get(0).getFullName());
    }

    @Test
    public void testSearchByPartialName() {
        List<User> results = searchManager.searchUsers("Dwight");
        assertEquals(1, results.size());
        assertEquals("Dwight Schrute", results.get(0).getFullName());
    }

    @Test
    public void testSearchByPartialUsername() {
        List<User> results = searchManager.searchUsers("jim");
        assertEquals(1, results.size());
        assertEquals("Jim Halpert", results.get(0).getFullName());
    }

    @Test
    public void testEmptyQuery() {
        List<User> results = searchManager.searchUsers("");
        assertEquals(0, results.size());
    }

    @Test
    public void testNoResults() {
        List<User> results = searchManager.searchUsers("Creed");
        assertEquals(0, results.size());
    }
}