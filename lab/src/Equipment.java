import javafx.beans.property.SimpleStringProperty;

public class Equipment {
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    private final SimpleStringProperty status;

    public Equipment(String name, String type, String status) {
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.status = new SimpleStringProperty(status);
    }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }

    public String getType() { return type.get(); }
    public void setType(String value) { type.set(value); }

    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
}
