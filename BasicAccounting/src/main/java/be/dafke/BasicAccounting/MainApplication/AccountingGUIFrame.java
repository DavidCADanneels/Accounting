package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public AccountingGUIFrame(String title) {
        super(title);
    }

    public void setAccounting(Accounting accounting){
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("ACCOUNTING"));
        }
    }
}
