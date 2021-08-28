package dev.summerfeeling.betterscreenshot;

/**
 * Created: 01.05.2021
 *
 * @author Summerfeeling
 */
public enum Resolution {

    ORIGINAL(0, 0),
    HQ(720, 480),
    HD(1280, 720),
    FULL_HD(1920, 1080),
    QHD(2560, 1440),
    ULTRA_HD(3840, 2160);

    private final int height;
    private final int width;

    Resolution(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
