package pl.bratosz.labelscreator.labels.zpl;

import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.model.Label;

import java.util.List;

public class ZPL2LabelsWriter {
    private String openLabel;
    private String positionFullNameInOneLine;
    private String positionName1stLine;
    private String positionName2ndLine;
    private String close;
    private String positionSTDBoxNumber;
    private String positionCenteredBoxNumber;
    private String positionPlantNumber;
    private String endLabel;

    private ZPL2LabelsWriter() {

    }

    public static ZPL2LabelsWriter createWithStandardLabelSize() {
        ZPL2LabelsWriter zplLW = new ZPL2LabelsWriter();
        zplLW.openLabel = "^XA";
        zplLW.positionFullNameInOneLine = "^FS^CI28^FO16,96^FB448,2,0,C^A0,45,45^FD";
        zplLW.positionName1stLine = "^FS^CI28^FO16,96^FB448,1,0,C^A0,55,55^FD";
        zplLW.positionName2ndLine = "^FS^CI28^FO16,144^FB448,1,0,C^A0,55,55^FD";
        zplLW.close = "^FS";
        zplLW.positionSTDBoxNumber = "^CI28^FO16,216^FB448,1,0,R^A0,70,70^FD";
        zplLW.positionCenteredBoxNumber = "^FS^CI28^FO16,140^FB448,2,8,C^A0,120,120^FD";
        zplLW.positionPlantNumber = "^FO16,300^A0N,28,28,^FD";
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

    private String addLabelWithBoxNumberOnly(Label l) {
        String s;
        s = openLabel
                + positionCenteredBoxNumber
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
                + positionName1stLine
                + l.getLastName()
                + close

                + positionName2ndLine
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
