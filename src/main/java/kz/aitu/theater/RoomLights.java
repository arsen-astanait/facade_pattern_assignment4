package kz.aitu.theater;

public class RoomLights {
    private int brightnessPercent = 100;

    public void setBrightness(int brightnessPercent) {
        if (brightnessPercent < 0 || brightnessPercent > 100) {
            throw new IllegalArgumentException("Brightness must be between 0 and 100.");
        }
        this.brightnessPercent = brightnessPercent;
    }

    public int getBrightnessPercent() {
        return brightnessPercent;
    }
}
