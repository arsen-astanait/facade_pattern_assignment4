package kz.aitu.theater;

public class HomeTheaterException extends RuntimeException {
    public HomeTheaterException(String message) {
        super(message);
    }

    public HomeTheaterException(String message, Throwable cause) {
        super(message, cause);
    }
}
