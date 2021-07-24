package be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails

import be.dafke.Accounting.BasicAccounting.Journals.JournalActions
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalDetailsGUI
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AccountDetailsPopupMenu extends JPopupMenu {
    final JMenuItem move, delete, edit, details

    SelectableTable<Booking> gui
    Journals journals

    AccountDetailsPopupMenu(Journals journals, SelectableTable<Booking> gui) {
        this(gui)
        this.journals=journals
    }

    AccountDetailsPopupMenu(SelectableTable<Booking> gui) {
        this.gui = gui
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"))
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"))
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"))
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_JOURNAL_DETAILS"))
        delete.addActionListener({ e -> deleteTransaction() })
        move.addActionListener({ e -> moveTransaction() })
        edit.addActionListener({ e -> editTransaction() })
        details.addActionListener({ e -> showDetails() })

        add(delete)
        add(move)
        add(edit)
        add(details)
    }

    void setJournals(Journals journals){
        this.journals=journals
    }

    void moveTransaction() {
        setVisible(false)
        ArrayList<Booking> bookings = gui.selectedObjects
        JournalActions.moveBookings(bookings, journals)
    }

    void deleteTransaction() {
        setVisible(false)
        ArrayList<Booking> bookings = gui.selectedObjects
        JournalActions.deleteBookings(bookings)
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
            Transaction transaction = booking.transaction
            Journal journal = transaction.journal
            JournalDetailsGUI newGui = JournalDetailsGUI.getJournalDetails locationOnScreen, journal
            newGui.selectObject(booking)
        }
    }

}
