package pl.bratosz.labelscreator.model;

public class Label {
    String firstName;
    String lastName;
    String fullBoxNumber;
    String cornerContent;
    public Label(
            String firstName, String lastName, String fullBoxNumber, String cornerContent) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullBoxNumber = fullBoxNumber;
        this.cornerContent = cornerContent;
    }

    public Label(String fullBoxNumber, String cornerContent) {
        firstName = "";
        lastName = "";
        this.fullBoxNumber = fullBoxNumber;
        this.cornerContent = cornerContent;
    }

    public Label(String fullBoxNumber) {
        firstName = "";
        lastName = "";
        cornerContent = "";
        this.fullBoxNumber = fullBoxNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullBoxNumber() {
        return fullBoxNumber;
    }

    public String getCornerContent() { return cornerContent; }
}
