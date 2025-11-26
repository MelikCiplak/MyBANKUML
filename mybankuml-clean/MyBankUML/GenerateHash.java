import org.mindrot.jbcrypt.BCrypt;

public class GenerateHash {
    public static void main(String[] args) {
        String[] passwords = {"admin123", "pass123", "password"};
        for (String pw : passwords) {
            String hash = BCrypt.hashpw(pw, BCrypt.gensalt());
            System.out.println(pw + " -> " + hash);
        }
    }
}
