package be.dafke.Accounting.BasicAccounting.Journals.View.DualView

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.ComponentModel.SelectableTableModel
import be.dafke.Utils.Utils

import static java.util.ResourceBundle.getBundle

class TransactionOverviewDataModel extends SelectableTableModel<Transaction> {
    static final int ID = 0
    static final int DATE = 1
    static final int DESCRIPTION = 2
    static final int DEBIT = 3
    static final int CREDIT = 4
    static final int TOTAL_AMOUNT = 5
    static final int NR_OF_COLS = 6

    HashMap<Integer, String> columnNames = new HashMap<>()
    HashMap<Integer, Class> columnClasses = new HashMap<>()

    Journal journal

    TransactionOverviewDataModel() {
        createColumnNames()
        createColumnClasses()
    }

    void createColumnNames() {
        columnNames.put(ID, getBundle("Accounting").getString("NR"))
        columnNames.put(DATE, getBundle("Accounting").getString("DATE"))
        columnNames.put(DEBIT, getBundle("Accounting").getString("DEBIT"))
        columnNames.put(CREDIT, getBundle("Accounting").getString("CREDIT"))
        columnNames.put(DESCRIPTION, getBundle("Accounting").getString("DESCRIPTION"))
        columnNames.put(TOTAL_AMOUNT, getBundle("Accounting").getString("TOTAL_AMOUNT"))
    }


    void createColumnClasses() {
        columnClasses.put(ID, String.class)
        columnClasses.put(DATE, String.class)
        columnClasses.put(DEBIT, Account.class)
        columnClasses.put(CREDIT, Account.class)
        columnClasses.put(DESCRIPTION, String.class)
        columnClasses.put(TOTAL_AMOUNT, BigDecimal.class)
    }

    void setJournal(Journal journal) {
        this.journal = journal
    }

// DE GET METHODEN
// ===============

    int getRowCount() {
        journal?journal.businessObjects.size():0
    }

    int getColumnCount() {
        NR_OF_COLS
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    Transaction getValueAt(int row) {
        ArrayList<Transaction> transactions = journal.businessObjects
        transactions.get(row)
    }

    Object getValueAt(int row, int col) {
        Transaction transaction = getValueAt(row)
        if (col == ID) {
            if(journal==transaction.journal) {
                return journal.abbreviation + journal.getId(transaction)
            } else {
                return journal.abbreviation + journal.getId(transaction) +
                        " (" + transaction.abbreviation + transaction.id + ")"
            }
        } else if (col == DATE) return Utils.toString(transaction.date)
        else if (col == DEBIT) {
            List<Booking> bookings = transaction.getBusinessObjects{it.debit}
            if(bookings.size() == 1){
                Booking booking = bookings.get(0)
                return booking?booking.account:null
            } else return null
        } else if (col == CREDIT) {
            List<Booking> bookings = transaction.getBusinessObjects{it.credit}
            if(bookings.size() == 1){
                Booking booking = bookings.get(0)
                booking.account
            } else return null
        } else if (col == TOTAL_AMOUNT) return transaction.creditTotal
        else if (col == DESCRIPTION) return transaction.description
        null
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        (col == DATE || col == DESCRIPTION)
    }

// DE SET METHODEN
// ===============

    @Override
    void setValueAt(Object value, int row, int col) {
        Transaction transaction = getObject(row,col)
        if (col == DATE) {
            Calendar date = transaction.date
            Calendar newDate = Utils.toCalendar((String) value)
            if (journal != null && newDate != null) {
                transaction.date = newDate
            } else setValueAt(Utils.toString(date), row, col)
        } else if (col == DESCRIPTION) {
            transaction.description = (String) value
        }
        fireTableDataChanged()
    }

    @Override
    Transaction getObject(int row, int col) {
        if(journal==null) return null
        ArrayList<Transaction> transactions = journal.businessObjects
        return transactions.get(row)
    }

    int getRowInList(Transaction transaction){
        int row = 0
        if(journal==null) return 0
        ArrayList<Transaction> list = journal.businessObjects
        for(Transaction search:list){
            if(search!=transaction){
                row++
            } else{
                return row
            }
        }
        // TODO: -1 and catch effects
        return 0
    }

    int getRow(Transaction transaction) {
        if(journal==null) return -1
        return getRowInList(transaction)
    }
}
