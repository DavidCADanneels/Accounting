package be.dafke.Accounting.BasicAccounting.Journals.View

import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Transaction

import javax.swing.DefaultListSelectionModel

class TransactionSelectionModel extends DefaultListSelectionModel {

    static final String VIEW1 = "Single Table"
    static final String VIEW2 = "Dual Table"

    Transaction selectedTransaction = null
    Booking selectedBooking = null

}
