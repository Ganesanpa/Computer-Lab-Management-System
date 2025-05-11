import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reservation {
    private final StringProperty id;
    private final StringProperty lab;
    private final StringProperty date;
    private final StringProperty time;

    public Reservation(String id, String lab, String date, String time) {
        this.id = new SimpleStringProperty(id);
        this.lab = new SimpleStringProperty(lab);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty labProperty() {
        return lab;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty timeProperty() {
        return time;
    }

    public String getId() {
        return id.get();
    }

    public String getLab() {
        return lab.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setLab(String lab) {
        this.lab.set(lab);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setTime(String time) {
        this.time.set(time);
    }
}
