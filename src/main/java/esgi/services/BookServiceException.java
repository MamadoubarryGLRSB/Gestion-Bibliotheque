package esgi.services;

public class BookServiceException extends RuntimeException {
    public BookServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
