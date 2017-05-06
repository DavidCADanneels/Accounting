package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.JournalType;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.VATTransaction;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.JSplitPane.BOTTOM;
import static javax.swing.JSplitPane.TOP;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;

/**
 * Created by ddanneels on 28/01/2017.
 */
public class AccountInputPanel extends JPanel{

    private final JournalInputGUI journalInputGUI;
    private AccountTypes accountTypes;
//    private AccountsTableGUI accountsListGUI1, accountsListGUI2;
    private AccountsGUI accountsListGUI1, accountsListGUI2;

    public AccountInputPanel(JournalInputGUI journalInputGUI) {
        this.journalInputGUI = journalInputGUI;
        JSplitPane accountsPanel = createSplitPane();
        setLayout(new BorderLayout());
        add(accountsPanel,CENTER);
    }

    private JSplitPane createSplitPane() {
//        accountsListGUI1 = new AccountsTableGUI(journalInputGUI);
//        accountsListGUI2 = new AccountsTableGUI(journalInputGUI);
        accountsListGUI1 = new AccountsListGUI(journalInputGUI);
        accountsListGUI2 = new AccountsListGUI(journalInputGUI);
        JSplitPane splitPane = new JSplitPane(VERTICAL_SPLIT);
        splitPane.add(accountsListGUI1,TOP);
        splitPane.add(accountsListGUI2,BOTTOM);
        return splitPane;
    }

    public void fireAccountDataChanged() {
        accountsListGUI1.fireAccountDataChanged();
        accountsListGUI2.fireAccountDataChanged();
    }

    // Setters

    public void setAccounting(Accounting accounting) {
        setAccountTypes(accounting == null ? null : accounting.getAccountTypes());
        setJournals(accounting == null ? null : accounting.getJournals());
        accountsListGUI1.setAccounting(accounting);
        accountsListGUI2.setAccounting(accounting);
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }
    public void setJournals(Journals journals){
        setJournal(journals == null ? null : journals.getCurrentObject());
    }
    public void setJournal(Journal journal){
        setJournalType(journal == null ? null : journal.getType());
    }

    public void setJournalType(JournalType journalType) {
        if(journalType==null) {
            accountsListGUI1.setAccountTypes(accountTypes);
            accountsListGUI1.setVatType(null);
            accountsListGUI2.setAccountTypes(accountTypes);
            accountsListGUI2.setVatType(null);
//            setVatType(VATTransaction.VATType.NONE);
        } else {
            accountsListGUI1.setAccountTypes(journalType.getDebetTypes());
            accountsListGUI2.setAccountTypes(journalType.getCreditTypes());
            VATTransaction.VATType vatType = journalType.getVatType();
            if (vatType == VATTransaction.VATType.SALE) {
                accountsListGUI1.setVatType(VATTransaction.VATType.CUSTOMER);
                accountsListGUI2.setVatType(VATTransaction.VATType.SALE);
//                setVatType(VATTransaction.VATType.SALE); // 2 -> BTW
            } else if (vatType == VATTransaction.VATType.PURCHASE) {
                accountsListGUI1.setVatType(VATTransaction.VATType.PURCHASE);
                accountsListGUI2.setVatType(null);
//                setVatType(VATTransaction.VATType.PURCHASE); // 1 -> BTW
            } else {
//                setVatType(VATTransaction.VATType.NONE);
                accountsListGUI1.setVatType(null);
                accountsListGUI2.setVatType(null);
            }
        }
    }
}
