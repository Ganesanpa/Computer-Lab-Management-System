import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class ManageSoftwarePage {

    private TableView<Software> tableView;
    private final String DB_URL = "jdbc:mysql://localhost:3306/lab_management";
    private final String DB_USER = "root";
    private final String DB_PASS = "your_password"; // Replace with your actual password

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label title = new Label("Manage Software");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tableView = new TableView<>();

        TableColumn<Software, String> nameCol = new TableColumn<>("Name");
        TableColumn<Software, String> versionCol = new TableColumn<>("Version");
        TableColumn<Software, String> licenseCol = new TableColumn<>("License");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        versionCol.setCellValueFactory(new PropertyValueFactory<>("version"));
        licenseCol.setCellValueFactory(new PropertyValueFactory<>("license"));

        tableView.getColumns().addAll(nameCol, versionCol, licenseCol);
        tableView.setItems(loadSoftwareFromDatabase());

        // Buttons
        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        Button backBtn = new Button("Back");

        addBtn.setOnAction(e -> showAddDialog());
        editBtn.setOnAction(e -> showEditDialog());
        deleteBtn.setOnAction(e -> deleteSelectedSoftware());
        backBtn.setOnAction(e -> new MaintenancePage().show(stage));

        HBox buttonBox = new HBox(10, addBtn, editBtn, deleteBtn, backBtn);

        root.getChildren().addAll(title, tableView, buttonBox);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Manage Software");
        stage.show();
    }

    private ObservableList<Software> loadSoftwareFromDatabase() {
        ObservableList<Software> list = FXCollections.observableArrayList();
        String query = "SELECT name, version, license FROM software";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Software(
                        rs.getString("name"),
                        rs.getString("version"),
                        rs.getString("license")
                ));
            }
        } catch (SQLException e) {
            showAlert("Database error: " + e.getMessage());
        }

        return list;
    }

    private void showAddDialog() {
        Dialog<Software> dialog = new Dialog<>();
        dialog.setTitle("Add Software");

        TextField nameField = new TextField();
        TextField versionField = new TextField();
        TextField licenseField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Version:"), 0, 1); grid.add(versionField, 1, 1);
        grid.add(new Label("License:"), 0, 2); grid.add(licenseField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Software(nameField.getText(), versionField.getText(), licenseField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(software -> {
            insertSoftware(software);
            tableView.setItems(loadSoftwareFromDatabase());
        });
    }

    private void insertSoftware(Software software) {
        String query = "INSERT INTO software (name, version, license) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, software.getName());
            stmt.setString(2, software.getVersion());
            stmt.setString(3, software.getLicense());
            stmt.executeUpdate();

        } catch (SQLException e) {
            showAlert("Insert error: " + e.getMessage());
        }
    }

    private void showEditDialog() {
        Software selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select software to edit.");
            return;
        }

        Dialog<Software> dialog = new Dialog<>();
        dialog.setTitle("Edit Software");

        TextField nameField = new TextField(selected.getName());
        TextField versionField = new TextField(selected.getVersion());
        TextField licenseField = new TextField(selected.getLicense());

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Version:"), 0, 1); grid.add(versionField, 1, 1);
        grid.add(new Label("License:"), 0, 2); grid.add(licenseField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Software(nameField.getText(), versionField.getText(), licenseField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            updateSoftware(selected, updated);
            tableView.setItems(loadSoftwareFromDatabase());
        });
    }

    private void updateSoftware(Software oldSoft, Software newSoft) {
        String query = "UPDATE software SET name = ?, version = ?, license = ? WHERE name = ? AND version = ? AND license = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newSoft.getName());
            stmt.setString(2, newSoft.getVersion());
            stmt.setString(3, newSoft.getLicense());
            stmt.setString(4, oldSoft.getName());
            stmt.setString(5, oldSoft.getVersion());
            stmt.setString(6, oldSoft.getLicense());

            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Update error: " + e.getMessage());
        }
    }

    private void deleteSelectedSoftware() {
        Software selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select software to delete.");
            return;
        }

        String query = "DELETE FROM software WHERE name = ? AND version = ? AND license = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, selected.getName());
            stmt.setString(2, selected.getVersion());
            stmt.setString(3, selected.getLicense());

            stmt.executeUpdate();
            tableView.setItems(loadSoftwareFromDatabase());

        } catch (SQLException e) {
            showAlert("Delete error: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class Software {
        private final String name;
        private final String version;
        private final String license;

        public Software(String name, String version, String license) {
            this.name = name;
            this.version = version;
            this.license = license;
        }

        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getLicense() { return license; }
    }
}
