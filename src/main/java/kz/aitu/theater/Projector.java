package kz.aitu.theater;

public class Projector {
    private boolean on;
    private boolean dvdInputSelected;
    private boolean widescreenEnabled;

    public void powerOn() {
        on = true;
    }

    public void selectDvdInput() {
        requirePower();
        dvdInputSelected = true;
    }

    public void enableWidescreenMode() {
        requirePower();
        if (!dvdInputSelected) {
            throw new IllegalStateException("Connect a video source before changing the display mode.");
        }
        widescreenEnabled = true;
    }

    public void powerOff() {
        on = false;
        dvdInputSelected = false;
        widescreenEnabled = false;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isDvdInputSelected() {
        return dvdInputSelected;
    }

    public boolean isWidescreenEnabled() {
        return widescreenEnabled;
    }

    private void requirePower() {
        if (!on) {
            throw new IllegalStateException("The projector must be powered on first.");
        }
    }
}
