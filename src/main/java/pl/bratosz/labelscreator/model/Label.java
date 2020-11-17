package pl.bratosz.labelscreator.model;

public class Label {
    String firstName;
    String lastName;
    String fullBoxNumber;

    public Label(String firstName, String lastName, String fullBoxNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullBoxNumber = fullBoxNumber;
    }

    public Label(String fullBoxNumber) {
        firstName = "";
        lastName = "";
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
}
