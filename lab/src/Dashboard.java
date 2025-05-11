import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard {
    public void show(Stage stage) {
        Button labReservation = new Button("Lab Reservation");
        Button attendance = new Button("Attendance");
        Button labStatus = new Button("Lab Status");
        Button maintenance = new Button("Maintenance");

        VBox layout = new VBox(15, labReservation, attendance, labStatus, maintenance);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Main Menu");
        stage.show();
    }
}
