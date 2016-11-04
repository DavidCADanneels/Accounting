package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Transaction;

import java.util.EventListener;

/**
 * Created by ddanneels on 4/11/2016.
 */
public interface AddBookingListener extends EventListener{
    void addBookingToTransaction(Booking booking, Transaction transaction);

    Transaction getCurrentTransaction();
}
