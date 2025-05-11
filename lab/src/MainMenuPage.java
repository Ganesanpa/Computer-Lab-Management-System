import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;

public class MainMenuPage {
    public static void show(Stage stage) {
        // Main layout
        BorderPane root = new BorderPane();

        // Sidebar menu (VBox)
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #f0f0f0; -fx-pref-width: 180px;");

        // Buttons
        Button reservationBtn = new Button("Lab Reservation");
        Button attendanceBtn = new Button("Attendance");
        Button viewDetailsBtn = new Button("View Details");
        Button maintenanceBtn = new Button("Maintenance");
        Button manageLabBtn = new Button("Manage Lab");
        Button logoutBtn = new Button("Logout");

        // Styling all buttons
        for (Button btn : new Button[]{reservationBtn, attendanceBtn, viewDetailsBtn, maintenanceBtn, manageLabBtn, logoutBtn}) {
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle("-fx-font-size: 14px;");
        }

        // Add buttons to sidebar
        sidebar.getChildren().addAll(
            reservationBtn, attendanceBtn, viewDetailsBtn, maintenanceBtn, manageLabBtn, logoutBtn
        );

        // Load image from resources
        ImageView imageView = null;
        InputStream imageStream = MainMenuPage.class.getResourceAsStream("/images/com.png");
        if (imageStream != null) {
            Image image = new Image(imageStream);
            imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
        } else {
            System.out.println("❌ Image not found: /images/com.png");
        }

        // Welcome message
        Text welcomeText = new Text("Welcome to the Computer Lab Management System");
        welcomeText.setStyle("-fx-font-size: 18px;");
        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(20));
        if (imageView != null) centerBox.getChildren().add(imageView);
        centerBox.getChildren().add(welcomeText);

        // Layout setup
        root.setLeft(sidebar);
        root.setCenter(centerBox);

        // Button Actions
        reservationBtn.setOnAction(e -> LabReservationPage.show(stage));
        attendanceBtn.setOnAction(e -> AttendancePage.show(stage));
        viewDetailsBtn.setOnAction(e -> new ViewDetailsPage().show(stage));
        maintenanceBtn.setOnAction(e -> new MaintenancePage().show(stage));
        manageLabBtn.setOnAction(e -> new ManageLabPage().show(stage));
        logoutBtn.setOnAction(e -> new LoginPage().start(stage));

        // Set scene and show
        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Main Menu");
        stage.show();
    }
}
