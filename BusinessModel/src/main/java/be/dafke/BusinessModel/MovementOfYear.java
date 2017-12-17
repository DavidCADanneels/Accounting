package be.dafke.BusinessModel;

import java.util.Calendar;
import java.util.function.Predicate;

public class MovementOfYear implements Predicate<Movement>{
    private final int year;

    public MovementOfYear(int year) {
        this.year = year;
    }

    @Override
    public boolean test(Movement movement) {
        Calendar date = movement.getDate();
        int year = date.get(Calendar.YEAR);
        return this.year==year;
    }
}
