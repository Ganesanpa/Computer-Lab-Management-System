import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationData {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lab_management";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Bankai@123";

    public static void addReservation(String labName, Date date, String time, String reservedBy) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO reservations (lab_name, reserved_date, reserved_time, reserved_by) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, labName);
            stmt.setDate(2, date);
            stmt.setString(3, time);
            stmt.setString(4, reservedBy);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllReservations() {
        List<String> reservations = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM reservations";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String record = rs.getString("lab_name") + " | " +
                                rs.getDate("reserved_date") + " | " +
                                rs.getString("reserved_time") + " | " +
                                rs.getString("reserved_by");
                reservations.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}
