package pl.bratosz.labelscreator.excel.format;

public class A4 implements PageFormat {
    private static final int HEIGHT = 297;
    private static final int WIDTH = 210;

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}
