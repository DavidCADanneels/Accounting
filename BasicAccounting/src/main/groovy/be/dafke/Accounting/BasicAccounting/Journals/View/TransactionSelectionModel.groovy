package be.dafke.Accounting.BasicAccounting.Journals.View

import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Transaction

import javax.swing.DefaultListSelectionModel

class TransactionSelectionModel extends DefaultListSelectionModel {

    static final String VIEW1 = "Single Table"
    static final String VIEW2 = "Dual Table"
    String view = VIEW1

    ArrayList<Transaction> selectedTransactions = new ArrayList<>()
    Transaction selectedTransaction = null
    ArrayList<Booking> selectedBookings = new ArrayList<>()
    Booking selectedBooking = null

    boolean multiSelection

    void selectTransactions(ArrayList<Transaction> list){
        selectedTransactions = list
        selectedTransaction = null//list.empty?null:list[0]
        System.out.println("selectTransactions")
        printData()
    }

    void selectTransaction(Transaction transaction) {
        selectedTransaction = transaction
        selectedTransactions.clear()
        selectedBookings.clear()
        System.out.println("selectTransaction")
        printData()
    }

//    void selectBookings(ArrayList<Booking> bookings) {
//        selectedBookings = bookings
//        selectedBooking = null //bookings && !bookings.empty ? bookings[0] : null
//        selectedTransaction = null//selectedBooking ? selectedBooking.transaction : null
//        selectedTransactions.clear()
//        System.out.println("selectBookings")
//        printData()
//    }

    void selectBooking(Booking booking){
        selectedBooking = booking
        selectedBookings.clear()
        selectedBookings.add(booking)
        selectedTransaction = null//booking.transaction
        selectedTransactions.clear()
//        selectedTransactions.add(booking.transaction)
        System.out.println("selectBooking")
        printData()
    }

    void printData() {
        System.out.println """Output:
selectedBooking: ${selectedBooking&&selectedBooking.account?selectedBooking.account.name:'null'}
selectedTransaction: ${selectedTransaction?selectedTransaction.transactionId:'null'}
selectedTransactions: ${selectedTransactions.size()}
"""
    }
}
