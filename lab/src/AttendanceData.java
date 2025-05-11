import java.sql.*;
import java.time.LocalDate;

public class AttendanceData {

    public static void addAttendance(String studentName, LocalDate date, String status) {
        String sql = "INSERT INTO attendance (student_name, date, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentName);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setString(3, status);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAttendance(String studentName, LocalDate date, String status) {
        String sql = "UPDATE attendance SET status = ? WHERE student_name = ? AND date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, studentName);
            stmt.setDate(3, Date.valueOf(date));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAttendance(String studentName, LocalDate date) {
        String sql = "DELETE FROM attendance WHERE student_name = ? AND date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentName);
            stmt.setDate(2, Date.valueOf(date));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String fetchAttendanceReport() {
        StringBuilder report = new StringBuilder();
        String sql = "SELECT date, student_name, status FROM attendance ORDER BY date, student_name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            LocalDate lastDate = null;
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                String student = rs.getString("student_name");
                String status = rs.getString("status");

                if (!date.equals(lastDate)) {
                    report.append("Date: ").append(date).append("\n");
                    lastDate = date;
                }
                report.append("  ").append(student).append(" - ").append(status).append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report.toString();
    }
}
