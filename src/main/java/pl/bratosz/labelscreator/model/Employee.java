package pl.bratosz.labelscreator.model;

public class Employee {
    private String firstName;
    private String lastName;
    private int lockerNumber;
    private int boxNumber;

    public Employee(
            String firstName, String lastName, int lockerNumber, int boxNumber) {
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

    public int getLockerNumber() {
        return lockerNumber;
    }

    public int getBoxNumber() {
        return boxNumber;
    }
}
