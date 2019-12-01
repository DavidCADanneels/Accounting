package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.Accounting.BasicAccounting.Balances.BalanceGUI
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class JournalDetailsPopupMenu extends JPopupMenu {
    final JMenuItem move, delete, edit, details, balance

    SelectableTable<Booking> gui
    Journals journals
//    Accounting accounting

    JournalDetailsPopupMenu(Journals journals, SelectableTable<Booking> gui) {
        this(gui)
        this.journals=journals
    }

    JournalDetailsPopupMenu(SelectableTable<Booking> gui) {
        this.gui = gui
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"))
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"))
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"))
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"))
        balance = new JMenuItem(getBundle("Accounting").getString("BALANCE_CALCULATION"))

        delete.addActionListener({ e -> deleteTransaction() })
        move.addActionListener({ e -> moveTransaction() })
        edit.addActionListener({ e -> editTransaction() })
        details.addActionListener({ e -> showDetails() })
        balance.addActionListener({ e -> showBalance() })

        add(delete)
        add(move)
        add(edit)
        add(details)
        addSeparator()
        add(balance)
    }

//    void setAccounting(Accounting accounting) {
//        this.accounting = accounting
//        setJournals(accounting?accounting.journals:null)
//    }

    void showBalance() {
        setVisible(false)
        // choices:
        // 1: all transactions from this journal (e.g. FAM16)
        // 2: all transaction from a certain year (2016)
        // 3: all transaction of a certain period (e.g. 01/07/2016 - 30/06/2017)

        // TODO first:
        // save transactions per year as well in Accounting
        // Accounting.getTransactions(int year)
        // later:
        // Accounting.getTransactions(Bookyear year)
        // with Bookyear: a period in time (e.g. 01/07 - 30/06)

        Accounting accounting = Session.activeAccounting

        int year = gui.selectedObject.transaction.date.get(Calendar.YEAR)
        Accounts subAccounts = accounting.accounts.getSubAccounts(Movement.ofYear(year))

        Balances balances = accounting.balances
        Balance closingBalance = balances.createClosingBalance(subAccounts)
        Balance relationsBalance = balances.createRelationsBalance(subAccounts)
        Balance resultBalance = balances.createResultBalance(subAccounts)

        BalanceGUI.getBalance(accounting, closingBalance).visible = true
        BalanceGUI.getBalance(accounting, resultBalance).visible = true
        BalanceGUI.getBalance(accounting, relationsBalance).visible = true

        // choice 2: year=year of selected transaction
//        Accounting accounting = Session.activeAccounting
//        Balances balances = accounting.balances
//        BalanceGUI.getBalance()

    }

    void setJournals(Journals journals){
        this.journals=journals
    }

    void moveTransaction() {
        setVisible(false)
        ArrayList<Booking> bookings = gui.selectedObjects
        Main.moveBookings(bookings, journals)
    }

    void deleteTransaction() {
        setVisible(false)
        ArrayList<Booking> bookings = gui.selectedObjects
        Main.deleteBookings(bookings)
    }

    void editTransaction() {
        setVisible(false)
        Booking booking = gui.selectedObject
        Transaction transaction = booking.transaction
        Main.editTransaction(transaction)
    }

    void showDetails() {
        Point locationOnScreen = getLocationOnScreen()
        setVisible(false)
        ArrayList<Booking> bookings = gui.selectedObjects
        for (Booking booking : bookings) {
            Account account = booking.account
            AccountDetailsGUI newGui = AccountDetailsGUI.getAccountDetails(locationOnScreen, account, journals)
            newGui.selectObject(booking)
        }
    }
}
