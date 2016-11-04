package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Transaction;

import java.util.EventListener;

/**
 * Created by ddanneels on 4/11/2016.
 */
public interface JournalDataChangedListener extends EventListener{
    void fireJournalDataChanged();

    Transaction getTransaction();
}
