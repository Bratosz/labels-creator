package pl.bratosz.labelscreator.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.bratosz.labelscreator.formater.StringFormater;
import pl.bratosz.labelscreator.model.Employee;

import java.util.LinkedList;
import java.util.List;

public class LabelsCreator {
    private List<String> labels;
    private ExcelLabelsWriter excelLabelsWriter;
    private String fileName;

    public LabelsCreator() {
        labels = new LinkedList<>();
        excelLabelsWriter = new ExcelLabelsWriter(new LabelsSheetParameters());
        fileName = "Etykiety";
    }

    public XSSFWorkbook create(List<Employee> employees) {
        prepareLabels(employees);
        return excelLabelsWriter.createLabels(labels);
    }

    private void prepareLabels(List<Employee> employees) {
        for(Employee employee : employees) {
            labels.add(createLabel(employee));
        }
    }

    private String createLabel(Employee employee) {
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        int lockerNumber = employee.getLockerNumber();
        int boxNumber = employee.getBoxNumber();
        return formatContent(
                firstName, lastName, lockerNumber, boxNumber);


    }

    private String formatContent(
            String firstName, String lastName, int lockerNumber, int boxNumber) {
        String fullBoxNumber = lockerNumber + "/" + boxNumber;
        StringFormater sf = new StringFormater();
        return "\r\n"
                + sf.capitalizeFirstLetter(firstName) + " "
                + sf.capitalizeFirstLetter(lastName)
                + "\r\n"
                + "                               " + fullBoxNumber;
    }
}
