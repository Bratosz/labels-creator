package pl.bratosz.labelscreator.labels.zpl;

public class ZPLFontSize {
    private int height;
    private int width;
    private int rows;

    public ZPLFontSize (int height, int width, int rows) {
        this.height = height;
        this.width = width;
        this.rows = rows;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getRows() {
        return rows;
    }
}
