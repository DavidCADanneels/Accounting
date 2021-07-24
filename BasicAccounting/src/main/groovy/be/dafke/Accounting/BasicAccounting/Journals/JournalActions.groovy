package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.Accounting.BusinessModel.Transactions
import be.dafke.Accounting.BusinessModel.VATBooking
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.JOptionPane

import static java.util.ResourceBundle.getBundle

class JournalActions {

    static void addTransactionToJournal(Transaction transaction, Journal journal){
        Accounting accounting = journal.accounting
        Transactions transactions = accounting.transactions
        transactions.setId(transaction)
        transactions.addBusinessObject transaction
        journal.addBusinessObject transaction
        transaction.journal = journal

        ArrayList<VATBooking> vatBookings = transaction.vatBookings
        if (vatBookings != null && !vatBookings.isEmpty()) {
            Main.fireVATFieldsUpdated(/*vatFields*/)
        }

        Contact contact = transaction.contact
        if(contact){
            Main.fireCustomerDataChanged()
        }

        Main.fireJournalDataChanged(journal)
        for (Account account : transaction.accounts) {
            Main.fireAccountDataChanged(account)
        }
    }


    static Journal getNewJournal(Transaction transaction, Journals journals){
        Journal journal = transaction.journal
        ArrayList<Journal> dagboeken = journals.getAllJournalsExcept(journal)
        Object[] lijst = dagboeken.toArray()
        int keuze = JOptionPane.showOptionDialog(null,
                getBundle("BusinessActions").getString("CHOOSE_JOURNAL"),
                getBundle("BusinessActions").getString("JOURNAL_CHOICE"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0])
        if(keuze!=JOptionPane.CLOSED_OPTION){
            (Journal) lijst[keuze]
        }else null
    }

    static void moveTransaction(Set<Transaction> transactions, Journals journals, Transaction transaction) {
        // ask journal only once
        Journal newJournal = getNewJournal(transaction, journals)
        moveTransaction(transactions, newJournal)
    }

    static void moveBookings(ArrayList<Booking> bookings, Journals journals) {
        Set<Transaction> transactions = getTransactions(bookings)
        moveTransaction(transactions, journals)
    }

    static void moveTransaction(Set<Transaction> transactions, Journal newJournal) {
        Set<Journal> updatedJournals = new HashSet<>()
        Set<Account> updatedAccounts = new HashSet<>()
        if(newJournal){
            updatedJournals.add(newJournal)
        }
        for (Transaction transaction : transactions) {
            if (transaction != null) {
                Journal oldJournal = transaction.journal
                if (oldJournal != null) {
                    oldJournal.removeBusinessObject(transaction)
                }

                if (newJournal != null) { // e.g. when Cancel has been clicked
                    newJournal.addBusinessObject(transaction)
                }
                transaction.journal = newJournal
                for (Account account : transaction.accounts) {
                    updatedAccounts.add(account)
                }
            }
        }

        for (Journal journal:updatedJournals) {
            Main.fireJournalDataChanged(journal)
        }

        for (Account account : updatedAccounts) {
            Main.fireAccountDataChanged(account)
        }
//        ActionUtils.showErrorMessage(ActionUtils.TRANSACTION_MOVED, oldJournal.name, newJournal.name)
    }

    static Set<Transaction> getTransactions(ArrayList<Booking> bookings){
        Set<Transaction> transactions = new HashSet<>()
        for (Booking booking:bookings) {
            transactions.add(booking.transaction)
        }
        transactions
    }

    static void deleteBookings(ArrayList<Booking> bookings) {
        Set<Transaction> transactions = getTransactions(bookings)
        deleteTransactions(transactions)
    }

    static void deleteTransactions(Set<Transaction> transactions){
        for (Transaction transaction : transactions) {
            deleteTransaction(transaction)
        }
    }

    static void deleteTransaction(Transaction transaction) {//throws NotEmptyException{
        Journal journal = transaction.journal
        journal.removeBusinessObject(transaction)
        Session.activeAccounting.transactions.removeBusinessObject(transaction)
        transaction.journal = null

        Main.fireJournalDataChanged(journal)
        for (Account account : transaction.accounts) {
            Main.fireAccountDataChanged(account)
        }

        ArrayList<VATBooking> vatBookings = transaction.vatBookings
        if (vatBookings != null && !vatBookings.isEmpty()) {
            Main.fireVATFieldsUpdated()
        }
//        ActionUtils.showErrorMessage(TRANSACTION_REMOVED, journal.name)
    }

}
