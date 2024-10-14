package pl.bratosz.labelscreator.service;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.bratosz.labelscreator.model.Label189;

import java.util.ArrayList;
import java.util.List;

public class Label189Creator {
    public static List<Label189> get(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowsAmount = getActualNumberOfRows(sheet);

        List<Label189> labels = new ArrayList<>();

        for (int i = 0; i < rowsAmount; i++) {
            XSSFRow row = sheet.getRow(i);
            String topLeft = getTopLeft(row);
            String topRight = getTopRight(row);
            String middle = getMiddle(row);
            String bottom = getBottom(row);
            Label189 label = new Label189(topLeft, topRight, middle, bottom);
            labels.add(label);
        }

        return labels;
    }

    private static int getActualNumberOfRows(XSSFSheet sheet) {
        int rowsAmount = sheet.getPhysicalNumberOfRows();
        int actualRowsAmount = 0;
        for (int i = 0; i < rowsAmount; i++) {
            if (sheet.getRow(i ) != null && sheet.getRow(i).getCell(0) != null) {
                actualRowsAmount++;
            } else {
                break;
            }
        }
        return actualRowsAmount;
    }

    private static String getTopLeft(XSSFRow row) {
        return row.getCell(0).getRawValue();
    }

    private static String getTopRight(XSSFRow row) {
        String first = row.getCell(1).getStringCellValue();
        String second = row.getCell(2).getRawValue();
        String third = row.getCell(3).getRawValue();
        return first + '-' + second + '/' + third;
    }

    private static String getMiddle(XSSFRow row) {
        String lastName = row.getCell(4).getStringCellValue();
        String firstName = row.getCell(5).getStringCellValue();
        return lastName + ' ' + firstName;
    }

    private static String getBottom(XSSFRow row) {
        String first = row.getCell(6).getRawValue();
        String second = row.getCell(7).getRawValue();
        second = addMissingZeros(second);
        return first + '/' + second;
    }

    private static String addMissingZeros(String s) {
        if (s.length() == 1) {
            return "00" + s;
        } else if (s.length() == 2) {
            return "0" + s;
        } else {
            return s;
        }
    }
}
