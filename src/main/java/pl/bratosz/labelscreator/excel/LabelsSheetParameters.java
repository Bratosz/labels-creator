package pl.bratosz.labelscreator.excel;

import pl.bratosz.labelscreator.excel.format.Font;
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



    public LabelsSheetParameters() {
        sheetName = "Etykiety";
        pageFormat = new A4();
        labelsInRow = 3;
        labelsInColumn = 8;
        setFontName(Font.TIMES_NEW_ROMAN);
        setDefaultFontSize();
    }

    public LabelsSheetParameters(LabelsFormat labelsFormat) {
        setFontName(Font.TIMES_NEW_ROMAN);
        sheetName = "Etykiety";
        pageFormat = new A4();
        labelsInRow = 3;
        labelsInColumn = 8;
        if(labelsFormat.equals(LabelsFormat.NUMBERS_ONLY)){
            setDefaultFontSizeForFormatWithNumbersOnly();
        } else {
            setDefaultFontSize();
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

    public void setFontSize(int fontSize) {
        if(fontSize != 0) {
            this.fontSize = Math.abs(fontSize);
        } else {
            setDefaultFontSize();
        }
    }

    public void setFontName(Font font) {
        fontName = font.getName();
    }

    private void setDefaultFontSize() {
        fontSize = 16;
    }

    private void setDefaultFontSizeForFormatWithNumbersOnly() {
        fontSize = 40;
    }
}
