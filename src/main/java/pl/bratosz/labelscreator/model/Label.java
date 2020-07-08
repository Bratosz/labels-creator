package pl.bratosz.labelscreator.model;

public class Label {
    String firstName;
    String secondName;
    String fullBoxNumber;

    public Label(String firstName, String secondName, String fullBoxNumber) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.fullBoxNumber = fullBoxNumber;
    }

    public Label(String fullBoxNumber) {
        firstName = "";
        secondName = "";
        this.fullBoxNumber = fullBoxNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFullBoxNumber() {
        return fullBoxNumber;
    }
}
