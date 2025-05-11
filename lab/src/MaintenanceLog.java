import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MaintenanceLog {
    private final StringProperty type;
    private final StringProperty name;
    private final StringProperty date;
    private final StringProperty notes;

    public MaintenanceLog(String type, String name, String date, String notes) {
        this.type = new SimpleStringProperty(type);
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.notes = new SimpleStringProperty(notes);
    }

    public StringProperty typeProperty() { return type; }
    public StringProperty nameProperty() { return name; }
    public StringProperty dateProperty() { return date; }
    public StringProperty notesProperty() { return notes; }
}
