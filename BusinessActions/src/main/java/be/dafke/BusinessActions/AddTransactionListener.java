package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Transaction;

import java.util.EventListener;

/**
 * Created by ddanneels on 4/11/2016.
 */
public interface AddTransactionListener extends EventListener{
    void addTransactionToJournal(Transaction transaction, Journal journal);

//    Transaction getCurrentTransaction();
}
