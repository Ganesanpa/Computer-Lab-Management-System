import java.sql.*;

public class UserData {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/lab_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Bankai@123"; // Update if needed

    // Call this once during app startup to ensure the table and admin exist
    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100)," +
                    "email VARCHAR(100)," +
                    "username VARCHAR(50) UNIQUE," +
                    "password VARCHAR(100)," +
                    "role VARCHAR(50)" +
                    ")";
            stmt.execute(sql);

            // Ensure default admin exists
            String checkAdmin = "SELECT * FROM users WHERE username = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);

            if (!rs.next()) {
                String insertAdmin = "INSERT INTO users (name, email, username, password, role) " +
                        "VALUES ('Administrator', 'admin@lab.com', 'admin', 'admin123', 'Admin')";
                stmt.execute(insertAdmin);
                System.out.println("Default admin user created.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Used for login
    public static boolean validate(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Used for registration
    public static boolean register(String name, String email, String username, String password, String role) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (name, email, username, password, role) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, username);
            stmt.setString(4, password);
            stmt.setString(5, role);
            stmt.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists.");
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
