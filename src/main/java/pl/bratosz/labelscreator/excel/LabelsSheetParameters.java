package pl.bratosz.labelscreator.excel;

import pl.bratosz.labelscreator.excel.format.A4;
import pl.bratosz.labelscreator.excel.format.PageFormat;

public class LabelsSheetParameters {
    private int fontSize;
    private String fontName;
    private String sheetName;
    private PageFormat pageFormat;
    private int labelsInRow;
    private int labelsInColumn;

    public LabelsSheetParameters() {
        fontSize = 16;
        fontName = "Times New Roman";
        sheetName = "Etykiety";
        pageFormat = new A4();
        labelsInRow = 3;
        labelsInColumn = 8;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getFontName() {
        return fontName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public PageFormat getPageFormat() {
        return pageFormat;
    }

    public int getLabelsInRow() {
        return labelsInRow;
    }

    public int getLabelsInColumn() {
        return labelsInColumn;
    }
}
