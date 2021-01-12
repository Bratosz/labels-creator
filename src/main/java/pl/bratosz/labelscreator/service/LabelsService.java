package pl.bratosz.labelscreator.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import pl.bratosz.labelscreator.labels.ExcelEmployeeReader;
import pl.bratosz.labelscreator.labels.ExcelFileStorage;
import pl.bratosz.labelscreator.labels.LabelsCreator;
import pl.bratosz.labelscreator.labels.format.EditorSpreadSheetType;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.labels.zpl.ZPLFontSize;
import pl.bratosz.labelscreator.model.Employee;
import pl.bratosz.labelscreator.model.Label;
import pl.bratosz.labelscreator.notification.SSLMailNotificator;
import pl.bratosz.labelscreator.payload.UploadFileResponse;


import java.util.List;

@Service
public class LabelsService {

    public UploadFileResponse create(
            XSSFWorkbook workbook, LabelsFormat labelsFormat, EditorSpreadSheetType editorSpreadSheetType) {
        ExcelEmployeeReader employeeReader = new ExcelEmployeeReader(workbook);
        List<Employee> loadedEmployees;
        LabelsCreator labelsCreator = new LabelsCreator(labelsFormat);
        ExcelFileStorage fileStorage = new ExcelFileStorage();

        SSLMailNotificator ssl = new SSLMailNotificator();
        ssl.message();

        loadedEmployees = employeeReader.loadEmployees();
        List<Label> labels = labelsCreator.generate(loadedEmployees);
        XSSFWorkbook fileToPrint = labelsCreator.generateSpreadSheetFile(labels, editorSpreadSheetType);

        return fileStorage.storeFile(fileToPrint);
    }

    public UploadFileResponse createSpreadSheet(List<Employee> employees, LabelsFormat labelsFormat, EditorSpreadSheetType editorSpreadSheetType) {
        LabelsCreator labelsCreator = new LabelsCreator(labelsFormat);
        ExcelFileStorage fileStorage = new ExcelFileStorage();

        List<Label> labels = labelsCreator.generate(employees);
        XSSFWorkbook fileToPrint = labelsCreator.generateSpreadSheetFile(labels, editorSpreadSheetType);

        return fileStorage.storeFile(fileToPrint);
    }

    public String createLabelsAsZPL2(LabelsFormat labelsFormat, List<Employee> employees, String plantNumber) {
        LabelsCreator lc = new LabelsCreator(labelsFormat, plantNumber);
        List<Label> labels = lc.generate(employees);
        return lc.createInZPL2(labels);
    }

    public String createNumericLabelsAsZPL2(
            int beginNumber, int endNubmer, int capacity, LabelsFormat labelsFormat) {
        LabelsCreator lc = new LabelsCreator(labelsFormat);
        List<Label> labels = lc.generate(beginNumber, endNubmer, capacity);
        return lc.createInZPL2(labels);
    }


    public String createLabelsFromCustomString(String customString, int labelsAmount) {
        String[] split = customString.split("\\s+");
        ZPLFontSize fontSize = null;
        if ((split.length == 1) && customString.length() <= 4) {
            fontSize = new ZPLFontSize(270,150);
        } else {
            for (int i = 0; i < split.length; i++) {
                int longestWordLength = 0;
                int actualLength = split[i].length();
                if (actualLength >= longestWordLength) longestWordLength = actualLength;
                fontSize = determineFontSize(longestWordLength);
            }
        }
        LabelsCreator lc = new LabelsCreator();
        return lc.generateFromCustomString(customString, fontSize, labelsAmount);
    }

    private ZPLFontSize determineFontSize(int contentLength) {
        if (contentLength <= 6) {
            return new ZPLFontSize(120,120);
        } else if (contentLength <= 8) {
            return new ZPLFontSize(75, 75);
        } else if (contentLength <= 12) {
            return new ZPLFontSize(55,55);
        } else if (contentLength <= 16) {
            return new ZPLFontSize(40,40);
        } else {
            return new ZPLFontSize(30,30);
        }
    }
}

