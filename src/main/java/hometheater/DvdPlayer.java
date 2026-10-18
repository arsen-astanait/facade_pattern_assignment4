package hometheater;

public class DvdPlayer {
    private boolean on;
    private boolean playing;
    private String discTitle;

    public void powerOn() {
        on = true;
    }

    public void insertDisc(String title) {
        requirePower();
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("A disc title is required.");
        }
        if (discTitle != null) {
            throw new IllegalStateException("Eject the current disc before inserting another.");
        }
        discTitle = title.trim();
    }

    public void play() {
        requirePower();
        if (discTitle == null) {
            throw new IllegalStateException("Insert a disc before pressing play.");
        }
        if (playing) {
            throw new IllegalStateException("The disc is already playing.");
        }
        playing = true;
    }

    public void stop() {
        playing = false;
    }

    public void ejectDisc() {
        if (playing) {
            throw new IllegalStateException("Stop playback before ejecting the disc.");
        }
        discTitle = null;
    }

    public void powerOff() {
        if (playing) {
            throw new IllegalStateException("Stop playback before powering off the player.");
        }
        on = false;
        discTitle = null;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isPlaying() {
        return playing;
    }

    public String getDiscTitle() {
        return discTitle;
    }

    private void requirePower() {
        if (!on) {
            throw new IllegalStateException("The DVD player must be powered on first.");
        }
    }
}
