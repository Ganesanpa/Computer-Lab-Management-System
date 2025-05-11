import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class MaintenancePage {

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label title = new Label("Lab Maintenance");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button labBtn = new Button("Manage Labs");
        Button equipmentBtn = new Button("Manage Equipment");
        Button softwareBtn = new Button("Manage Software");
        Button logsBtn = new Button("Maintenance Logs");
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new MainMenuPage().show(stage));
        // Open ManageLabPage
        labBtn.setOnAction(e -> new ManageLabPage().show(stage));

        // Dummy actions for now
       equipmentBtn.setOnAction(e -> new ManageEquipmentPage().show(stage));

        softwareBtn.setOnAction(e -> new ManageSoftwarePage().show(stage));
        logsBtn.setOnAction(e -> new MaintenanceLogPage().show(stage));


        root.getChildren().addAll(title, labBtn, equipmentBtn, softwareBtn, logsBtn,backButton);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Maintenance");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feature Action");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
