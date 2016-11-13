package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Transaction;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class TransactionActions {

    public static void deleteTransaction(Transaction transaction) {
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);
    }

    public static void moveTransaction(Transaction transaction, Journal oldjournal, Journal newJournal) {
        oldjournal.removeBusinessObject(transaction);
        newJournal.addBusinessObject(transaction);
    }
}
