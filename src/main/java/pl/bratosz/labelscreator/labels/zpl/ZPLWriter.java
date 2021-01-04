package pl.bratosz.labelscreator.labels.zpl;

import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.model.Label;

import java.util.List;

public class ZPLWriter {
    private String openLabel;
    private String positionFullNameInOneLine;
    private String positionNameAt1stLine;
    private String positionNameAt2ndLine;
    private String close;
    private String positionSTDBoxNumber;
    private String beginCenteredContent;
    private String fontSize;
    private String positionPlantNumber;
    private String endLabel;

    private ZPLWriter() {

    }

    public static ZPLWriter create() {
        ZPLWriter zplLW = new ZPLWriter();
        zplLW.openLabel = "^XA";
        zplLW.positionFullNameInOneLine = "^FS^CI28^FO16,96^FB448,2,0,C^A0,45,45^FD";
        zplLW.positionNameAt1stLine = "^FS^CI28^FO16,96^FB448,1,0,C^A0,55,55^FD";
        zplLW.positionNameAt2ndLine = "^FS^CI28^FO16,144^FB448,1,0,C^A0,55,55^FD";
        zplLW.close = "^FS";
        zplLW.positionSTDBoxNumber = "^CI28^FO16,216^FB448,1,0,R^A0,70,70^FD";
        zplLW.beginCenteredContent = "^FS^CI28^FO16,140^FB448,2,8,C^A0,120,120^FD";
        zplLW.positionPlantNumber = "^FO16,290^A0N,28,28,^FD";
        zplLW.endLabel = "^XZ";
        return zplLW;
    }

    public String generate(LabelsFormat labelsFormat, List<Label> labels) {
        String labelsToPrint = "";
        switch (labelsFormat) {
            case DOUBLE_NUMBER:
            case SINGLE_NUMBER:
                for (Label l : labels) {
                    labelsToPrint += addLabelWithBoxNumberOnly(l);
                }
                return labelsToPrint;
            default:
                for (Label l : labels) {
                    labelsToPrint += addStandardLabel(l);
                }
                return labelsToPrint;
        }
    }

    public String generate(String content, int fontSize) {
        beginCenteredContent = changeFontSize(beginCenteredContent, fontSize);
        String s;
        s = openLabel
                + beginCenteredContent
                + content
                + close
                + endLabel;
        return s;
    }

    private String changeFontSize(String expression, int fontSize) {
        String firstPart = get1stPartOfExpression(expression);
        String secondPart = prepare2ndPartWithFontSize(fontSize);
        return firstPart + secondPart;
    }

    private String prepare2ndPartWithFontSize(int fontSize) {
        return fontSize + "," + fontSize + "^FD";
    }

    private String get1stPartOfExpression(String expression) {
        int stringBeforeFont = expression.lastIndexOf("^A0,") + 1;
        return beginCenteredContent.substring(0, stringBeforeFont);
    }

    private String addLabelWithBoxNumberOnly(Label l) {
        String s;
        s = openLabel
                + beginCenteredContent
                + l.getFullBoxNumber()
                + close

                + positionPlantNumber
                + l.getPlantNumber()
                + close

                + endLabel;
        return s;
    }



    private String addStandardLabel(Label l) {
        String s;

        s = openLabel
                + positionNameAt1stLine
                + l.getLastName()
                + close

                + positionNameAt2ndLine
                + l.getFirstName()
                + close

                + positionSTDBoxNumber
                + l.getFullBoxNumber()
                + close

                + positionPlantNumber
                + l.getPlantNumber()
                + close

                + endLabel;
        return s;
    }


}
