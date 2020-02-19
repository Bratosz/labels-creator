package pl.bratosz.labelscreator.converter;

import org.springframework.core.convert.converter.Converter;

public abstract class EnumConverter<T extends Enum<T>> implements Converter<String, T> {
    private Class<T> enumClass;

    public EnumConverter(final Class<T> iEnumClass) {
        super();
        enumClass = iEnumClass;
    }

    @Override
    public T convert(String value) {
        for (final T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equals(value)) {
                return enumValue;
            }
        }
        return null;
    }
}
