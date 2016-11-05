package be.dafke.BusinessActions;

import java.util.EventListener;

/**
 * Created by ddanneels on 5/11/2016.
 */
public interface JournalDataChangeListener extends EventListener{
    void fireJournalDataChanged();
}
