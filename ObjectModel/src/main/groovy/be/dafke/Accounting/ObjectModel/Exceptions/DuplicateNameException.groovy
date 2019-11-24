package be.dafke.Accounting.ObjectModel.Exceptions

class DuplicateNameException extends Throwable{
    DuplicateNameException() {}

    DuplicateNameException(String message) {
        super(message)
    }
}
