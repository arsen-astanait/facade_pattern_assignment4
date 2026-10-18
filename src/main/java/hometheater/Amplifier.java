package hometheater;

public class Amplifier {
    private boolean on;
    private boolean dvdInputSelected;
    private int volume;

    public void powerOn() {
        on = true;
    }

    public void selectDvdInput() {
        requirePower();
        dvdInputSelected = true;
    }

    public void setVolume(int volume) {
        requirePower();
        if (!dvdInputSelected) {
            throw new IllegalStateException("Select an audio input before setting the volume.");
        }
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100.");
        }
        this.volume = volume;
    }

    public void powerOff() {
        on = false;
        dvdInputSelected = false;
        volume = 0;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isDvdInputSelected() {
        return dvdInputSelected;
    }

    public int getVolume() {
        return volume;
    }

    private void requirePower() {
        if (!on) {
            throw new IllegalStateException("The amplifier must be powered on first.");
        }
    }
}
