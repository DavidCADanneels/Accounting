package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.BusinessModel.VATTransactions;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsTableGUI extends AccountsGUI {
    private final SelectableTable<Account> table;
    private final AccountDataTableModel accountDataTableModel;
    private final AccountFilterPanel filterPanel;

    private AccountsTablePopupMenu popup;
    private JournalInputGUI journalInputGUI;
    private Journals journals;
    private AccountsTableButtons accountsTableButtons;

    private VATTransaction.VATType vatType = null;
    private VATTransactions vatTransactions = null;
    private Contacts contacts = null;


    public AccountsTableGUI(JournalInputGUI journalInputGUI) {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("Accounting").getString("ACCOUNTS")));

        // CENTER
        //
        accountDataTableModel = new AccountDataTableModel();
        table = new SelectableTable<>(accountDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(100, 100));

        this.journalInputGUI=journalInputGUI;
        popup = new AccountsTablePopupMenu(this);
        setPopup(popup);
        // TODO: register popup menu as TransactionListener and remove TransactionListener from 'this'.
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));

        accountsTableButtons = new AccountsTableButtons(this);

        filterPanel = new AccountFilterPanel(accountDataTableModel);

        JScrollPane scrollPane1 = new JScrollPane(table);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);
        add(filterPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(accountsTableButtons,BorderLayout.SOUTH);
	}

	public void showDetails(){
        popup.setVisible(false);
        for(Account account : table.getSelectedObjects()){
            AccountDetails.getAccountDetails(account, journals, journalInputGUI);
        }
    }

    public void setAccounting(Accounting accounting) {
	    // FIXME: enable buttons if something is selected (Listener) or make sure always something is selected
        // for info: the buttons can be used if nothing is selected, their listeners can deal with non-selections
        accountsTableButtons.setActive(accounting!=null);
	    accountDataTableModel.setFilter(null);
        setAccounts(accounting==null?null:accounting.getAccounts());
        setAccountTypes(accounting==null?null:accounting.getAccountTypes());
        setJournals(accounting==null?null:accounting.getJournals());
        setVatTransactions(accounting == null ? null : accounting.getVatTransactions());
        setContacts(accounting == null ? null : accounting.getContacts());
        // if setAccounts() is used here, popup.setAccounts() will be called twice
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));  // TODO: Needed?
        fireAccountDataChanged();
    }

    public void setVatTransactions(VATTransactions vatTransactions) {
        this.vatTransactions = vatTransactions;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType;
    }

    public void book(boolean debit) {
        popup.setVisible(false);
        for(Account account : table.getSelectedObjects()){
            AccountActions.book(journalInputGUI, account, debit, vatType, vatTransactions, accounts, accountTypes, contacts);
        }
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
    }


    public void setAccounts(Accounts accounts) {
        super.setAccounts(accounts);
        accountDataTableModel.setAccounts(accounts);
        filterPanel.clearSearchFields();
        fireAccountDataChanged();
    }

    public void setAccountTypes(AccountTypes accountTypes){
	    super.setAccountTypes(accountTypes);
        accountDataTableModel.setAccountTypes(accountTypes.getBusinessObjects());
        filterPanel.setAccountTypes(accountTypes);
        fireAccountDataChanged();
    }

    public void fireAccountDataChanged() {
        accountDataTableModel.fireTableDataChanged();
    }
}