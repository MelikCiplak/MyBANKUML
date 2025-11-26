package presentation;

import application.AuthenticationManager;
import application.RoleManager;
import application.SearchManager;
import io.javalin.http.Context;
import model.User;

import java.util.List;
import java.util.Map;

public class SearchController {
    private SearchManager searchManager;
    private AuthenticationManager authManager;
    private RoleManager roleManager;

    public SearchController(SearchManager searchManager, AuthenticationManager authManager, RoleManager roleManager) {
        this.searchManager = searchManager;
        this.authManager = authManager;
        this.roleManager = roleManager;
    }

    public void searchUsers(Context ctx) {
        try {
            // 1. Verify Authentication
            String token = ctx.header("Authorization");
            User currentUser = authManager.getUserByToken(token);

            // 2. Verify Role (RBAC) - Only Teller or Admin can search
            if (!roleManager.canAccess(currentUser, RoleManager.Feature.SEARCH_CUSTOMERS)) {
                throw new Exception("Access Denied: Insufficient Permissions");
            }

            // 3. Get Query Parameter (e.g., GET /api/search?q=Smith)
            String query = ctx.queryParam("q");
            
            // 4. Execute Search (Logic Layer)
            List<User> results = searchManager.searchUsers(query);

            // 5. Return Results
            ctx.json(results);

        } catch (Exception e) {
            // Return 403 for permission errors, 400 for bad requests
            int status = e.getMessage().contains("Access Denied") ? 403 : 400;
            ctx.status(status).json(Map.of("error", e.getMessage()));
        }
    }
}