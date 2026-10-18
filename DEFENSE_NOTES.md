# Short defense notes

**Why Facade?** Starting a movie requires multiple devices in a specific order. The facade reduces that process to one understandable method for the client.

**Which class is the facade?** `HomeTheaterFacade`. The rest are individual subsystems and `Main` is the client.

**What is actually simplified?** The client does not select device inputs, change brightness, set audio volume or load a disc. It only calls `watchMovie()` or `endMovie()`.

**Why are the subsystem classes independent?** They do not import or mention `HomeTheaterFacade`. Advanced clients can call each component directly when they need finer control.

**Why isn't this just a class that prints messages?** Each component stores its own state. For example, `DvdPlayer.play()` fails unless the player is powered on and has a disc, and `Projector.enableWidescreenMode()` requires a selected input. The tests inspect these states.

**What if setup fails halfway?** `watchMovie()` catches the failure, tries to turn everything back to standby, and throws a `HomeTheaterException` with the original cause. Errors during cleanup are attached as suppressed exceptions.

**What is Chapter 7 about?** Errors should be handled explicitly: validate inputs, preserve useful context, and use exceptions instead of unclear error codes.

**What is Chapter 8 about?** Boundaries between a client and complex or external APIs. Here, the facade is that boundary: client code is independent of the five device interfaces.

**What is Chapter 9 about?** Unit tests. Each test creates fresh objects, checks a clearly named behavior and reports pass/fail without requiring real hardware.

**How is Facade different from Adapter?** Adapter changes an interface so one component can be used where another is expected. Facade offers a simpler entry point to several cooperating components.

**Demo steps:** Run `Main` to show the two facade methods. Then run `HomeTheaterFacadeTest`; all nine tests should pass. Open the UML diagram to explain who depends on whom.
