package be.dafke.BasicAccounting.Accounts.AccountsTable;

import be.dafke.BasicAccounting.Accounts.*;
import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetails;
import be.dafke.BasicAccounting.Accounts.AccountsFilter.AccountFilterPanel;
import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import static be.dafke.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI.showAccountManager;
import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsTableGUI extends JPanel {
    private final SelectableTable<Account> table;
    private final AccountDataTableModel accountDataTableModel;
    private final AccountFilterPanel filterPanel;

    private AccountsTablePopupMenu popup;
    private JournalInputGUI journalInputGUI;
    private Accounts accounts;
    private Journals journals;
    private Journal journal;
    private JournalType journalType;
    private AccountsList accountsList;
    private AccountsTableButtons accountsTableButtons;

    private VATTransaction.VATType vatType = null;
    private VATTransactions vatTransactions = null;
    private Contacts contacts = null;

    public AccountsTableGUI(JournalInputGUI journalInputGUI, boolean left) {
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

        accountsTableButtons = new AccountsTableButtons(this, left);

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

    public void manageAccounts(){
        popup.setVisible(false);
        ArrayList<AccountType> accountTypes = accountsList.getAccountTypes();
        showAccountManager(accounts, accountTypes).setVisible(true);
    }

    public void addAccount(){
        popup.setVisible(false);
        ArrayList<AccountType> accountTypes = accountsList.getAccountTypes();
        new NewAccountGUI(accounts, accountTypes).setVisible(true);
    }

    public void setPopup(AccountsTablePopupMenu popup) {
        this.popup = popup;
    }

    public void setAccounting(Accounting accounting) {
	    // FIXME: enable buttons if something is selected (Listener) or make sure always something is selected
        // for info: the buttons can be used if nothing is selected, their listeners can deal with non-selections
        accountsTableButtons.setActive(accounting!=null);
	    accountDataTableModel.setFilter(null);
        setAccounts(accounting==null?null:accounting.getAccounts());
        setJournals(accounting==null?null:accounting.getJournals());
        setVatTransactions(accounting == null ? null : accounting.getVatTransactions());
        setContacts(accounting == null ? null : accounting.getContacts());
        // if setAccounts() is used here, popup.setAccounts() will be called twice
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));  // TODO: Needed?
        fireAccountDataChanged();
    }
    public void setJournalType(JournalType journalType) {
        this.journalType = journalType;
    }

    public JournalType getJournalType() {
        return journalType;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {

    }
    public void setJournal(Journal journal, boolean left) {
        this.journal = journal;
        if(journal!=null){
            JournalType journalType = journal.getType();
            setJournalType(journalType);

            AccountsList list = left?journalType.getLeft():journalType.getRight();
            setAccountsList(list);
            setVatType(list.getVatType());
        } else {
            // TODO: set null or 'default' type?
//            accountGuiLeft.setJournalType(null);
//            accountGuiRight.setJournalType(null);
            Accounting accounting = Accountings.getActiveAccounting();
            AccountTypes accountTypes = accounting.getAccountTypes();

            AccountsList list = new AccountsList();
            list.addAllTypes(accountTypes, true);
            setAccountsList(list);
            setVatType(null);
        }
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

    public void setAccountTypesList(ArrayList<AccountType> accountTypes) {
        filterPanel.setAccountTypesList(accountTypes);

    }
    public void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList;
        filterPanel.setAccountList(accountsList);
        accountDataTableModel.setAccountList(accountsList);
        accountsTableButtons.setAccountsList(accountsList);
    }

    public void book(boolean debit) {
        popup.setVisible(false);
        for(Account account : table.getSelectedObjects()){
            ArrayList<AccountType> accountTypes = accountsList.getAccountTypes();
            AccountActions.book(journalInputGUI, account, debit, vatType, vatTransactions, accounts, accountTypes, contacts);
        }
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
    }


    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
        accountDataTableModel.setAccounts(accounts);
        filterPanel.clearSearchFields();
        fireAccountDataChanged();
    }

    public void fireAccountDataChanged() {
        int row = table.getSelectedRow();
        accountDataTableModel.fireTableDataChanged();
        if (row != -1) table.setRowSelectionInterval(row, row);
    }
}