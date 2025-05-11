import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class MaintenanceLogPage {

    private TableView<MaintenanceLog> tableView;
    private final String DB_URL = "jdbc:mysql://localhost:3306/lab_management";
    private final String DB_USER = "root";
    private final String DB_PASS = "Bankai@123"; // Replace with your MySQL password

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label title = new Label("Maintenance Logs");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tableView = new TableView<>();

        TableColumn<MaintenanceLog, String> typeCol = new TableColumn<>("Type");
        TableColumn<MaintenanceLog, String> nameCol = new TableColumn<>("Name");
        TableColumn<MaintenanceLog, Date> dateCol = new TableColumn<>("Date");
        TableColumn<MaintenanceLog, String> notesCol = new TableColumn<>("Notes");

        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        tableView.getColumns().addAll(typeCol, nameCol, dateCol, notesCol);
        tableView.setItems(loadLogsFromDatabase());

        Button addBtn = new Button("Add Log");
        Button backBtn = new Button("Back");

        addBtn.setOnAction(e -> showAddDialog());
        backBtn.setOnAction(e -> new MaintenancePage().show(stage));

        HBox buttonBox = new HBox(10, addBtn, backBtn);

        root.getChildren().addAll(title, tableView, buttonBox);

        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.setTitle("Maintenance Log");
        stage.show();
    }

    private ObservableList<MaintenanceLog> loadLogsFromDatabase() {
        ObservableList<MaintenanceLog> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM maintenance_logs";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new MaintenanceLog(
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getDate("maintenance_date"),
                        rs.getString("notes")
                ));
            }
        } catch (SQLException e) {
            showAlert("Error loading logs: " + e.getMessage());
        }

        return list;
    }

    private void showAddDialog() {
        Dialog<MaintenanceLog> dialog = new Dialog<>();
        dialog.setTitle("Add Maintenance Log");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Lab", "Equipment", "Software");
        typeBox.setValue("Lab");

        TextField nameField = new TextField();
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextArea notesArea = new TextArea();

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Type:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Name:"), 0, 1); grid.add(nameField, 1, 1);
        grid.add(new Label("Date:"), 0, 2); grid.add(datePicker, 1, 2);
        grid.add(new Label("Notes:"), 0, 3); grid.add(notesArea, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new MaintenanceLog(
                        typeBox.getValue(),
                        nameField.getText(),
                        java.sql.Date.valueOf(datePicker.getValue()),
                        notesArea.getText()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(log -> {
            insertLog(log);
            tableView.setItems(loadLogsFromDatabase());
        });
    }

    private void insertLog(MaintenanceLog log) {
        String query = "INSERT INTO maintenance_logs (type, name, maintenance_date, notes) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, log.getType());
            stmt.setString(2, log.getName());
            stmt.setDate(3, log.getMaintenanceDate());
            stmt.setString(4, log.getNotes());

            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Error adding log: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class MaintenanceLog {
        private final String type;
        private final String name;
        private final Date maintenanceDate;
        private final String notes;

        public MaintenanceLog(String type, String name, Date maintenanceDate, String notes) {
            this.type = type;
            this.name = name;
            this.maintenanceDate = maintenanceDate;
            this.notes = notes;
        }

        public String getType() { return type; }
        public String getName() { return name; }
        public Date getMaintenanceDate() { return maintenanceDate; }
        public String getNotes() { return notes; }
    }
}
