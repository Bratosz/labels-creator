package pl.bratosz.labelscreator.labels.zpl;

public class ZPLFontSize {
    private int height;
    private int width;

    public ZPLFontSize (int height, int width) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
