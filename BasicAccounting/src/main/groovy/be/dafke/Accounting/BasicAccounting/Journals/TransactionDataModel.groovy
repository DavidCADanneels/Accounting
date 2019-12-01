package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class TransactionDataModel extends SelectableTableModel<Booking> {
    static final int DEBIT_ACCOUNT = 0
    static final int CREDIT_ACCOUNT = 1
    static final int DEBIT_AMOUNT = 2
    static final int CREDIT_AMOUNT = 3
    static final int VATINFO = 4
    static final int NR_OF_COLS = 5

    private HashMap<Integer, String> columnNames = new HashMap<>()
    private HashMap<Integer, Class> columnClasses = new HashMap<>()

    private Transaction transaction

    TransactionDataModel() {
        createColumnNames()
        createColumnClasses()
    }

    private void createColumnNames() {
        columnNames.put(DEBIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"))
        columnNames.put(CREDIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"))
        columnNames.put(DEBIT_AMOUNT, getBundle("Accounting").getString("DEBIT"))
        columnNames.put(CREDIT_AMOUNT, getBundle("Accounting").getString("CREDIT"))
        columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"))
    }


    private void createColumnClasses() {
        columnClasses.put(DEBIT_ACCOUNT, Account.class)
        columnClasses.put(CREDIT_ACCOUNT, Account.class)
        columnClasses.put(DEBIT_AMOUNT, BigDecimal.class)
        columnClasses.put(CREDIT_AMOUNT, BigDecimal.class)
        columnClasses.put(VATINFO, String.class)
    }

    void setTransaction(Transaction transaction) {
        this.transaction = transaction
    }

// DE GET METHODEN
// ===============

    int getRowCount() {
        transaction?transaction.getBusinessObjects().size():0
    }

    int getColumnCount() {
        NR_OF_COLS
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    Booking getValueAt(int row) {
        if(transaction) {
            ArrayList<Booking> bookings = transaction.getBusinessObjects()
            bookings.get(row)
        } else null
    }

    Object getValueAt(int row, int col) {
        Booking booking = getValueAt(row)
        if (col == DEBIT_ACCOUNT) {
            if (booking.isDebit())
                booking.getAccount()
            else null
        } else if (col == CREDIT_ACCOUNT) {
            if(!booking.isDebit())
                booking.getAccount()
            else null
        } else if (col == DEBIT_AMOUNT) {
            if (booking.isDebit()) booking.getAmount()
            ""
        } else if (col == CREDIT_AMOUNT) {
            if (!booking.isDebit()) booking.getAmount()
            ""
        } else if (col == VATINFO){
            booking.getMergedVATBookingsString()
        } else null
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        false
    }

// DE SET METHODEN
// ===============

    @Override
    Booking getObject(int row, int col) {
        if(transaction==null) null
        ArrayList<Booking> bookings = transaction.getBusinessObjects()
        bookings.get(row)
    }

    private int getRowInList(ArrayList<Booking> list, Booking booking){
        int row = 0
        for(Booking search:list){
            if(search!=booking){
                row++
            } else{
                row
            }
        }
        // TODO: -1 and catch effects
        0
    }

    int getRow(Booking booking) {
        if(transaction==null) -1
        ArrayList<Booking> bookings = transaction.getBusinessObjects()
        getRowInList(bookings,booking)
    }
}
