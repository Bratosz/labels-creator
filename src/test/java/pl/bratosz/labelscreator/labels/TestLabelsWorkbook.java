package pl.bratosz.labelscreator.labels;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestLabelsWorkbook {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private TestLabelsWorkbook(){}

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private XSSFWorkbook workbook;
        private XSSFSheet sheet;

        public Builder workbook(XSSFWorkbook workbook) {
            this.workbook = workbook;
            return this;
        }

        public Builder workbook() {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet();
            return this;
        }

        public Builder headerRow(
                String firstCol, String secondCol, String thirdCol, String fourthCol) {
            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue(firstCol);
            row.createCell(1).setCellValue(secondCol);
            row.createCell(2).setCellValue(thirdCol);
            row.createCell(3).setCellValue(fourthCol);
            return this;
        }

        public Builder addEmployee(
                String firstName, String lastName, int lockerNo, int boxNo) {
            int nextRowIndex = sheet.getPhysicalNumberOfRows();
            XSSFRow row = sheet.createRow(nextRowIndex);
            row.createCell(0).setCellValue(firstName);
            row.createCell(1).setCellValue(lastName);
            row.createCell(2).setCellValue(lockerNo);
            row.createCell(3).setCellValue(boxNo);
            return this;
        }

        public TestLabelsWorkbook build() {
            TestLabelsWorkbook tlb = new TestLabelsWorkbook();
            tlb.workbook = this.workbook;
            tlb.sheet = this.sheet;
            return tlb;
        }


    }

    public void addEmployee(
            String firstName, String lastName, Integer lockerNo, Integer boxNo) {
        int nextRowIndex = sheet.getPhysicalNumberOfRows();
        XSSFRow row = sheet.createRow(nextRowIndex);
        row.createCell(0).setCellValue(firstName);
        row.createCell(1).setCellValue(lastName);
        row.createCell(2).setCellValue(lockerNo);
        row.createCell(3).setCellValue(boxNo);
    }

    public void addNullEmployee() {
        int nextRowIndex = sheet.getPhysicalNumberOfRows();
        sheet.createRow(nextRowIndex);
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }
    public XSSFSheet getSheet() {
        return sheet;
    }
}
