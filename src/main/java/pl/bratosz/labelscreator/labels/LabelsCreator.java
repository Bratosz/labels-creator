package pl.bratosz.labelscreator.labels;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.bratosz.labelscreator.labels.format.EditorSpreadSheetType;
import pl.bratosz.labelscreator.labels.format.LabelsOrientation;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.formater.StringFormater;
import pl.bratosz.labelscreator.labels.zpl.ZPLFontSize;
import pl.bratosz.labelscreator.labels.zpl.ZPLWriter;
import pl.bratosz.labelscreator.model.Employee;
import pl.bratosz.labelscreator.model.Label;

import java.util.LinkedList;
import java.util.List;

public class LabelsCreator {

    private LabelsFormat labelsFormat;
    private String plantNumber;

    public LabelsCreator() {
    }

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
        String lockerNumber = employee.getLockerNumber();
        String boxNumber = employee.getBoxNumber();
        return formatContent(
                firstName, lastName, lockerNumber, boxNumber);


    }

    private Label formatContent(
            String firstName, String lastName, String lockerNumber, String boxNumber) {
        switch (labelsFormat) {
            case FIRST_NAME_LETTER: {
                String firstLetterFromFirstName = firstName.substring(0, 1) + ".";
                return createStandardContent(firstLetterFromFirstName, lastName, lockerNumber, boxNumber);
            }
            case LAST_NAME_LETTER: {
                String firstLetterFromLastName = lastName.substring(0, 1) + ".";
                return createStandardContent(firstName, firstLetterFromLastName, lockerNumber, boxNumber);
            }
            case DOUBLE_NUMBER: {
                lockerNumber = addFirstNameAsPrefix(firstName, lockerNumber);
                lockerNumber = addLastNameAsSuffix(lastName, lockerNumber);
                return new Label(createFullBoxNumber(lockerNumber, boxNumber), plantNumber);
            }
            case STANDARD:
            default: {
                return createStandardContent(firstName, lastName, lockerNumber, boxNumber);
            }
        }

    }

    private String addLastNameAsSuffix(String lastName, String lockerNumber) {
        return lockerNumber + lastName;
    }

    private String addFirstNameAsPrefix(String firstName, String lockerNumber) {
        return firstName + lockerNumber;
    }

    private Label createStandardContent(
            String firstName, String lastName, String lockerNumber, String boxNumber) {
        String fullBoxNumber = createFullBoxNumber(lockerNumber, boxNumber);
        StringFormater sf = new StringFormater();
        return new Label(
                sf.capitalizeFirstLetters(firstName),
                sf.capitalizeFirstLetters(lastName),
                fullBoxNumber,
                plantNumber
        );
    }

    private String createFullBoxNumber(String lockerNumber, String boxNumber) {
        return lockerNumber + "/" + boxNumber;
    }

    public String createInZPL2(List<Label> labels) {
        LabelsOrientation defaultOrientation = LabelsOrientation.HORIZONTAL;
        return createInZPL2(labels, defaultOrientation);
    }

    public String createInZPL2(List<Label> labels, LabelsOrientation labelsOrientation) {
        ZPLWriter zplLW = ZPLWriter.create();
        return zplLW.generate(labelsFormat, labels, labelsOrientation);
    }

    public String generateFromCustomString(String content, ZPLFontSize fontSize, int labelsAmount) {
        ZPLWriter writer = ZPLWriter.create();
        String result = "";
        content = writer.generate(content, fontSize);
        for (int i = 0; i < labelsAmount; i++) {
            result += content;
        }
        return result;
    }

    public List<Label> generate(
            int beginLockerNumber,
            int lastLockerNumber,
            int capacity) {
        List<Label> labels = new LinkedList<>();
        switch (labelsFormat) {
            case SINGLE_NUMBER:
                return createLabelsWithSingleNumber(beginLockerNumber, lastLockerNumber);
            case DOUBLE_NUMBER:
                return createLabelsWithDoubleNumber(
                        beginLockerNumber, lastLockerNumber, capacity);
            case DOUBLE_NUMBER_WITH_ORDINAL_NUMBER_IN_CORNER:
                return createLabelsWithDoubleNumberAndOrdinalNumberInCorner(
                        beginLockerNumber,
                        lastLockerNumber,
                        capacity);
            default:
                return labels;
        }
    }

    public List<Label> generateWithCustomBoxesRange(
            int lockerNumber,
            int startingBoxNumber,
            int endBoxNumber) {
        List<Label> labels = new LinkedList<>();
        for (int i = startingBoxNumber; i <= endBoxNumber; i++) {
            String fullBoxNumber = lockerNumber + "/" + i;
            Label l = new Label(fullBoxNumber);
            labels.add(l);
        }
        return labels;
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

    private List<Label> createLabelsWithDoubleNumberAndOrdinalNumberInCorner(
            int lockerNumber,
            int lastLockerNumber,
            int capacity) {
        List<Label> labels = new LinkedList<>();
        do {
            for (int i = 1; i <= capacity; i++) {
                String fullBoxNumber = lockerNumber + "/" + i;
                String ordinalNumber = getOrdinalNumber(lockerNumber, i, capacity);
                labels.add(new Label(fullBoxNumber, ordinalNumber));
            }
            lockerNumber++;
        } while (lockerNumber <= lastLockerNumber);
        return labels;
    }

    private String getOrdinalNumber(int lockerNumber, int i, int capacity) {
        int ordinalNumber = (lockerNumber - 1) * capacity + i;
        if (ordinalNumber < 10) {
            return "  " + ordinalNumber;
        } else if (ordinalNumber < 100) {
            return " " + ordinalNumber;
        } else {
            return String.valueOf(ordinalNumber);
        }
    }

    private List<Label> createLabelsWithSingleNumber(
            Integer beginNumber,
            int endNumber) {
        List<Label> labels = new LinkedList<>();
        do {
            Label l = new Label(beginNumber.toString());
            labels.add(l);
            beginNumber++;
        } while (beginNumber <= endNumber);
        return labels;
    }


}
