import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EquipmentData {
    public static List<String> getAllEquipment() {
        List<String> equipmentList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM equipment");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String info = rs.getString("name") + " (" + rs.getString("type") + ")";
                equipmentList.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return equipmentList;
    }

    public static void addEquipment(String name, String type, String status) {
        String sql = "INSERT INTO equipment (name, type, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setString(3, status);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
