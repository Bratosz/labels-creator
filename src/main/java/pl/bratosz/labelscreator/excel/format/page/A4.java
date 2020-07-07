package pl.bratosz.labelscreator.excel.format.page;

public class A4 implements PageSize {
    private static final int HEIGHT_IN_MM = 297;
    private static final int WIDTH_IN_MM = 210;

    public int getWidth() {
        return WIDTH_IN_MM;
    }

    public int getHeight() {
        return HEIGHT_IN_MM;
    }
}
