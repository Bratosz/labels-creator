package pl.bratosz.labelscreator.excel.format;

public enum Font {
    TIMES_NEW_ROMAN("Times New Roman"),
    TAHOMA("Tahoma");

    private String name;

    Font(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
