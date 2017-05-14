package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Journal;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private Accounting accounting;

    public AccountingGUIFrame(String title) {
        super(title);
    }

    public void setAccounting(Accounting accounting){
        this.accounting = accounting;
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("ACCOUNTING"));
        }
    }

    public void setJournal(Journal journal){
        if(accounting==null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING"));
        } else {
            if (journal == null) {
                setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString());
            } else {
                setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString() + ": " + journal.getName());
            }
        }
    }
}
