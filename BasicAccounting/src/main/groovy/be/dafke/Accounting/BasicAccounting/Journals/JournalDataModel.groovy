package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class JournalDataModel extends SelectableTableModel<Booking> {
    static final int DEBIT_ACCOUNT = 0
    static final int CREDIT_ACCOUNT = 1
    static final int DEBIT_AMOUNT = 2
    static final int CREDIT_AMOUNT = 3
    static final int VATINFO = 4

    HashMap<Integer, String> columnNames = new HashMap<>()
    HashMap<Integer, Class> columnClasses = new HashMap<>()

    Transaction transaction

    JournalDataModel() {
        createColumnNames()
        createColumnClasses()
    }

    void createColumnNames() {
        columnNames.put(DEBIT_ACCOUNT, getBundle("Accounting").getString("DEBIT"))
        columnNames.put(CREDIT_ACCOUNT, getBundle("Accounting").getString("CREDIT"))
        columnNames.put(DEBIT_AMOUNT, getBundle("Accounting").getString("D"))
        columnNames.put(CREDIT_AMOUNT, getBundle("Accounting").getString("C"))
        columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"))
    }

    void createColumnClasses() {
        columnClasses.put(DEBIT_ACCOUNT, Account.class)
        columnClasses.put(CREDIT_ACCOUNT, Account.class)
        columnClasses.put(DEBIT_AMOUNT, BigDecimal.class)
        columnClasses.put(CREDIT_AMOUNT, BigDecimal.class)
        columnClasses.put(VATINFO, BigDecimal.class)
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Booking booking = getBooking(row)
        if (col == VATINFO){
            booking.getVATBookingsString()
        } else if (col == DEBIT_ACCOUNT) {
            booking.debit?booking.account:null
        } else if (col == CREDIT_ACCOUNT) {
            booking.debit?null:booking.account
        } else if (col == DEBIT_AMOUNT) {
            booking.debit?booking.amount:null
        } else if (col == CREDIT_AMOUNT) {
            booking.debit?null:booking.amount
        } else null
    }

    Booking getBooking(int row){
        transaction.businessObjects.get(row)
    }

    int getColumnCount() {
        columnNames.size()
    }

    int getRowCount() {
        transaction?transaction.businessObjects.size():0
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        row != VATINFO && getValueAt(row,col)!=null
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        Booking booking = getBooking(row)
        if(col == DEBIT_ACCOUNT || col == CREDIT_ACCOUNT){
            Account newAccount = (Account)value
            // FIXME: use only for unbooked Bookings
            // if already booked, old account must be unbooked
            // and newAccount booked
            // What to do when editing (booked/unbooked) vatTransactions ???
            if(newAccount) {
                booking.setAccount(newAccount)
            }
            fireTableDataChanged()
        } else if(col == DEBIT_AMOUNT || col == CREDIT_AMOUNT){
            BigDecimal newAmount = (BigDecimal) value
            if(newAmount){
                Transaction transaction = booking.transaction
                transaction.removeBusinessObject(booking)
                booking.setAmount(newAmount)
                transaction.addBusinessObject(booking)
            }
            Main.fireTransactionInputDataChanged()
        }
    }

    void setTransaction(Transaction transaction) {
        this.transaction = transaction
    }

    Transaction getTransaction() {
        transaction
    }

    @Override
    Booking getObject(int row, int col) {
        transaction.businessObjects.get(row)
    }

}