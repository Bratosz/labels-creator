package pl.bratosz.labelscreator.excel.format;

public enum FontName {
    TIMES_NEW_ROMAN("Times New Roman"),
    ARIAL("Arial"),
    TAHOMA("Tahoma");

    private String name;

    FontName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
