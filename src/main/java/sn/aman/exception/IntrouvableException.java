package sn.aman.exception;

public class IntrouvableException extends Exception{
    private String message;
    public IntrouvableException(String message) {
        super(message);
        this.message = message;
    }
}
