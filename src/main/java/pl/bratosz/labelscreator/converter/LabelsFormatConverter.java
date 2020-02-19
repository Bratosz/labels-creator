package pl.bratosz.labelscreator.converter;

import org.springframework.stereotype.Component;
import pl.bratosz.labelscreator.excel.format.labels.LabelsFormat;

@Component
public class LabelsFormatConverter extends EnumConverter<LabelsFormat> {
    public LabelsFormatConverter() {
        super(LabelsFormat.class);
    }
}
