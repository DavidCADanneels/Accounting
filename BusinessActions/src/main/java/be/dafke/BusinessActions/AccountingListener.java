package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Accounting;

import java.util.EventListener;

/**
 * Created by ddanneels on 4/11/2016.
 */
public interface AccountingListener extends EventListener{

    void setAccounting(Accounting accounting);
}
