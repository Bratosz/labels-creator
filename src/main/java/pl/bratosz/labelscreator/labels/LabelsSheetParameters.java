package pl.bratosz.labelscreator.labels;

import pl.bratosz.labelscreator.labels.format.FontName;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.labels.format.page.A4;
import pl.bratosz.labelscreator.labels.format.page.PageSize;

public class LabelsSheetParameters {
    private int fontSize;
    private String fontName;
    private String sheetName;
    private PageSize pageSize;
    private int labelsInRow;
    private int labelsInColumn;
    private LabelsFormat labelsFormat;



    public LabelsSheetParameters() {
        sheetName = "Etykiety";
        pageSize = new A4();
        labelsInRow = 3;
        labelsInColumn = 8;
        setFontName(FontName.ARIAL);
        setDefaultFontSize();
    }

    public LabelsSheetParameters(LabelsFormat labelsFormat) {
        this.labelsFormat = labelsFormat;
        setFontName(FontName.ARIAL);
        sheetName = "Etykiety";
        pageSize = new A4();
        labelsInRow = 3;
        labelsInColumn = 8;
        if(labelsFormat.equals(LabelsFormat.DOUBLE_NUMBER)){
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

    public PageSize getPageSize() {
        return pageSize;
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

    public LabelsFormat getLabelsFormat() {
        return labelsFormat;
    }

    public void setFontName(FontName fontName) {
        this.fontName = fontName.getName();
    }

    private void setDefaultFontSize() {
        fontSize = 16;
    }

    private void setDefaultFontSizeForFormatWithNumbersOnly() {
        fontSize = 40;
    }
}
