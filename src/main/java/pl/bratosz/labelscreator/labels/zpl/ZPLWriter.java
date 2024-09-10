package pl.bratosz.labelscreator.labels.zpl;

import pl.bratosz.labelscreator.exception.LabelContentException;
import pl.bratosz.labelscreator.labels.format.LabelsOrientation;
import pl.bratosz.labelscreator.labels.format.labels.LabelSize;
import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.model.Label;
import pl.bratosz.labelscreator.model.Label189;

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
    private String positionBIGCenteredContent60x40;
    private String positionBIGCenteredContent40x20;
    private String fontSize;
    private String positionPlantNumber;
    private String positionOrdinalNumber;
    private String endLabel;
    private String positionForVerticalContentUpTo3Signs;
    private String positionMEDIUMCenteredContent;
    private String positionTopLeftContent;
    private String positionTopRightContent;
    private String positionCenteredBottomContent;
    private String positionCenteredContentFor189;

    private ZPLWriter() {

    }

    public static ZPLWriter create() {
        ZPLWriter zplLW = new ZPLWriter();
        zplLW.openLabel = "^XA";
        zplLW.positionFullNameInOneLine = "^FS^CI28^FO16,106^FB460,2,0,C^A0N,55,55^FD";
        zplLW.positionNameAt1stLine = "^FS^CI28^FO16,96^FB460,1,0,C^A0N,55,55^FD";
        zplLW.positionNameAt1stLineHigher = "^FS^CI28^FO16,60^FB460,1,0,C^A0N,55,55^FD";
        zplLW.positionNameAt2ndLine = "^FS^CI28^FO16,144^FB460,1,0,C^A0N,55,55^FD";
        zplLW.positionNameAt2ndLineLower = "^FS^CI28^FO16,200^FB460,1,0,C^A0N,55,55^FD";
        zplLW.close = "^FS";
        zplLW.positionSTDBoxNumber = "^CI28^FO16,216^FB448,1,0,R^A0N,70,70^FD";
        zplLW.positionCenteredContent = "^FS^CI28^FO16,120^FB470,2,8,C^A0N,120,120^FD";
        zplLW.positionBIGCenteredContent60x40 = "^FS^CI28^FO16,50^FB470,2,8,C^A0N,300,115^FD";
        zplLW.positionBIGCenteredContent40x20 = "^FS^CI28^FO60,25^FB235,2,8,C^A0N,150,58^FD";
        zplLW.positionMEDIUMCenteredContent = "^FS^CI28^FO16,50^FB490,2,8,C^A0N,240,110^FD";
        zplLW.positionPlantNumber = "^FO16,290^A0N,28,28,^FD";
        zplLW.positionOrdinalNumber = "^FO16,240^A0N,90,80,^FD";
        zplLW.endLabel = "^XZ";
        zplLW.positionForVerticalContentUpTo3Signs = "^FS^CI28^FO0,30^FB300,1,0,C^A0R,400,170^FD";

        //189
        zplLW.positionTopLeftContent = "^FS^CI28^FO16,16^FB230,1,0,L^A0N,55,55^FD";
        zplLW.positionTopRightContent = "^FS^CI28^FO225,16^FB230,1,0,R^A0N,55,55^FD";
        zplLW.positionCenteredContentFor189 = "^FS^CI28^FO16,130^FB470,2,8,C^A0N,70,40^FD";
        zplLW.positionCenteredBottomContent = "^FS^CI28^FO16,250^FB460,1,0,C^A0N,50,50^FD";

        return zplLW;
    }

    public String generate(
            LabelsFormat labelsFormat,
            List<Label> labels,
            LabelsOrientation labelsOrientation,
            LabelSize labelSize) {
        String labelsToPrint = "";
        switch (labelsFormat) {
            case DOUBLE_NUMBER:
            case SINGLE_NUMBER:
                for (Label l : labels) {
                    try {
                        labelsToPrint += createLabelWithBoxNumberOnly(l, labelsOrientation, labelSize);
                    } catch (LabelContentException e) {
                    }
                }
                return labelsToPrint;
            case DOUBLE_NUMBER_WITH_ORDINAL_NUMBER_IN_CORNER:
                for (Label l : labels) {
                    try {
                        labelsToPrint += createLabelWithBoxNumberOnlyAndOrdinalNumberInCorner(l, labelsOrientation, labelSize);
                    } catch (LabelContentException e) {
                        continue;
                    }
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

    public String generate(Label189 label189) {
        String middleText = label189.getMiddle();

        if (middleText.length() > 20) {
            positionCenteredContentFor189 = changeFontSize(positionCenteredContentFor189, new ZPLFontSize(30, 30, 1));
        }

        String s;
        s = openLabel +

                positionTopLeftContent +
                label189.getTopLeft() +
                close +

                positionTopRightContent +
                label189.getTopRight() +
                close +

                positionCenteredContentFor189 +
                label189.getMiddle() +
                close +

                positionCenteredBottomContent +
                label189.getBottom() +
                close +

                endLabel;
        return s;
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
        if (desiredFontHeight == actualFontHeight) {
            return String.valueOf(actualVerticalOrigin);
        } else if (desiredFontHeight > actualFontHeight) {
            int heightDifference = desiredFontHeight - actualFontHeight;
            return alignVerticallyForFontIncrease(heightDifference, actualVerticalOrigin);
        } else {
            int heightDifference = actualFontHeight - desiredFontHeight;
            return alignVerticallyForFontDecrease(heightDifference, actualVerticalOrigin);
        }
    }

    private String alignVerticallyForFontDecrease(int heightDifference, int actualVerticalOrigin) {
        return Math.round(actualVerticalOrigin + (heightDifference / 2.5)) + "";
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
        return Math.round((float) (DEFAULT_FONT_HEIGHT + (vDifference * 2.5)));
    }

    private int calculateLowerFont(int vDifference) {
        return Math.round((float) (DEFAULT_FONT_HEIGHT - (vDifference * 2.5)));
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
        String fontPrefix = "^A0N,";
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

    private String createLabelWithBoxNumberOnlyAndOrdinalNumberInCorner(
            Label l, LabelsOrientation labelsOrientation, LabelSize labelSize) throws LabelContentException {
        String s;
        if (labelsOrientation.equals(LabelsOrientation.HORIZONTAL)) {
            if (labelSize.equals(LabelSize.SIZE_60X40)) {
                s = openLabel
                        + positionMEDIUMCenteredContent
                        + l.getFullBoxNumber()
                        + close

                        + positionOrdinalNumber
                        + l.getCornerContent()
                        + close

                        + endLabel;
            } else if (labelSize.equals(LabelSize.SIZE_40X20)) {
                s = openLabel
                        + positionBIGCenteredContent40x20
                        + l.getFullBoxNumber()
                        + close

                        + endLabel;
            } else {
                throw new LabelContentException("Format pionowy nie jest obsługiwany");
            }
        } else {
            throw new LabelContentException("Format pionowy nie jest obsługiwany");
        }
        return s;
    }

    private String createLabelWithBoxNumberOnly(
            Label l, LabelsOrientation labelsOrientation, LabelSize labelSize) throws LabelContentException {
        String s = "";
        String mainContentPosition = getMainContentPosition(labelSize);
        if (labelsOrientation.equals(LabelsOrientation.HORIZONTAL)) {
            s = openLabel
                    + mainContentPosition
                    + l.getFullBoxNumber()
                    + close

                    + positionPlantNumber
                    + l.getCornerContent()
                    + close

                    + endLabel;
        } else if (labelsOrientation.equals(LabelsOrientation.VERTICAL)) {
            if (l.getFullBoxNumber().length() <= 3) {
                s = openLabel
                        + positionForVerticalContentUpTo3Signs
                        + l.getFullBoxNumber()
                        + close

                        + endLabel;
            } else {
                throw new LabelContentException("Content length is bigger than 3");
            }
        }
        return s;
    }

    private String getMainContentPosition(LabelSize labelSize) {
        switch (labelSize) {
            case SIZE_40X20:
                return positionBIGCenteredContent40x20;
            case SIZE_60X40:
            default:
                return positionBIGCenteredContent60x40;
        }
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
                + l.getCornerContent()
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
                + l.getCornerContent()
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
                + l.getCornerContent()
                + close

                + endLabel;
        return s;
    }
}
