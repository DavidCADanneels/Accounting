package be.dafke.BusinessModel;

import java.util.Calendar;
import java.util.function.Predicate;

public class TransactionOfYear implements Predicate<Transaction>{
    private final int year;

    public TransactionOfYear(int year) {
        this.year = year;
    }

    @Override
    public boolean test(Transaction transaction) {
        Calendar date = transaction.getDate();
        int year = date.get(Calendar.YEAR);
        return this.year==year;
    }
}
