import util.SecurityUtils;

public class TestHash {
    public static void main(String[] args) {
        String password = "password";
        String hash = SecurityUtils.hashPassword(password);
        System.out.println("Generated hash for 'password': " + hash);
        
        // Test the hash we have in the file
        String existingHash = "$2a$10$N9qo8uLOickgx2ZMRZoMye/JDMgqNMpJnJ7N7aXPY8qQq5nMhz7ZW";
        boolean matches = SecurityUtils.checkPassword("password", existingHash);
        System.out.println("Does 'password' match existing hash? " + matches);
        
        // Try different passwords
        String[] testPasswords = {"password", "admin", "admin123", "password123", ""};
        for (String pw : testPasswords) {
            if (SecurityUtils.checkPassword(pw, existingHash)) {
                System.out.println("MATCH FOUND: '" + pw + "' matches the hash!");
            }
        }
    }
}
