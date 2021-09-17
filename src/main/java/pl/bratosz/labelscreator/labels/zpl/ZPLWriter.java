package pl.bratosz.labelscreator.labels.zpl;

import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.model.Label;

import java.util.List;

public class ZPLWriter {
    private final static int DEFAULT_VERTICAL_ORIGIN = 120;
    private final static int DEFAULT_FONT_HEIGHT = 120;
    private String openLabel;
    private String positionFullNameInOneLine;
    private String positionNameAt1stLine;
    private String positionNameAt1stLineHigher;
    private String positionNameAt2ndLine;
    private String positionNameAt2ndLineLower;
    private String close;
    private String positionSTDBoxNumber;
    private String positionCenteredContent;
    private String positionBIGCenteredContent;
    private String fontSize;
    private String positionPlantNumber;
    private String endLabel;

    private ZPLWriter() {

    }

    public static ZPLWriter create() {
        ZPLWriter zplLW = new ZPLWriter();
        zplLW.openLabel = "^XA";
        zplLW.positionFullNameInOneLine = "^FS^CI28^FO16,106^FB460,2,0,C^A0,55,55^FD";
        zplLW.positionNameAt1stLine = "^FS^CI28^FO16,96^FB460,1,0,C^A0,55,55^FD";
        zplLW.positionNameAt1stLineHigher = "^FS^CI28^FO16,60^FB460,1,0,C^A0,55,55^FD";
        zplLW.positionNameAt2ndLine = "^FS^CI28^FO16,144^FB460,1,0,C^A0,55,55^FD";
        zplLW.positionNameAt2ndLineLower = "^FS^CI28^FO16,200^FB460,1,0,C^A0,55,55^FD";
        zplLW.close = "^FS";
        zplLW.positionSTDBoxNumber = "^CI28^FO16,216^FB448,1,0,R^A0,70,70^FD";
        zplLW.positionCenteredContent = "^FS^CI28^FO16,120^FB470,2,8,C^A0,120,120^FD";
        zplLW.positionBIGCenteredContent = "^FS^CI28^FO16,50^FB470,2,8,C^A0,300,150^FD";
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
                    labelsToPrint += createLabelWithBoxNumberOnly(l);
                }
                return labelsToPrint;
            case LAST_NAME_LETTER:
                for (Label l : labels) {
                    labelsToPrint += createLabelWithLastNameLetter(l);
                }
                return labelsToPrint;
            case FIRST_NAME_LETTER:
                for (Label l : labels) {
                    labelsToPrint += createLabelWithFirstNameLetter(l);
                }
                return labelsToPrint;
            case FIRST_NAME_AND_LAST_NAME:
                for (Label l : labels) {
                    labelsToPrint += createLabelWithFirstNameAndLastNameOnly(l);
                }
                return labelsToPrint;
            default:
                for (Label l : labels) {
                    labelsToPrint += addStandardLabel(l);
                }
                return labelsToPrint;
        }
    }

    public String generate(String content, ZPLFontSize fontSize) {
        positionCenteredContent = changeFontSize(positionCenteredContent, fontSize);
        positionCenteredContent = verticalAlign(positionCenteredContent, fontSize);
        String s;
        s = openLabel
                + positionCenteredContent
                + content
                + close
                + endLabel;
        return s;
    }

    private String changeFontSize(String expression, ZPLFontSize fontSize) {
        String beforeFont = getExpressionBeforeFont(expression);
        String fontDeclaration = prepareFontDeclaration(fontSize);
        String endExpression = "^FD";
        return beforeFont + fontDeclaration + endExpression;

    }

    private String verticalAlign(String expression, ZPLFontSize fontSize) {

        String beforeVAlign = getExpressionBeforeVAlign(expression);
        String afterVAlign = getExpressionAfterVerticalOrigin(expression);

        int actualVerticalOrigin = getVerticalOrigin(expression);
        String verticalOriginValue = resolveVerticalOrigin(
                fontSize.getHeight(), fontSize.getRows(), actualVerticalOrigin);

        return beforeVAlign + verticalOriginValue + afterVAlign;

    }

    private int getVerticalOrigin(String expression) {
        int vOriginBeginIndex = getVOriginBeginIndex(expression);
        int vOriginEndIndex = getVOriginEndIndex(expression);
        return Integer.parseInt(expression.substring(vOriginBeginIndex, vOriginEndIndex));
    }

    private int getVOriginEndIndex(String expression) {
        String vOriginSuffix = "^FB";
        return expression.indexOf(vOriginSuffix) - 1;
    }

    private int getVOriginBeginIndex(String expression) {
        String vOriginPrefix = "^FO16,";
        return expression.indexOf(vOriginPrefix) + vOriginPrefix.length() + 1;
    }

    private String resolveVerticalOrigin(int desiredFontHeight, int rows, int actualVerticalOrigin) {
        int actualFontHeight = resolveFontHeight(actualVerticalOrigin);
        desiredFontHeight = desiredFontHeight * rows;
        if(desiredFontHeight == actualFontHeight) {
            return String.valueOf(actualVerticalOrigin);
        }else if(desiredFontHeight > actualFontHeight) {
            int heightDifference = desiredFontHeight - actualFontHeight;
            return alignVerticallyForFontIncrease(heightDifference, actualVerticalOrigin);
        } else {
            int heightDifference = actualFontHeight - desiredFontHeight;
            return alignVerticallyForFontDecrease(heightDifference, actualVerticalOrigin);
        }
    }

    private String alignVerticallyForFontDecrease(int heightDifference, int actualVerticalOrigin) {
        return Math.round(actualVerticalOrigin + (heightDifference /2.5)) + "";
    }

    private String alignVerticallyForFontIncrease(int heightDifference, int actualVerticalOrigin) {
        return Math.round(actualVerticalOrigin - (heightDifference / 2.5)) + "";
    }

    private int resolveFontHeight(int actualVerticalOrigin) {
        if (actualVerticalOrigin == DEFAULT_VERTICAL_ORIGIN) {
            return DEFAULT_FONT_HEIGHT;
        } else if (fontIsBiggerThanDefault(actualVerticalOrigin)) {
            int vDifference = DEFAULT_VERTICAL_ORIGIN - actualVerticalOrigin;
            return calculateBiggerFont(vDifference);
        } else {
            int vDifference = actualVerticalOrigin - DEFAULT_FONT_HEIGHT;
            return calculateLowerFont(vDifference);
        }
    }

    private int calculateBiggerFont(int vDifference) {
        return  Math.round((float)(DEFAULT_FONT_HEIGHT + (vDifference * 2.5)));
    }

    private int calculateLowerFont(int vDifference) {
        return Math.round((float)(DEFAULT_FONT_HEIGHT - (vDifference * 2.5)));
    }

    private boolean fontIsBiggerThanDefault(int actualVerticalOrigin) {
        if (actualVerticalOrigin < DEFAULT_VERTICAL_ORIGIN) {
            return true;
        } else {
            return false;
        }
    }

    private String getExpressionAfterVerticalOrigin(String expression) {
        String afterVAlign = "^FB";
        return getExpressionWithPrefix(expression, afterVAlign);
    }

    private String getExpressionBeforeVAlign(String expression) {
        String vAlignPrefix = "^FO16,";
        return getExpressionWithSuffix(expression, vAlignPrefix);
    }

    private String prepareFontDeclaration(ZPLFontSize fontSize) {
        return fontSize.getHeight() + "," + fontSize.getWidth();
    }

    private String getExpressionBeforeFont(String expression) {
        String fontPrefix = "^A0,";
        return getExpressionWithSuffix(expression, fontPrefix);
    }

    private String getExpressionWithSuffix(String expression, String suffix) {
        int endOfExpression = expression.indexOf(suffix) + suffix.length();
        return expression.substring(0, endOfExpression);
    }

    private String getExpressionWithPrefix(String expression, String prefix) {
        int beginOfExpression = expression.indexOf(prefix);
        return expression.substring(beginOfExpression);
    }

    private String createLabelWithBoxNumberOnly(Label l) {
        String s;
        s = openLabel
                + positionBIGCenteredContent
                + l.getFullBoxNumber()
                + close

                + positionPlantNumber
                + l.getPlantNumber()
                + close

                + endLabel;
        return s;
    }

    private String createLabelWithFirstNameAndLastNameOnly(Label l) {
        String s;
        s = openLabel
                + positionNameAt1stLineHigher
                + l.getFirstName()
                + close

                + positionNameAt2ndLineLower
                + l.getLastName()
                + close

                + endLabel;
        return s;
    }

    private String createLabelWithFirstNameLetter(Label l) {
        String s;
        s = openLabel
                + positionFullNameInOneLine
                + l.getLastName() + " " + l.getFirstName()
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

    private String createLabelWithLastNameLetter(Label l) {
        String s;
        s = openLabel
                + positionFullNameInOneLine
                + l.getFirstName() + " " + l.getLastName()
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
