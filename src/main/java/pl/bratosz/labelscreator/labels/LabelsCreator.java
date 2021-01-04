package pl.bratosz.labelscreator.labels;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.bratosz.labelscreator.labels.format.EditorSpreadSheetType;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.formater.StringFormater;
import pl.bratosz.labelscreator.labels.zpl.ZPLWriter;
import pl.bratosz.labelscreator.model.Employee;
import pl.bratosz.labelscreator.model.Label;

import java.util.LinkedList;
import java.util.List;

public class LabelsCreator {

    private LabelsFormat labelsFormat;
    private String plantNumber;

    public LabelsCreator(){}

    public LabelsCreator(LabelsFormat labelsFormat, String plantNumber) {
        this.labelsFormat = labelsFormat;
        this.plantNumber = plantNumber;
    }

    public LabelsCreator(LabelsFormat labelsFormat) {
        this.labelsFormat = labelsFormat;
    }

    public List<Label> generate(List<Employee> employees) {
        return prepareLabels(employees);
    }

    public XSSFWorkbook generateSpreadSheetFile(List<Label> labels, EditorSpreadSheetType editorSpreadSheetType) {
        SpreadSheetLabelsWriter spreadSheetLabelsWriter =
                new SpreadSheetLabelsWriter(new LabelsSheetParameters(labelsFormat), editorSpreadSheetType);
        return spreadSheetLabelsWriter.create(labels);
    }

    private List<Label> prepareLabels(List<Employee> employees) {
        List<Label> labels = new LinkedList<>();
        for (Employee employee : employees) {
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
        switch (labelsFormat) {
            case STANDARD: {
                return createStandardContent(firstName, lastName, lockerNumber, boxNumber);
            }
            case FIRST_NAME_LETTER: {
                String firstLetterFromName = firstName.substring(0, 1) + ".";
                return createStandardContent(firstLetterFromName, lastName, lockerNumber, boxNumber);
            }
            case LAST_NAME_LETTER: {
                String firstLetterFromLastName = lastName.substring(0, 1) + ".";
                return createStandardContent(firstLetterFromLastName, firstName, lockerNumber, boxNumber);
            }
            case DOUBLE_NUMBER: {
                return new Label(createFullBoxNumber(lockerNumber, boxNumber), plantNumber);
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
                fullBoxNumber,
                plantNumber
        );
    }

    private String createFullBoxNumber(int lockerNumber, int boxNumber) {
        return lockerNumber + "/" + boxNumber;
    }


    public String createInZPL2(List<Label> labels) {
        ZPLWriter zplLW = ZPLWriter.create();
        return zplLW.generate(labelsFormat, labels);
    }

    public String generateFromCustomString(String content, int fontSize, int labelsAmount) {
        String result = "";
        ZPLWriter writer = ZPLWriter.create();
        for (int i = 0; i < labelsAmount; i++) {
            result += writer.generate(content, fontSize);
        }
        return result;
    }

    public List<Label> generate(int beginNumber, int endNumber, int capacity) {
        List<Label> labels = new LinkedList<>();
        switch (labelsFormat) {
            case SINGLE_NUMBER:
                return createLabelsWithSingleNumber(beginNumber, endNumber);
            case DOUBLE_NUMBER:
                return createLabelsWithDoubleNumber(
                        beginNumber, endNumber, capacity);
            default:
                return labels;
        }
    }

    private List<Label> createLabelsWithDoubleNumber(
            int beginNumber, int endNumber, int capacity) {
        List<Label> labels = new LinkedList<>();
        do {
            for (int i = 1; i <= capacity; i++) {
                String fullBoxNumber = beginNumber + "/" + i;
                Label l = new Label(fullBoxNumber);
                labels.add(l);
            }
            beginNumber++;
        } while (beginNumber <= endNumber);
        return labels;
    }


    private List<Label> createLabelsWithSingleNumber(
            Integer beginNumber, int endNumber) {
        List<Label> labels = new LinkedList<>();
        do {
            Label l = new Label(beginNumber.toString());
            labels.add(l);
            beginNumber++;
        } while (beginNumber <= endNumber);
        return labels;
    }
}
