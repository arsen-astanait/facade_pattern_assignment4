package hometheater;

import java.util.Objects;

public final class HomeTheaterFacade {
    private final Amplifier amplifier;
    private final DvdPlayer dvdPlayer;
    private final Projector projector;
    private final Screen screen;
    private final RoomLights lights;
    private boolean movieInProgress;

    public HomeTheaterFacade() {
        this(new Amplifier(), new DvdPlayer(), new Projector(), new Screen(), new RoomLights());
    }

    // Package-private injection is used by tests, not by the ordinary client.
    HomeTheaterFacade(Amplifier amplifier, DvdPlayer dvdPlayer, Projector projector,
                      Screen screen, RoomLights lights) {
        this.amplifier = Objects.requireNonNull(amplifier, "amplifier");
        this.dvdPlayer = Objects.requireNonNull(dvdPlayer, "dvdPlayer");
        this.projector = Objects.requireNonNull(projector, "projector");
        this.screen = Objects.requireNonNull(screen, "screen");
        this.lights = Objects.requireNonNull(lights, "lights");
    }

    public String watchMovie(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Movie title must not be blank.");
        }
        if (movieInProgress) {
            throw new HomeTheaterException("A movie is already playing. End it first.");
        }

        String movieTitle = title.trim();
        try {
            screen.lower();
            lights.setBrightness(25);
            projector.powerOn();
            projector.selectDvdInput();
            projector.enableWidescreenMode();
            amplifier.powerOn();
            amplifier.selectDvdInput();
            amplifier.setVolume(35);
            dvdPlayer.powerOn();
            dvdPlayer.insertDisc(movieTitle);
            dvdPlayer.play();
            movieInProgress = true;
            return "Now playing: " + movieTitle;
        } catch (RuntimeException problem) {
            restoreStandby(problem);
            throw new HomeTheaterException("Could not start movie '" + movieTitle + "'.", problem);
        }
    }

    public String endMovie() {
        if (!movieInProgress) {
            throw new HomeTheaterException("No movie is currently playing.");
        }

        try {
            dvdPlayer.stop();
            dvdPlayer.ejectDisc();
            dvdPlayer.powerOff();
            amplifier.powerOff();
            projector.powerOff();
            screen.raise();
            lights.setBrightness(100);
            movieInProgress = false;
            return "Movie ended. Home theater returned to standby mode.";
        } catch (RuntimeException problem) {
            restoreStandby(problem);
            throw new HomeTheaterException("Could not shut down the theater completely.", problem);
        }
    }

    private void restoreStandby(RuntimeException originalProblem) {
        attempt(dvdPlayer::stop, originalProblem);
        attempt(dvdPlayer::ejectDisc, originalProblem);
        attempt(dvdPlayer::powerOff, originalProblem);
        attempt(amplifier::powerOff, originalProblem);
        attempt(projector::powerOff, originalProblem);
        attempt(screen::raise, originalProblem);
        attempt(() -> lights.setBrightness(100), originalProblem);
        movieInProgress = false;
    }

    private void attempt(Runnable resetAction, RuntimeException originalProblem) {
        try {
            resetAction.run();
        } catch (RuntimeException resetProblem) {
            originalProblem.addSuppressed(resetProblem);
        }
    }
}
