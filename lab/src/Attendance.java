import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Attendance {
    private final StringProperty studentId;
    private final StringProperty name;
    private final StringProperty lab;
    private final StringProperty status;

    public Attendance(String studentId, String name, String lab, String status) {
        this.studentId = new SimpleStringProperty(studentId);
        this.name = new SimpleStringProperty(name);
        this.lab = new SimpleStringProperty(lab);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty studentIdProperty() { return studentId; }
    public StringProperty nameProperty() { return name; }
    public StringProperty labProperty() { return lab; }
    public StringProperty statusProperty() { return status; }
}
