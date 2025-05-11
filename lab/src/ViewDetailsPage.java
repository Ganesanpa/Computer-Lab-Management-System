import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class ViewDetailsPage {

    private VBox buildReservationView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TableView<Reservation> table = new TableView<>();

        TableColumn<Reservation, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<Reservation, String> labCol = new TableColumn<>("Lab");
        labCol.setCellValueFactory(data -> data.getValue().labProperty());

        TableColumn<Reservation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty());

        TableColumn<Reservation, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(data -> data.getValue().timeProperty());

        table.getColumns().addAll(idCol, labCol, dateCol, timeCol);
        table.getItems().addAll(
            new Reservation("1", "Lab A", "2024-05-01", "10:00 AM"),
            new Reservation("2", "Lab B", "2024-05-02", "2:00 PM")
        );

        vbox.getChildren().add(table);
        return vbox;
    }
    private VBox buildAttendanceView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
    
        TableView<Attendance> table = new TableView<>();
    
        TableColumn<Attendance, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(data -> data.getValue().studentIdProperty());
    
        TableColumn<Attendance, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
    
        TableColumn<Attendance, String> labCol = new TableColumn<>("Lab");
        labCol.setCellValueFactory(data -> data.getValue().labProperty());
    
        TableColumn<Attendance, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());
    
        table.getColumns().addAll(idCol, nameCol, labCol, statusCol);
        table.getItems().addAll(
            new Attendance("S101", "Alice", "Lab A", "Present"),
            new Attendance("S102", "Bob", "Lab B", "Absent")
        );
    
        vbox.getChildren().add(table);
        return vbox;
    }
    private VBox buildReportView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
    
        Label titleLabel = new Label("Report Generation");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    
        Button generateBtn = new Button("Generate Attendance Report");
        TextArea reportArea = new TextArea();
        reportArea.setPrefRowCount(10);
        reportArea.setWrapText(true);
    
        generateBtn.setOnAction(e -> {
            // Dummy content; replace with real report logic later
            reportArea.setText("Attendance Report:\n\n" +
                "S101 - Alice - Lab A - Present\n" +
                "S102 - Bob - Lab B - Absent\n");
        });
    
        vbox.getChildren().addAll(titleLabel, generateBtn, reportArea);
        return vbox;
    }
    
    public void show(Stage stage) {
        TabPane tabPane = new TabPane();

        // Reservation tab
        Tab reservationTab = new Tab("Lab Reservations");
        reservationTab.setContent(buildReservationView());
        reservationTab.setClosable(false);

        // Attendance tab
        Tab attendanceTab = new Tab("Attendance");
        attendanceTab.setContent(buildAttendanceView());
        attendanceTab.setClosable(false);

        // Report tab
        Tab reportTab = new Tab("Report Generation");
        reportTab.setContent(buildReportView());

        reportTab.setClosable(false);

        tabPane.getTabs().addAll(reservationTab, attendanceTab, reportTab);
        
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new MainMenuPage().show(stage));
        VBox root = new VBox(10,tabPane,backButton);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("ViewDetails");
        stage.setScene(scene);
        stage.show();
    }
}
