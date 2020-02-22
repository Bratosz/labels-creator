package pl.bratosz.labelscreator.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.bratosz.labelscreator.excel.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.formater.StringFormater;
import pl.bratosz.labelscreator.model.Employee;

import java.util.LinkedList;
import java.util.List;

public class LabelsCreator {
    private List<String> labels;
    private ExcelLabelsWriter excelLabelsWriter;
    private LabelsFormat labelsFormat;

    public LabelsCreator(LabelsFormat labelsFormat) {
        labels = new LinkedList<>();
        this.labelsFormat = labelsFormat;
        excelLabelsWriter = new ExcelLabelsWriter(new LabelsSheetParameters(labelsFormat));
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
        switch(labelsFormat){
            case STANDARD: {
                return createStandardContent(firstName, lastName, lockerNumber, boxNumber);
            }
            case FIRST_NAME_LETTER: {
                String firstLetterFromName = firstName.substring(0,1) + ".";
                return createStandardContent(firstLetterFromName, lastName, lockerNumber, boxNumber);
            }
            case NUMBERS_ONLY: {
                return createFullBoxNumber(lockerNumber, boxNumber);
            }
            default: {
                return createStandardContent(firstName, lastName, lockerNumber, boxNumber);
            }
        }

    }

    private String createStandardContent(
            String firstName, String lastName, int lockerNumber, int boxNumber) {
        String fullBoxNumber = createFullBoxNumber(lockerNumber, boxNumber);
        StringFormater sf = new StringFormater();
        return "\r\n"
                + sf.capitalizeFirstLetter(firstName) + " "
                + sf.capitalizeFirstLetter(lastName)
                + "\r\n"
                + "                               " + fullBoxNumber;
    }

    private String createFullBoxNumber(int lockerNumber, int boxNumber) {
        return lockerNumber + "/" + boxNumber;
    }
}
