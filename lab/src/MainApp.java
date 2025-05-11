import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
         UserData.initialize();
        LoginPage loginPage = new LoginPage();
        loginPage.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
