package be.dafke.Accounting.Exceptions;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:54
 */
public class DuplicateNameException extends Throwable {
    public DuplicateNameException() {
    }

    public DuplicateNameException(String message) {
        super(message);
    }
}
