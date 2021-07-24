package be.dafke.Accounting.BasicAccounting.Journals.Edit

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.Accounting.BasicAccounting.Accounts.Selector.AccountSelectorDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class JournalGUIPopupMenu extends JPopupMenu{
    final JMenuItem delete, edit, change, debitCredit, details
    final SelectableTable<Booking> table

    JournalGUIPopupMenu(SelectableTable<Booking> table) {
        this.table = table
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"))
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_AMOUNT"))
        change = new JMenuItem(getBundle("Accounting").getString("CHANGE_ACCOUNT"))
        debitCredit = new JMenuItem(getBundle("Accounting").getString("D_C"))
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"))
        add(details)
        delete.addActionListener({ e -> deleteBooking() })
        edit.addActionListener({ e -> editAmount() })
        change.addActionListener({ e -> changeAccount() })
        debitCredit.addActionListener({ e -> switchDebitCredit() })
        details.addActionListener({ e -> showDetails() })
        add(delete)
        add(edit)
        add(change)
        add(debitCredit)
    }

    void deleteBooking() {
        setVisible(false)
        ArrayList<Booking> bookings = table.selectedObjects
        for (Booking booking : bookings) {
            Transaction transaction = booking.transaction
            transaction.removeBusinessObject booking
            Main.fireTransactionInputDataChanged()
        }
    }

    void editAmount() {
        setVisible(false)
        ArrayList<Booking> bookings = table.selectedObjects
        for (Booking booking : bookings) {
            Transaction transaction = booking.transaction
            Account account = booking.account
            //TODO: or JournalGUI.table should contain Movements iso Bookings
            // booking must be removed and re-added to Transaction to re-calculate the totals
            transaction.removeBusinessObject(booking)
            BigDecimal amount = AccountActions.askAmount(account, booking.debit,transaction, this)
            if (amount != null) {
                booking.setAmount(amount)
            }
            transaction.addBusinessObject(booking)
            Main.fireTransactionInputDataChanged()
        }
    }

    void switchDebitCredit() {
        setVisible(false)
        ArrayList<Booking> bookings = table.selectedObjects
        for (Booking booking : bookings) {
            Transaction transaction = booking.transaction
            // booking must be removed and re-added to Transaction to re-calculate the totals
            transaction.removeBusinessObject(booking)
            booking.setDebit(!booking.debit)
            transaction.addBusinessObject(booking)
            Main.fireTransactionInputDataChanged()
        }
    }

    void changeAccount() {
        setVisible(false)
        ArrayList<Booking> bookings = table.selectedObjects
        for (Booking booking : bookings) {
            AccountSelectorDialog sel = AccountSelectorDialog.getAccountSelector(Session.activeAccounting.accounts, Session.activeAccounting.accountTypes.businessObjects)
            sel.visible = true
            Account account = sel.getSelection()
            if (account != null) {
                booking.setAccount(account)
            }
            Main.fireTransactionInputDataChanged()
        }
    }

    void showDetails() {
        Point locationOnScreen = getLocationOnScreen()
        setVisible(false)
        ArrayList<Booking> bookings = table.selectedObjects
        for (Booking booking : bookings) {
            Account account = booking.account
            AccountDetailsGUI.getAccountDetails(locationOnScreen, account, Session.activeAccounting.journals)
        }
    }
}
