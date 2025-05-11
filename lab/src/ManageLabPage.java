import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ManageLabPage {

    private final ObservableList<String> labs = FXCollections.observableArrayList(
        "Lab A", "Lab B", "Lab C"
    );

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label title = new Label("Manage Labs");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<String> labListView = new ListView<>(labs);
        labListView.setPrefHeight(200);

        Button addBtn = new Button("Add Lab");
        Button editBtn = new Button("Edit Lab");
        Button deleteBtn = new Button("Delete Lab");
        Button backButton = new Button("Back");

        HBox buttonBox = new HBox(10, addBtn, editBtn, deleteBtn);

        addBtn.setOnAction(e -> addLab());
        editBtn.setOnAction(e -> editLab(labListView.getSelectionModel().getSelectedItem()));
        deleteBtn.setOnAction(e -> deleteLab(labListView.getSelectionModel().getSelectedItem()));
        backButton.setOnAction(e -> new MainMenuPage().show(stage));
        root.getChildren().addAll(title, labListView, buttonBox,backButton);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Manage Labs");
        stage.setScene(scene);
        stage.show();
    }

    private void addLab() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Lab");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter lab name:");

        dialog.showAndWait().ifPresent(name -> labs.add(name));
    }

    private void editLab(String selectedLab) {
        if (selectedLab == null) {
            showAlert("Please select a lab to edit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedLab);
        dialog.setTitle("Edit Lab");
        dialog.setHeaderText(null);
        dialog.setContentText("Edit lab name:");

        dialog.showAndWait().ifPresent(newName -> {
            labs.set(labs.indexOf(selectedLab), newName);
        });
    }

    private void deleteLab(String selectedLab) {
        if (selectedLab == null) {
            showAlert("Please select a lab to delete.");
            return;
        }

        labs.remove(selectedLab);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
