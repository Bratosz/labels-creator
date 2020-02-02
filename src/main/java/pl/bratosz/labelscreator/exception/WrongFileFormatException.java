package pl.bratosz.labelscreator.exception;

import java.io.IOException;

public class WrongFileFormatException extends IOException {
    public WrongFileFormatException (String message) {
        super(message);
    }
}
