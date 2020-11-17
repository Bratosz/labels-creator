package pl.bratosz.labelscreator.labels.zpl;

import pl.bratosz.labelscreator.labels.format.labels.LabelsFormat;
import pl.bratosz.labelscreator.model.Label;

import java.util.List;

public class ZPL2LabelsWriter {
    private String openLabel;
    private String stdNamePrefix;
    private String close;
    private String stdNumberPrefix;
    private String centeredNumberPrefix;
    private String endLabel;

    private ZPL2LabelsWriter() {

    }

    public static ZPL2LabelsWriter createWithStandardFormat() {
        ZPL2LabelsWriter zplLW = new ZPL2LabelsWriter();
        zplLW.openLabel = "^XA";
        zplLW.stdNamePrefix = "^CW0,E:CEARIALBD.FNT^FS^CI28^FO16,96^FB448,2,8,C^A0,45,45^FD";
        zplLW.close = "^FS";
        zplLW.stdNumberPrefix = "^CI28^FO16,216^FB448,1,0,R^A0,70,70^FD";
        zplLW.centeredNumberPrefix = "^CW0,E:CEARIALBD.FNT^FS^CI28^FO16,140^FB448,2,8,C^A0,120,120^FD";
        zplLW.endLabel = "^XZ";
        return zplLW;
    }

    public String generate(LabelsFormat labelsFormat, List<Label> labels) {
        String labelsToPrint = "";
        switch (labelsFormat) {
            case NUMBERS_ONLY:
                for (Label l : labels) {
                    labelsToPrint += addNumberOnlyLabel(l);
                }
                return labelsToPrint;
            default:
                for (Label l : labels) {
                    labelsToPrint += addStandardLabel(l);
                }
                return labelsToPrint;
        }
    }

    private String addNumberOnlyLabel(Label l) {
        String s;
        s = openLabel
                + centeredNumberPrefix
                + l.getFullBoxNumber()
                + close
                + endLabel;
        return s;
    }

    private String addStandardLabel(Label l) {
        String s;
        s = openLabel
                + stdNamePrefix
                + l.getFirstName() + " " + l.getLastName()
                + close
                + stdNumberPrefix
                + l.getFullBoxNumber()
                + close
                + endLabel;
        return s;
    }


}
