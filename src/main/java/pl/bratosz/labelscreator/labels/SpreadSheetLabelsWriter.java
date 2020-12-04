package pl.bratosz.labelscreator.labels;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import pl.bratosz.labelscreator.labels.format.EditorSpreadSheetType;
import pl.bratosz.labelscreator.labels.format.FontName;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.labels.format.page.PageSize;
import pl.bratosz.labelscreator.model.Label;

import java.util.List;

public class SpreadSheetLabelsWriter {
    private static final float CONVERSION_RATE_FOR_COLUMN_WIDTH = 0.00765613f;
    private static final float CONVERSION_RATE_FOR_ROW_HEIGHT_LIBRE_OFFICE = 0.35266666f;
    private static final float CONVERSION_RATE_FOR_ROW_HEIGHT_EXCEL = 0.33147321f;
    private String sheetName;
    private CellStyle cellStyle;
    private Font font;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private PageSize pageSize;
    private int labelsInRow;
    private int labelsInColumn;
    private int numberOfRows;
    private EditorSpreadSheetType editorSpreadSheetType;
    private LabelsFormat labelsFormat;


    public SpreadSheetLabelsWriter(LabelsSheetParameters sheetParameters, EditorSpreadSheetType editorSpreadSheetType) {
        labelsFormat = sheetParameters.getLabelsFormat();
        createFont(sheetParameters);
        pageSize = sheetParameters.getPageSize();
        labelsInRow = sheetParameters.getLabelsInRow();
        labelsInColumn = sheetParameters.getLabelsInColumn();
        sheetName = sheetParameters.getSheetName();
        sheet = workbook.createSheet(sheetName);
        setPrintParameters();
        this.editorSpreadSheetType = editorSpreadSheetType;
    }

    private void createFont(LabelsSheetParameters parameters) {
        workbook = new XSSFWorkbook();
        font = workbook.createFont();
        font.setFontHeightInPoints((short) parameters.getFontSize());
        font.setFontName(parameters.getFontName());
    }

    public XSSFWorkbook create(List<Label> labels) {
        createLabelsStyle();
        setColumnsAndRowsSize();
        createRequiredNumberOfRows(labels.size());
        writeLabelsInCells(labels);
        return workbook;
    }


    private void setPrintParameters() {
        sheet.setMargin(Sheet.RightMargin, 0.0d);
        sheet.setMargin(Sheet.LeftMargin, 0.0d);
        sheet.setMargin(Sheet.BottomMargin, 0.0d);
        sheet.setMargin(Sheet.TopMargin, 0.0d);
        sheet.setFitToPage(true);
        XSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setPaperSize(PaperSize.A4_PAPER);
        ps.setFitWidth((short) 1);
        ps.setFitHeight((short) 0);
    }

    private void createLabelsStyle() {
        cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);
    }

    private void setColumnsAndRowsSize() {
        int pageWidth = pageSize.getWidth();
        int pageHeight = pageSize.getHeight();
        setColumnsWidth(pageWidth);
        setRowsHeight(pageHeight);
    }

    private void createRequiredNumberOfRows(int size) {
        calculateNumberOfRows(size);
        for (int i = 0; i < numberOfRows; i++) {
            sheet.createRow(i);
        }
    }

    private void writeLabelsInCells(List<Label> labels) {
        int labelPointer = 0;
        XSSFFont fontForName = workbook.createFont();
        XSSFFont fontForBoxNumber = workbook.createFont();
        fontForName.setFontName(FontName.ARIAL.getName());
        fontForName.setFontHeightInPoints((short) 16);
        fontForBoxNumber = workbook.createFont();
        fontForBoxNumber.setFontName(FontName.ARIAL.getName());
        fontForBoxNumber.setFontHeightInPoints((short) 18);
        fontForBoxNumber.setBold(true);
        for (int i = 0; i < labels.size(); i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < labelsInRow; j++) {
                if (labelPointer >= labels.size()) {
                    return;
                }
                if(labelsFormat == LabelsFormat.DOUBLE_NUMBER) {
                    String number = labels.get(labelPointer++).getFullBoxNumber();
                    Cell cell = row.createCell(j);
                    cell.setCellValue(number);
                    cell.setCellStyle(cellStyle);
                } else {
                    Label label = labels.get(labelPointer++);
                    XSSFRichTextString content = new XSSFRichTextString(
                            "\r\n" +
                            label.getFirstName() + " " + label.getLastName()
                            + "\r\n\r\n");
                    content.applyFont(fontForName);
                    content.append("                        " + label.getFullBoxNumber(), fontForBoxNumber);
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(content);
                }


            }
        }
    }

    private void calculateNumberOfRows(int numberOfLabels) {
        numberOfRows = (int) Math.ceil((float)numberOfLabels / labelsInRow);
    }

    private void setColumnsWidth(int pageWidth) {
        int columnWidthInMM = calculateSingleColumnWidth(pageWidth);
        int columnWidthInPoints = (int) convertMillimetersToPointsForWidth(columnWidthInMM);

        for (int i = 0; i < labelsInRow; i++) {
            sheet.setColumnWidth(i, columnWidthInPoints);
        }
    }
    private void setRowsHeight(int height) {
        float rowHeight = calculateSingleRowHeight(height);
        float rowHeightInPoints = convertMillimetersToPointsForHeight(rowHeight);
        sheet.setDefaultRowHeightInPoints(rowHeightInPoints);

    }

    private int calculateSingleColumnWidth(int pageWidth) {
        return (pageWidth / labelsInRow);
    }

    private float calculateSingleRowHeight(float pageHeight) {
        return pageHeight / labelsInColumn;
    }

    private float convertMillimetersToPointsForWidth(float width) {
        return width / CONVERSION_RATE_FOR_COLUMN_WIDTH;
    }

    private float convertMillimetersToPointsForHeight(float height) {
        if(editorSpreadSheetType.equals(EditorSpreadSheetType.EXCEL)) {
            return height / CONVERSION_RATE_FOR_ROW_HEIGHT_EXCEL;
        } else {
            return height / CONVERSION_RATE_FOR_ROW_HEIGHT_LIBRE_OFFICE;
        }
    }
}
