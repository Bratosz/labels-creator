package pl.bratosz.labelscreator.converter;

import org.springframework.stereotype.Component;
import pl.bratosz.labelscreator.labels.format.EditorSpreadSheetType;

@Component
public class EditorTypeConverter extends EnumConverter<EditorSpreadSheetType> {

    public EditorTypeConverter() {
        super(EditorSpreadSheetType.class);
    }
}
