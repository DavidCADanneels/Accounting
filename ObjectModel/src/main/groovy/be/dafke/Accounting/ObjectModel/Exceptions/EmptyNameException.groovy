package be.dafke.Accounting.ObjectModel.Exceptions

class EmptyNameException extends Throwable{
    EmptyNameException() {}

    EmptyNameException(String message) {
        super(message)
    }
}
