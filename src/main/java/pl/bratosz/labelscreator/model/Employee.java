package pl.bratosz.labelscreator.model;

public class Employee {
    private String firstName;
    private String lastName;
    private String lockerNumber;
    private String boxNumber;

    public Employee(
            String firstName, String lastName, String lockerNumber, String boxNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.lockerNumber = lockerNumber;
        this.boxNumber = boxNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public String getLockerNumber() {
        return lockerNumber;
    }
}
