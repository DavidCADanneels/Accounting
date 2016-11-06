package be.dafke.BusinessActions;

import java.util.EventListener;

/**
 * Created by ddanneels on 4/11/2016.
 */
public interface TransactionDataChangedListener extends EventListener{
    void fireTransactionDataChanged();
}
