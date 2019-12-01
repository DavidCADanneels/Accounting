package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Journal

import javax.swing.*

import static java.util.ResourceBundle.getBundle 

class AccountingGUIFrame extends JFrame {
    Accounting accounting
    Journal journal

    AccountingGUIFrame(String title) {
        super(title)
    }

    void setAccounting(Accounting accounting){
        this.accounting = accounting
        journal = null
        fireDataChanged()
    }

    void setJournal(Journal journal) {
        this.journal = journal
        fireDataChanged()
    }

    void fireDataChanged(){
        if(accounting==null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING"))
        } else {
            if (journal == null) {
                setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString())
            } else {
                setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString() + ": " + journal.name)
            }
        }
    }
}
