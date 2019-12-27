package be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI
import be.dafke.Accounting.BasicAccounting.Accounts.AccountsFilter.AccountFilterPanel
import be.dafke.Accounting.BasicAccounting.Accounts.NewAccountDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.JournalSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AccountsTablePanel extends JPanel {
    final SelectableTable<Account> table
    final AccountDataTableModel accountDataTableModel
    final AccountFilterPanel filterPanel

    AccountsTablePopupMenu popup
    Accounting accounting
    Journal journal
    JournalType journalType
    AccountsList accountsList
    AccountsTableButtons accountsTableButtons

    VATTransaction.VATType vatType = null

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
////        filterPanel.journal = journal
//        filterPanel.setJournalSession(journalSession)
//        accountDataTableModel.journal = journal
//    }

    void showDetails(){
        popup.visible = false
        for(Account account : table.selectedObjects){
            Point location = getLocationOnScreen()
            AccountDetailsGUI.getAccountDetails(location, account, accounting.journals)
        }
    }

    void manageAccounts(){
        popup.visible = false
        ArrayList<AccountType> accountTypes = accountsList.accountTypes
        AccountManagementGUI accountManagementGUI = AccountManagementGUI.getInstance(accounting.accounts, accountTypes)
        accountManagementGUI.setLocation(getLocationOnScreen())
        accountManagementGUI.visible = true
    }

    void addAccount(){
        popup.visible = false
        ArrayList<AccountType> accountTypes = accountsList.accountTypes
        NewAccountDialog newAccountDialog = new NewAccountDialog(accounting.accounts, accountTypes)
        newAccountDialog.setLocation(getLocation())
        newAccountDialog.visible = true
    }

    void editAccount(){
        popup.visible = false
        Account account = table.selectedObject
        if(account!=null) {
            ArrayList<AccountType> accountTypes = accountsList.accountTypes
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounting.accounts, accountTypes)
            newAccountDialog.account = account
            newAccountDialog.visible = true
        }
    }

    void setPopup(AccountsTablePopupMenu popup) {
        this.popup = popup
    }

    void setAccounting(Accounting accounting, boolean left) {
        this.accounting = accounting

        // FIXME: enable buttons if something is selected (Listener) or make sure always something is selected
        // for info: the buttons can be used if nothing is selected, their listeners can deal with non-selections
        accountsTableButtons.setActive(accounting!=null)
//        accountDataTableModel.setFilter(null)
//        accountDataTableModel.accounts = accounting?accounting.accounts:null
        accountDataTableModel.accounting = accounting
        filterPanel.accounting = accounting
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accountingSession.activeJournal, left)
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
        filterPanel.journalSession = journalSession
        accountDataTableModel.journalSession = journalSession
    }
    void setJournal(Journal journal, boolean left) {
        this.journal = journal
        if(journal!=null){
            JournalType journalType = journal.type
            setJournalType(journalType)

            AccountsList list = left?journalType.getLeft():journalType.getRight()
            setAccountsList(list)
            setVatType(list.getVatType())
        } else {
            // TODO: set null or 'default' type?
//            accountGuiLeft.setJournalType(null)
//            accountGuiRight.setJournalType(null)
            Accounting accounting = Session.activeAccounting
            AccountTypes accountTypes = accounting.accountTypes

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
        popup.visible = false
        for(Account account : table.selectedObjects){
            AccountActions.book(account, debit, vatType, accounting, this)
        }
    }

    void fireGlobalShowNumbersChanged(boolean enabled) {
        filterPanel.refresh(enabled)
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