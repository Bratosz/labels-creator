package pl.bratosz.labelscreator.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.bratosz.labelscreator.excel.format.EditorSpreadSheetType;
import pl.bratosz.labelscreator.excel.format.page.PageSize;

import java.util.List;

public class ExcelLabelsWriter {
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

    public ExcelLabelsWriter(LabelsSheetParameters parameters, EditorSpreadSheetType editorSpreadSheetType) {
        createFont(parameters);
        pageSize = parameters.getPageSize();
        labelsInRow = parameters.getLabelsInRow();
        labelsInColumn = parameters.getLabelsInColumn();
        sheetName = parameters.getSheetName();
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

    public XSSFWorkbook createLabels(List<String> labels) {
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

    private void writeLabelsInCells(List<String> labels) {
        int labelPointer = 0;
        for (int i = 0; i < labels.size(); i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < labelsInRow; j++) {
                if (labelPointer >= labels.size()) {
                    return;
                }
                String label = labels.get(labelPointer++);
                Cell cell = row.createCell(j);
                cell.setCellValue(label);
                cell.setCellStyle(cellStyle);

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
