package pl.bratosz.labelscreator.excel;

import pl.bratosz.labelscreator.excel.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.excel.format.page.A4;
import pl.bratosz.labelscreator.excel.format.page.PageFormat;

public class LabelsSheetParameters {
    private int fontSize;
    private String fontName;
    private String sheetName;
    private PageFormat pageFormat;
    private int labelsInRow;
    private int labelsInColumn;

    public LabelsSheetParameters(LabelsFormat labelsFormat) {
        fontName = "Times New Roman";
        sheetName = "Etykiety";
        pageFormat = new A4();
        labelsInRow = 3;
        labelsInColumn = 8;
        if(labelsFormat.equals(LabelsFormat.NUMBERS_ONLY)){
            fontSize = 40;
        } else {
            fontSize = 16;
        }
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
