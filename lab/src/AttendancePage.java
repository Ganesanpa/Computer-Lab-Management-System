import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;

public class AttendancePage {

    private static final ObservableList<String> students = FXCollections.observableArrayList(
            "Arun", "Divya", "Ravi", "Meena", "Karthik"
    );

    private static void saveAttendanceToFile() {
        try (FileWriter writer = new FileWriter("attendance.csv")) {
            writer.write("Date,Student,Status\n");
            for (Map.Entry<LocalDate, Map<String, String>> entry : attendanceData.entrySet()) {
                LocalDate date = entry.getKey();
                Map<String, String> records = entry.getValue();
                for (Map.Entry<String, String> record : records.entrySet()) {
                    writer.write(date + "," + record.getKey() + "," + record.getValue() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving attendance: " + e.getMessage());
        
        
        }

        
    }
    private static String getAttendanceReport() {
        StringBuilder report = new StringBuilder();
        for (Map.Entry<LocalDate, Map<String, String>> entry : attendanceData.entrySet()) {
            report.append("Date: ").append(entry.getKey()).append("\n");
            for (Map.Entry<String, String> studentRecord : entry.getValue().entrySet()) {
                report.append("  ").append(studentRecord.getKey()).append(" - ").append(studentRecord.getValue()).append("\n");
            }
            report.append("\n");
        }
        return report.toString();
    }
    


    // Outer Map: date -> (student -> status)
    private static final Map<LocalDate, Map<String, String>> attendanceData = new HashMap<>();

    public static void show(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label title = new Label("Student Attendance");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        ComboBox<String> studentSelector = new ComboBox<>(students);
        studentSelector.setPromptText("Select a student");

        ToggleGroup statusGroup = new ToggleGroup();
        RadioButton presentBtn = new RadioButton("Present");
        presentBtn.setToggleGroup(statusGroup);
        RadioButton absentBtn = new RadioButton("Absent");
        absentBtn.setToggleGroup(statusGroup);

        HBox statusBox = new HBox(10, presentBtn, absentBtn);

        Button markBtn = new Button("Mark Attendance");
        Label feedbackLabel = new Label();
        
        Button updateBtn = new Button("Update Attendance");
        Button deleteBtn = new Button("Delete Attendance");
        
        ChangeListener<Object> loadExistingAttendance = (obs, oldVal, newVal) -> {
            LocalDate date = datePicker.getValue();
            String student = studentSelector.getValue();
        
            if (date != null && student != null && attendanceData.containsKey(date)) {
                String status = attendanceData.get(date).get(student);
                if ("Present".equals(status)) {
                    presentBtn.setSelected(true);
                } else if ("Absent".equals(status)) {
                    absentBtn.setSelected(true);
                } else {
                    statusGroup.selectToggle(null);
                }
            } else {
                statusGroup.selectToggle(null);
            }
        };
        
    deleteBtn.setOnAction(e -> {
    LocalDate date = datePicker.getValue();
    String student = studentSelector.getValue();

    if (date == null || student == null) {
        feedbackLabel.setText("Please select date and student.");
        return;
    }

    AttendanceData.deleteAttendance(student, date);
    feedbackLabel.setText("Attendance deleted for " + student + " on " + date);
    statusGroup.selectToggle(null);
});

        
deleteBtn.setOnAction(e -> {
    LocalDate date = datePicker.getValue();
    String student = studentSelector.getValue();

    if (date == null || student == null) {
        feedbackLabel.setText("Please select date and student.");
        return;
    }

    AttendanceData.deleteAttendance(student, date);
    feedbackLabel.setText("Attendance deleted for " + student + " on " + date);
    statusGroup.selectToggle(null);
});



        datePicker.valueProperty().addListener(loadExistingAttendance);
        studentSelector.valueProperty().addListener(loadExistingAttendance);
        

        Button saveBtn = new Button("Save to File");
        saveBtn.setOnAction(e -> {
            saveAttendanceToFile();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Attendance saved to 'attendance.csv'", ButtonType.OK);
            alert.showAndWait();
            
        });
        

        Button viewBtn = new Button("View Attendance");
        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(200);
        
       viewBtn.setOnAction(e -> reportArea.setText(AttendanceData.fetchAttendanceReport()));



      markBtn.setOnAction(e -> {
    LocalDate date = datePicker.getValue();
    String student = studentSelector.getValue();
    RadioButton selectedStatus = (RadioButton) statusGroup.getSelectedToggle();

    if (date == null || student == null || selectedStatus == null) {
        feedbackLabel.setText("Please select all fields.");
        return;
    }

    AttendanceData.addAttendance(student, date, selectedStatus.getText());
    feedbackLabel.setText("Attendance marked for " + student + " on " + date);
});


           

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> MainMenuPage.show(primaryStage));
        
        
        

        HBox editButtons = new HBox(10, updateBtn, deleteBtn);
        layout.getChildren().addAll(title, datePicker, studentSelector, statusBox, markBtn, editButtons, saveBtn, viewBtn, feedbackLabel, reportArea, backBtn);
        

        Scene scene = new Scene(layout, 400, 550);
        primaryStage.setScene(scene);
    }
}
