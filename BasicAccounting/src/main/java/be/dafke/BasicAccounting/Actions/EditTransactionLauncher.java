package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.JOptionPane;
import java.text.MessageFormat;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class EditTransactionLauncher {

    public void editTransaction(Transaction transaction, Journals journals) {
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);
        journal.setCurrentObject(transaction);
        journals.setCurrentObject(journal);
        String text = getBundle("Accounting").getString("TRANSACTION_REMOVED");
        Object[] messageArguments = {journal.getName()};
        MessageFormat formatter = new MessageFormat(text);
        String output = formatter.format(messageArguments);
        JOptionPane.showMessageDialog(null, output);
        ComponentMap.refreshAllFrames();
    }
}
