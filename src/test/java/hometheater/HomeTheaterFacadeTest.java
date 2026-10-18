package hometheater;

import java.util.Objects;

/** Zero-dependency unit tests: each test creates fresh components. */
public final class HomeTheaterFacadeTest {
    private static int passed;

    public static void main(String[] args) {
        run("watchMovie prepares every component", HomeTheaterFacadeTest::watchMoviePreparesComponents);
        run("endMovie restores initial state", HomeTheaterFacadeTest::endMovieRestoresStandby);
        run("blank title fails before changing devices", HomeTheaterFacadeTest::blankTitleDoesNotStartTheater);
        run("duplicate start keeps the original movie", HomeTheaterFacadeTest::secondStartIsRejected);
        run("end without a movie has a clear error", HomeTheaterFacadeTest::endBeforeStartIsRejected);
        run("subsystem failure cleans up and preserves the cause", HomeTheaterFacadeTest::failureRestoresStandby);
        run("DVD player is usable without the facade", HomeTheaterFacadeTest::dvdPlayerWorksIndependently);
        run("playing a missing disc is rejected", HomeTheaterFacadeTest::cannotPlayWithoutDisc);
        run("new movie can start after ending one", HomeTheaterFacadeTest::moviesCanRunSequentially);
        System.out.println("All " + passed + " tests passed.");
    }

    private static void watchMoviePreparesComponents() {
        Fixture f = new Fixture();
        equal("Now playing: Interstellar", f.facade.watchMovie(" Interstellar "));
        check(f.screen.isLowered(), "Screen was not lowered");
        equal(25, f.lights.getBrightnessPercent());
        check(f.projector.isOn() && f.projector.isDvdInputSelected()
                && f.projector.isWidescreenEnabled(), "Projector is not ready");
        check(f.amplifier.isOn() && f.amplifier.isDvdInputSelected(), "Amplifier is not ready");
        equal(35, f.amplifier.getVolume());
        check(f.dvdPlayer.isPlaying(), "DVD is not playing");
        equal("Interstellar", f.dvdPlayer.getDiscTitle());
    }

    private static void endMovieRestoresStandby() {
        Fixture f = new Fixture();
        f.facade.watchMovie("Interstellar");
        f.facade.endMovie();
        assertStandby(f);
    }

    private static void blankTitleDoesNotStartTheater() {
        Fixture f = new Fixture();
        expect(IllegalArgumentException.class, () -> f.facade.watchMovie("  "));
        assertStandby(f);
    }

    private static void secondStartIsRejected() {
        Fixture f = new Fixture();
        f.facade.watchMovie("Interstellar");
        expect(HomeTheaterException.class, () -> f.facade.watchMovie("Inception"));
        equal("Interstellar", f.dvdPlayer.getDiscTitle());
        check(f.dvdPlayer.isPlaying(), "First movie should keep playing");
    }

    private static void endBeforeStartIsRejected() {
        Fixture f = new Fixture();
        HomeTheaterException error = expect(HomeTheaterException.class, f.facade::endMovie);
        check(error.getMessage().contains("No movie"), "Exception should explain the problem");
        assertStandby(f);
    }

    private static void failureRestoresStandby() {
        Amplifier amplifier = new Amplifier();
        DvdPlayer dvd = new DvdPlayer();
        Projector brokenProjector = new Projector() {
            @Override
            public void enableWidescreenMode() {
                throw new IllegalStateException("Projector lens failed");
            }
        };
        Screen screen = new Screen();
        RoomLights lights = new RoomLights();
        HomeTheaterFacade facade = new HomeTheaterFacade(amplifier, dvd, brokenProjector, screen, lights);

        HomeTheaterException error = expect(HomeTheaterException.class,
                () -> facade.watchMovie("Interstellar"));
        check(error.getMessage().contains("Interstellar"), "Error should include movie title");
        check(error.getCause() instanceof IllegalStateException, "Original failure was lost");
        check(!screen.isLowered(), "Screen should be restored on failure");
        equal(100, lights.getBrightnessPercent());
        check(!amplifier.isOn() && !dvd.isOn() && !brokenProjector.isOn(),
                "Devices should be off after failure");
    }

    private static void dvdPlayerWorksIndependently() {
        DvdPlayer player = new DvdPlayer();
        player.powerOn();
        player.insertDisc("Dune");
        player.play();
        check(player.isPlaying(), "Direct use of DVD player should work");
        player.stop();
        player.ejectDisc();
        player.powerOff();
        check(!player.isOn() && player.getDiscTitle() == null, "DVD player did not shut down");
    }

    private static void cannotPlayWithoutDisc() {
        DvdPlayer player = new DvdPlayer();
        player.powerOn();
        expect(IllegalStateException.class, player::play);
    }

    private static void moviesCanRunSequentially() {
        Fixture f = new Fixture();
        f.facade.watchMovie("Interstellar");
        f.facade.endMovie();
        equal("Now playing: Inception", f.facade.watchMovie("Inception"));
        equal("Inception", f.dvdPlayer.getDiscTitle());
    }

    private static void assertStandby(Fixture f) {
        check(!f.screen.isLowered(), "Screen must be raised");
        equal(100, f.lights.getBrightnessPercent());
        check(!f.projector.isOn(), "Projector must be off");
        check(!f.amplifier.isOn(), "Amplifier must be off");
        check(!f.dvdPlayer.isOn() && !f.dvdPlayer.isPlaying(), "DVD player must be idle");
        equal(null, f.dvdPlayer.getDiscTitle());
    }

    private static <T extends Throwable> T expect(Class<T> type, Runnable action) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (type.isInstance(actual)) {
                return type.cast(actual);
            }
            throw new AssertionError("Expected " + type.getSimpleName()
                    + " but got " + actual.getClass().getSimpleName(), actual);
        }
        throw new AssertionError("Expected " + type.getSimpleName() + " but nothing was thrown.");
    }

    private static void equal(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected '" + expected + "', actual '" + actual + "'.");
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void run(String name, Runnable test) {
        test.run();
        passed++;
        System.out.println("PASS: " + name);
    }

    private static final class Fixture {
        final Amplifier amplifier = new Amplifier();
        final DvdPlayer dvdPlayer = new DvdPlayer();
        final Projector projector = new Projector();
        final Screen screen = new Screen();
        final RoomLights lights = new RoomLights();
        final HomeTheaterFacade facade = new HomeTheaterFacade(
                amplifier, dvdPlayer, projector, screen, lights);
    }
}
