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
    private Journal journal;

    public AccountingGUIFrame(String title) {
        super(title);
    }

    public void setAccounting(Accounting accounting){
        this.accounting = accounting;
        journal = null;
        fireDataChanged();
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
        fireDataChanged();
    }

    public void fireDataChanged(){
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
