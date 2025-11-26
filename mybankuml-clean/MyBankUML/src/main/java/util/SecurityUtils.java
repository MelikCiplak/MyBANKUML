package util;

import org.mindrot.jbcrypt.BCrypt;
import java.util.UUID;

public class SecurityUtils {

    /**
     * Hashes a password using BCrypt with a secure salt.
     * @param plainTextPassword The user's input password.
     * @return The hashed string.
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Verifies a candidate password against the stored hash.
     * @param candidate The input password.
     * @param hash The stored hash from the database.
     * @return true if matches.
     */
    public static boolean checkPassword(String candidate, String hash) {
        return BCrypt.checkpw(candidate, hash);
    }

    /**
     * Generates a unique string ID for users/transactions.
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}