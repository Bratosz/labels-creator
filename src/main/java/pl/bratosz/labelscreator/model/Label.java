package pl.bratosz.labelscreator.model;

public class Label {
    String firstName;
    String lastName;
    String fullBoxNumber;
    String plantNumber;
    public Label(
            String firstName, String lastName, String fullBoxNumber, String plantNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullBoxNumber = fullBoxNumber;
        this.plantNumber = plantNumber;
    }

    public Label(String fullBoxNumber, String plantNumber) {
        firstName = "";
        lastName = "";
        this.fullBoxNumber = fullBoxNumber;
        this.plantNumber = plantNumber;
    }

    public Label(String fullBoxNumber) {
        firstName = "";
        lastName = "";
        plantNumber = "";
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

    public String getPlantNumber() { return plantNumber; }
}
