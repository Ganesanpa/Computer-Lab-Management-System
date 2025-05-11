import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class ManageEquipmentPage {

    private TableView<Equipment> tableView;
    private final String DB_URL = "jdbc:mysql://localhost:3306/lab_management";
    private final String DB_USER = "root";
    private final String DB_PASS = "Bankai@123"; // 🔁 Replace with your actual MySQL password

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label title = new Label("Manage Equipment");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tableView = new TableView<>();

        TableColumn<Equipment, String> nameCol = new TableColumn<>("Name");
        TableColumn<Equipment, String> typeCol = new TableColumn<>("Type");
        TableColumn<Equipment, String> statusCol = new TableColumn<>("Status");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(nameCol, typeCol, statusCol);
        tableView.setItems(loadEquipmentFromDatabase());

        // Buttons
        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        Button backBtn = new Button("Back");

        addBtn.setOnAction(e -> showAddDialog());
        editBtn.setOnAction(e -> showEditDialog());
        deleteBtn.setOnAction(e -> deleteSelectedEquipment());
        backBtn.setOnAction(e -> new MaintenancePage().show(stage));

        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn, backBtn);

        root.getChildren().addAll(title, tableView, buttonBox);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Manage Equipment");
        stage.show();
    }

    private void showAddDialog() {
        Dialog<Equipment> dialog = new Dialog<>();
        dialog.setTitle("Add Equipment");

        TextField nameField = new TextField();
        TextField typeField = new TextField();
        TextField statusField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(statusField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Equipment(nameField.getText(), typeField.getText(), statusField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(equipment -> {
            insertEquipment(equipment);
            tableView.setItems(loadEquipmentFromDatabase());
        });
    }

    private void showEditDialog() {
        Equipment selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select equipment to edit.");
            return;
        }

        Dialog<Equipment> dialog = new Dialog<>();
        dialog.setTitle("Edit Equipment");

        TextField nameField = new TextField(selected.getName());
        TextField typeField = new TextField(selected.getType());
        TextField statusField = new TextField(selected.getStatus());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(statusField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Equipment(nameField.getText(), typeField.getText(), statusField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            updateEquipment(selected, updated);
            tableView.setItems(loadEquipmentFromDatabase());
        });
    }

    private void insertEquipment(Equipment equipment) {
        String query = "INSERT INTO equipment (name, type, status) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, equipment.getName());
            stmt.setString(2, equipment.getType());
            stmt.setString(3, equipment.getStatus());
            stmt.executeUpdate();

        } catch (SQLException e) {
            showAlert("Insert error: " + e.getMessage());
        }
    }

    private void deleteSelectedEquipment() {
        Equipment selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an equipment to delete.");
            return;
        }

        String query = "DELETE FROM equipment WHERE name = ? AND type = ? AND status = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, selected.getName());
            stmt.setString(2, selected.getType());
            stmt.setString(3, selected.getStatus());
            stmt.executeUpdate();

            tableView.setItems(loadEquipmentFromDatabase());
        } catch (SQLException e) {
            showAlert("Delete error: " + e.getMessage());
        }
    }

    private void updateEquipment(Equipment oldEq, Equipment newEq) {
        String query = "UPDATE equipment SET name = ?, type = ?, status = ? WHERE name = ? AND type = ? AND status = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newEq.getName());
            stmt.setString(2, newEq.getType());
            stmt.setString(3, newEq.getStatus());
            stmt.setString(4, oldEq.getName());
            stmt.setString(5, oldEq.getType());
            stmt.setString(6, oldEq.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Update error: " + e.getMessage());
        }
    }

    private ObservableList<Equipment> loadEquipmentFromDatabase() {
        ObservableList<Equipment> list = FXCollections.observableArrayList();
        String query = "SELECT name, type, status FROM equipment";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                String status = rs.getString("status");
                list.add(new Equipment(name, type, status));
            }

        } catch (SQLException e) {
            showAlert("Database error: " + e.getMessage());
        }

        return list;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class Equipment {
        private final String name;
        private final String type;
        private final String status;

        public Equipment(String name, String type, String status) {
            this.name = name;
            this.type = type;
            this.status = status;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public String getStatus() { return status; }
    }
}
