package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Journal;

import java.util.EventListener;

/**
 * Created by ddanneels on 4/11/2016.
 */
public interface SetJournalListener extends EventListener{

    void setJournal(Journal journal);
}
