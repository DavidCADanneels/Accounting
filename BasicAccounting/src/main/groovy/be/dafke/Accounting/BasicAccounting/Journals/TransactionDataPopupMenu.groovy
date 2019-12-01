package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class TransactionDataPopupMenu extends JPopupMenu {
    private final JMenuItem details

    private SelectableTable<Booking> gui
    private Journals journals

    TransactionDataPopupMenu(Journals journals, SelectableTable<Booking> gui) {
        this(gui)
        this.journals=journals
    }

    TransactionDataPopupMenu(SelectableTable<Booking> gui) {
        this.gui = gui
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"))
        details.addActionListener({ e -> showDetails() })
        add(details)
    }

    void setJournals(Journals journals){
        this.journals=journals
    }

    private void showDetails() {
        Point locationOnScreen = getLocationOnScreen()
        setVisible(false)
        ArrayList<Booking> bookings = gui.getSelectedObjects()
        for (Booking booking : bookings) {
            Account account = booking.getAccount()
            AccountDetailsGUI newGui = AccountDetailsGUI.getAccountDetails(locationOnScreen, account, journals)
            newGui.selectObject(booking)
        }
    }
}