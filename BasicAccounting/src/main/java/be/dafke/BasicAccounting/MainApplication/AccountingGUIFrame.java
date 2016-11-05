package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessModel.Accounting;
import be.dafke.ComponentModel.RefreshableFrame;

import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends RefreshableFrame implements AccountingListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private ArrayList<AccountingListener> accountingListeners;

    public AccountingGUIFrame(String title) {
        super(title);
    }

    public void setMenuBar(AccountingMenuBar menuBar){
        setJMenuBar(menuBar);
    }

    @Override
    public void setAccounting(Accounting accounting){
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("ACCOUNTING"));
        }
        for(AccountingListener accountingListener:accountingListeners){
            accountingListener.setAccounting(accounting);
        }
    }

    public void setAccountingListeners(ArrayList<AccountingListener> accountingListeners) {
        this.accountingListeners = accountingListeners;
    }
}
