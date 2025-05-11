import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LabReservationPage {
    public static void show(Stage stage) {
        Label title = new Label("Lab Reservation");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        ComboBox<String> labBox = new ComboBox<>();
        labBox.getItems().addAll("Lab 1", "Lab 2", "Lab 3");
        labBox.setPromptText("Select Lab");

        DatePicker datePicker = new DatePicker();

        TextField timeField = new TextField();
        timeField.setPromptText("Enter time (e.g. 10:00 AM)");

        Button submitBtn = new Button("Reserve");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> MainMenuPage.show(stage)); // navigate back


        Label resultLabel = new Label();

        submitBtn.setOnAction(e -> {
            String name = nameField.getText();
            String lab = labBox.getValue();
            String date = (datePicker.getValue() != null) ? datePicker.getValue().toString() : "";
            String time = timeField.getText();

            if (name.isEmpty() || lab == null || date.isEmpty() || time.isEmpty()) {
                resultLabel.setText("Please fill in all fields.");
            } else {
               try {
    java.sql.Date sqlDate = java.sql.Date.valueOf(date);
    ReservationData.addReservation(lab, sqlDate, time, name);
    resultLabel.setText("Reservation saved for " + name + " on " + date + " at " + time + " in " + lab);
    // Optional: clear fields after success
    nameField.clear();
    labBox.setValue(null);
    datePicker.setValue(null);
    timeField.clear();
} catch (Exception ex) {
    resultLabel.setText("Failed to save reservation: " + ex.getMessage());
    ex.printStackTrace();
}

                // Here you can add logic to save to a file or database
            }
        });

        GridPane form = new GridPane();
        form.setPadding(new Insets(15));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Lab:"), 0, 1);
        form.add(labBox, 1, 1);
        form.add(new Label("Date:"), 0, 2);
        form.add(datePicker, 1, 2);
        form.add(new Label("Time:"), 0, 3);
        form.add(timeField, 1, 3);
        form.add(submitBtn, 1, 4);
        form.add(resultLabel, 1, 5);

        VBox root = new VBox(10, title, form);
        root.setPadding(new Insets(15));

        HBox buttons = new HBox(10, submitBtn, backBtn);
        form.add(buttons, 1, 4);
        
       ;



        Scene scene = new Scene(root, 500, 350);
        stage.setScene(scene);
        stage.setTitle("Lab Reservation");
        stage.show();
    }
}
