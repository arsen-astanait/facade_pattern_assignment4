# Assignment 4 - Home Theater Facade

Course: Software Design Patterns (ShP-2216), Astana IT University.

This is Option B from Assignment 4. It demonstrates the Facade structural design pattern in Java 17. A client can start and end a movie without knowing the setup sequence for five separate theater devices.

## What the program does

`HomeTheaterFacade.watchMovie("Interstellar")` lowers the screen, dims the lights, powers on and configures the projector and amplifier, and starts a DVD. `endMovie()` stops the DVD, ejects it, turns off the electronics, raises the screen, and restores the lights. The order matters: for example, the player will not play without a loaded disc, and the projector cannot enter widescreen mode until an input is selected.

The subsystem classes keep their own state and validate operations. If an operation fails while a movie is starting, the facade makes a best-effort attempt to restore standby mode and throws an exception that retains the original cause. The program simulates hardware states; it does not control physical devices.

## Folder structure

```text
src/main/java/hometheater/        one short Java package
  Main.java                  client (uses only HomeTheaterFacade)
  HomeTheaterFacade.java     simplified API and coordination
  HomeTheaterException.java  errors at the facade boundary
  Amplifier.java             amplifier state, input and volume
  DvdPlayer.java             disc and playback state
  Projector.java             input and widescreen state
  Screen.java                screen position
  RoomLights.java            brightness and validation
src/test/java/hometheater/        tests in the same package
  HomeTheaterFacadeTest.java independent, zero-dependency unit tests
diagrams/
  home-theater-uml.png       UML class diagram
  home-theater-uml.puml      editable PlantUML source
run.sh / run.ps1             compile, run and test
```

All Java files use the single package `hometheater`, with matching directories for main code and tests.

## Running the project (Java 17 or newer)

Use IntelliJ IDEA to open this folder and run `hometheater.Main`. Run `hometheater.HomeTheaterFacadeTest` to check the nine unit tests.

Or use a terminal:

On Windows PowerShell:

```powershell
.\run.ps1
```

On macOS/Linux:

```bash
bash run.sh
```

Both scripts compile with `javac --release 17`, run the client and run the tests. No Maven, Gradle or external dependencies are required.

Expected client output:

```text
Now playing: Interstellar
Movie ended. Home theater returned to standby mode.
```

## Where the pattern is used

- **Facade:** `HomeTheaterFacade` exposes only `watchMovie(String)` and `endMovie()` to normal clients.
- **Subsystem:** `Amplifier`, `DvdPlayer`, `Projector`, `Screen`, and `RoomLights` contain the actual device operations.
- **Client:** `Main` constructs and calls only `HomeTheaterFacade`.
- **Independent use:** Each subsystem class is public and can still be used directly by a more advanced client. None of the subsystem classes refers back to the facade.

The package-private dependency-injection constructor in `HomeTheaterFacade` makes it possible to test states and failures. Ordinary clients use the public no-argument constructor.

## Tests and Clean Code

There are nine tests for the start sequence, shutdown, invalid input, duplicate starts, shutdown without an active movie, partial setup failure, direct DVD use, missing disc, and repeated sessions. They use fresh objects, descriptive test names and self-checking assertions.

The report explains six principles based on Chapters 7 (Error Handling), 8 (Boundaries) and 9 (Unit Tests) of *Clean Code*, including short before/after examples.

## Publishing to GitHub

This folder includes an incremental Git history. After creating an **empty** GitHub repository, run these commands in the project folder:

```bash
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```

Replace `YOUR_USERNAME` and `YOUR_REPOSITORY` with your actual repository information. The remote has not been set, so no GitHub URL is claimed here. Add the final repository URL to section 5 of the report before submitting.

Before the defense, run the project yourself and be ready to explain the device order, exception handling, and why the subsystems can still be used without the facade.
