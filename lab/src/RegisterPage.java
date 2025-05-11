import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterPage {
    public void show(Stage stage) {
        // UI Fields
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmField = new PasswordField();

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Student", "Teacher", "Technician", "Lab Assistant");

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back to Login");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Confirm Password:"), 0, 4);
        grid.add(confirmField, 1, 4);
        grid.add(new Label("Role:"), 0, 5);
        grid.add(roleBox, 1, 5);
        grid.add(registerBtn, 1, 6);
        grid.add(backBtn, 1, 7);

        // Register action
        registerBtn.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirm = confirmField.getText();
            String role = roleBox.getValue();

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty() || role == null) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            if (!password.equals(confirm)) {
                showAlert("Error", "Passwords do not match.");
                return;
            }

            boolean success = UserData.register(name, email, username, password, role);
            if (success) {
                showAlert("Success", "User registered successfully!");
                new LoginPage().start(stage);
            } else {
                showAlert("Error", "Registration failed. Username may already exist.");
            }
        });

        // Back to login
        backBtn.setOnAction(e -> new LoginPage().start(stage));

        Scene scene = new Scene(grid, 450, 400);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
