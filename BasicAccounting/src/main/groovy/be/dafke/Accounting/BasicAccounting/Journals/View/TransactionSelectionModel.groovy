package be.dafke.Accounting.BasicAccounting.Journals.View

import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Transaction

import javax.swing.DefaultListSelectionModel

class TransactionSelectionModel extends DefaultListSelectionModel {

    Transaction selectedTransaction = null
    Booking selectedBooking = null

    Transaction getSelectedTransaction() {
        return selectedTransaction
    }

    void setSelectedTransaction(Transaction selectedTransaction) {
        if(selectedTransaction) {
            println('Set T -> VALUE')
        } else {
            println('Set T -> null')
        }
        this.selectedTransaction = selectedTransaction
    }

    Booking getSelectedBooking() {
        return selectedBooking
    }

    void setSelectedBooking(Booking selectedBooking) {
        if(selectedBooking) {
            println('Set B -> VALUE')
        } else {
            println('Set B -> null')
        }
        this.selectedBooking = selectedBooking
    }
}
