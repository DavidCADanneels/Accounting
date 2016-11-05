package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessModel.Accounting;
import be.dafke.ComponentModel.RefreshableFrame;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends RefreshableFrame implements AccountingListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public AccountingGUIFrame(String title) {
        super(title);
    }

    @Override
    public void setAccounting(Accounting accounting){
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("ACCOUNTING"));
        }
    }
}
