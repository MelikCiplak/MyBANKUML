import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import data.JsonFileService;
import application.*;
import presentation.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting MyBankUML Backend...");

        // 1. Initialize Database Layer (Loads JSON files)
        JsonFileService database = new JsonFileService();

        // 2. Initialize Application Logic Layer (Managers)
        AuthenticationManager authMgr = new AuthenticationManager(database);
        RoleManager roleMgr = new RoleManager();
        AccountManager accountMgr = new AccountManager(database);
        TransactionManager txMgr = new TransactionManager(database);
        SearchManager searchMgr = new SearchManager(database);
        AdminManager adminMgr = new AdminManager(database);

        // 3. Initialize Presentation Layer (Controllers)
        AuthController authController = new AuthController(authMgr);
        AccountController accountController = new AccountController(accountMgr, txMgr, authMgr, roleMgr);
        AdminController adminController = new AdminController(adminMgr, authMgr, roleMgr);
        SearchController searchController = new SearchController(searchMgr, authMgr, roleMgr);

        // 4. Configure and Start Web Server
        Javalin app = Javalin.create(config -> {
            // Enables Cross-Origin Resource Sharing (useful for local testing)
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
            // Serve the frontend files from src/main/resources/public
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(8080);

        // 5. Register API Routes
        
        // --- Auth ---
        app.post("/api/login", authController::login);
        app.post("/api/logout", authController::logout);

        // --- Account & Transactions ---
        app.post("/api/transaction", accountController::handleTransaction);
        
        // --- Admin ---
        app.post("/api/admin/create-user", adminController::createUser);
        app.patch("/api/admin/users/{id}", adminController::updateUser);
        
        // --- Search (Added) ---
        app.get("/api/search", searchController::searchUsers);

        System.out.println("Backend running on http://localhost:8080");
    }
}