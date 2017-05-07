package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
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
    private final AccountDataModel accountDataModel;
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
        accountDataModel = new AccountDataModel();
        table = new SelectableTable<>(accountDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(100, 600));

        this.journalInputGUI=journalInputGUI;
        popup = new AccountsTablePopupMenu(this);
        setPopup(popup);
        // TODO: register popup menu as TransactionListener and remove TransactionListener from 'this'.
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));

        accountsTableButtons = new AccountsTableButtons(this);

        filterPanel = new AccountFilterPanel(accountDataModel);

        JScrollPane scrollPane1 = new JScrollPane(table);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);
        add(filterPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(accountsTableButtons,BorderLayout.SOUTH);
	}

	public void showDetails(){
        for(int i: table.getSelectedRows()){
            Account account = accounts.getBusinessObjects().get(i);
            AccountDetails.getAccountDetails(account, journals, journalInputGUI);
        }
        popup.setVisible(false);
    }

    public void setAccounting(Accounting accounting) {
        accountDataModel.setAccounts(accounting==null?null:accounting.getAccounts());
        filterPanel.setAccountTypes(accounting==null?null:accounting.getAccountTypes());

        setJournals(accounting==null?null:accounting.getJournals());
        setAccounts(accounting==null?null:accounting.getAccounts());
        setAccountTypes(accounting==null?null:accounting.getAccountTypes());
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
        for(int i: table.getSelectedRows()){
            Account account = accounts.getBusinessObjects().get(i);
            AccountActions.book(journalInputGUI, account, debit, vatType, vatTransactions, accounts, accountTypes, contacts);
        }
        popup.setVisible(false);
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
    }


    public void setAccounts(Accounts accounts) {
        super.setAccounts(accounts);
        accountDataModel.setAccounts(accounts);
        fireAccountDataChanged();
    }

    public void fireAccountDataChanged() {
        accountDataModel.fireTableDataChanged();
    }
}