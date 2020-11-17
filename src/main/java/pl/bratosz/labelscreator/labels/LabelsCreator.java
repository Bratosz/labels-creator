package pl.bratosz.labelscreator.labels;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.bratosz.labelscreator.labels.format.EditorSpreadSheetType;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.formater.StringFormater;
import pl.bratosz.labelscreator.labels.zpl.ZPL2LabelsWriter;
import pl.bratosz.labelscreator.model.Employee;
import pl.bratosz.labelscreator.model.Label;

import java.util.LinkedList;
import java.util.List;

public class LabelsCreator {

    private LabelsFormat labelsFormat;

    public LabelsCreator(LabelsFormat labelsFormat) {
        this.labelsFormat = labelsFormat;
    }

    public List<Label> create(List<Employee> employees) {
        return prepareLabels(employees);
    }

    public XSSFWorkbook generateSpreadSheetFile(List<Label> labels, EditorSpreadSheetType editorSpreadSheetType) {
        SpreadSheetLabelsWriter spreadSheetLabelsWriter =
                new SpreadSheetLabelsWriter(new LabelsSheetParameters(labelsFormat), editorSpreadSheetType);
        return spreadSheetLabelsWriter.create(labels);
    }

    private List<Label> prepareLabels(List<Employee> employees) {
        List<Label> labels = new LinkedList<>();
        for(Employee employee : employees) {
            labels.add(createLabel(employee));
        }
        return labels;
    }

    private Label createLabel(Employee employee) {
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        int lockerNumber = employee.getLockerNumber();
        int boxNumber = employee.getBoxNumber();
        return formatContent(
                firstName, lastName, lockerNumber, boxNumber);


    }

    private Label formatContent(
            String firstName, String lastName, int lockerNumber, int boxNumber) {
        switch(labelsFormat){
            case STANDARD: {
                return createStandardContent(firstName, lastName, lockerNumber, boxNumber);
            }
            case FIRST_NAME_LETTER: {
                String firstLetterFromName = firstName.substring(0,1) + ".";
                return createStandardContent(firstLetterFromName, lastName, lockerNumber, boxNumber);
            }
            case LAST_NAME_LETTER: {
                String firstLetterFromLastName = lastName.substring(0,1) + ".";
                return createStandardContent(firstLetterFromLastName, firstName, lockerNumber, boxNumber);
            }
            case NUMBERS_ONLY: {
                return new Label(createFullBoxNumber(lockerNumber, boxNumber));
            }
            default: {
                return createStandardContent(firstName, lastName, lockerNumber, boxNumber);
            }
        }

    }

    private Label createStandardContent(
            String firstName, String secondName, int lockerNumber, int boxNumber) {
        String fullBoxNumber = createFullBoxNumber(lockerNumber, boxNumber);
        StringFormater sf = new StringFormater();
        return new Label(
                sf.capitalizeFirstLetters(firstName),
                sf.capitalizeFirstLetters(secondName),
                fullBoxNumber
        );
    }

    private String createFullBoxNumber(int lockerNumber, int boxNumber) {
        return lockerNumber + "/" + boxNumber;
    }


    public String generateInZPL2(List<Label> labels) {
        ZPL2LabelsWriter zplLW = ZPL2LabelsWriter.createWithStandardFormat();
        return zplLW.generate(labelsFormat, labels);
    }
}
