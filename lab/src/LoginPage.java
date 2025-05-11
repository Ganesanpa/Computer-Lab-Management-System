import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPage {
    public void start(Stage stage) {
        // UI Elements
        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        Text errorText = new Text();
        errorText.setStyle("-fx-fill: red;");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(userLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginBtn, 1, 2);
        grid.add(registerBtn, 1, 3);
        grid.add(errorText, 1, 4);

        // Login action
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorText.setText("Please fill in all fields.");
                return;
            }

            boolean valid = UserData.validate(username, password);
            if (valid) {
                MainMenuPage.show(stage);
            } else {
                errorText.setText("Invalid username or password.");
            }
        });

        // Register button
        registerBtn.setOnAction(e -> new RegisterPage().show(stage));

        Scene scene = new Scene(grid, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }
}
