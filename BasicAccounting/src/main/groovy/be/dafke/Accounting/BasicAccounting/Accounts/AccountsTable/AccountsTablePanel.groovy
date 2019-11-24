package be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable

import be.dafke.Accounting.BusinessModel.*
import be.dafke.BasicAccounting.Accounts.AccountActions
import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI
import be.dafke.BasicAccounting.Accounts.AccountsFilter.AccountFilterPanel
import be.dafke.BasicAccounting.Accounts.NewAccountDialog
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.BusinessModelDao.AccountingSession
import be.dafke.BusinessModelDao.JournalSession
import be.dafke.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AccountsTablePanel extends JPanel {
    private final SelectableTable<Account> table
    private final AccountDataTableModel accountDataTableModel
    private final AccountFilterPanel filterPanel

    private AccountsTablePopupMenu popup
    private Accounting accounting
    private Journal journal
    private JournalType journalType
    private AccountsList accountsList
    private AccountsTableButtons accountsTableButtons

    private VATTransaction.VATType vatType = null

    AccountsTablePanel(boolean left) {
        setLayout(new BorderLayout())
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("Accounting").getString("ACCOUNTS")))

        // CENTER
        //
        accountDataTableModel = new AccountDataTableModel(left)
        table = new SelectableTable<>(accountDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(100, 100))

        popup = new AccountsTablePopupMenu(this)
        setPopup(popup)
        // TODO: register popup menu as TransactionListener and remove TransactionListener from 'this'.
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table))

        accountsTableButtons = new AccountsTableButtons(this, left)

        filterPanel = new AccountFilterPanel(accountDataTableModel, left)

        JScrollPane scrollPane1 = new JScrollPane(table)
        JPanel center = new JPanel()

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS))
        center.add(scrollPane1)
        add(filterPanel, BorderLayout.NORTH)
        add(center, BorderLayout.CENTER)
        add(accountsTableButtons,BorderLayout.SOUTH)
    }

//    void setJournal(Journal journal) {
//        this.journal = journal
////        filterPanel.setJournal(journal)
//        filterPanel.setJournalSession(journalSession)
//        accountDataTableModel.setJournal(journal)
//    }

    void showDetails(){
        popup.setVisible(false)
        for(Account account : table.getSelectedObjects()){
            Point location = getLocationOnScreen()
            AccountDetailsGUI.getAccountDetails(location, account, accounting.getJournals())
        }
    }

    void manageAccounts(){
        popup.setVisible(false)
        ArrayList<AccountType> accountTypes = accountsList.getAccountTypes()
        AccountManagementGUI accountManagementGUI = AccountManagementGUI.getInstance(accounting.getAccounts(), accountTypes)
        accountManagementGUI.setLocation(getLocationOnScreen())
        accountManagementGUI.setVisible(true)
    }

    void addAccount(){
        popup.setVisible(false)
        ArrayList<AccountType> accountTypes = accountsList.getAccountTypes()
        NewAccountDialog newAccountDialog = new NewAccountDialog(accounting.getAccounts(), accountTypes)
        newAccountDialog.setLocation(getLocation())
        newAccountDialog.setVisible(true)
    }

    void editAccount(){
        popup.setVisible(false)
        Account account = table.getSelectedObject()
        if(account!=null) {
            ArrayList<AccountType> accountTypes = accountsList.getAccountTypes()
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounting.getAccounts(), accountTypes)
            newAccountDialog.setAccount(account)
            newAccountDialog.setVisible(true)
        }
    }

    void setPopup(AccountsTablePopupMenu popup) {
        this.popup = popup
    }

    void setAccounting(Accounting accounting, boolean left) {
        this.accounting = accounting
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accountingSession.getActiveJournal(), left)
        // FIXME: enable buttons if something is selected (Listener) or make sure always something is selected
        // for info: the buttons can be used if nothing is selected, their listeners can deal with non-selections
        accountsTableButtons.setActive(accounting!=null)
//        accountDataTableModel.setFilter(null)
        accountDataTableModel.setAccounts(accounting==null?null:accounting.getAccounts())
        // if setAccounts() is used here, popup.setAccounts() will be called twice
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table))  // TODO: Needed?
        fireAccountDataChanged()
    }
    void setJournalType(JournalType journalType) {
        this.journalType = journalType
    }

    JournalType getJournalType() {
        journalType
    }

    Journal getJournal() {
        journal
    }

    void setJournalSession(JournalSession journalSession) {
        filterPanel.setJournalSession(journalSession)
        accountDataTableModel.setJournalSession(journalSession)
    }
    void setJournal(Journal journal, boolean left) {
        this.journal = journal
        if(journal!=null){
            JournalType journalType = journal.getType()
            setJournalType(journalType)

            AccountsList list = left?journalType.getLeft():journalType.getRight()
            setAccountsList(list)
            setVatType(list.getVatType())
        } else {
            // TODO: set null or 'default' type?
//            accountGuiLeft.setJournalType(null)
//            accountGuiRight.setJournalType(null)
            Accounting accounting = Session.getActiveAccounting()
            AccountTypes accountTypes = accounting.getAccountTypes()

            AccountsList list = new AccountsList()
            list.addAllTypes(accountTypes, true)
            setAccountsList(list)
            setVatType(null)
        }
    }

    void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType
    }

    void setAccountTypesList(ArrayList<AccountType> accountTypes) {
        filterPanel.setAccountTypesList(accountTypes)
    }

    void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList
        accountDataTableModel.setAccountList(accountsList)
        filterPanel.setAccountList(accountsList)
        accountsTableButtons.setAccountsList(accountsList)
    }

    void book(boolean debit) {
        popup.setVisible(false)
        for(Account account : table.getSelectedObjects()){
            AccountActions.book(account, debit, vatType, accounting, this)
        }
    }

    void fireAccountDataChanged() {
        int row = table.getSelectedRow()
        accountDataTableModel.fireTableDataChanged()
        try {
            if (row != -1) table.setRowSelectionInterval(row, row)
        }catch (IllegalArgumentException iae){
            System.err.println("row = "+row)
            iae.printStackTrace()
        }
    }
}