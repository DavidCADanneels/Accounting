package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.ComponentModel.SelectableTableModel
import be.dafke.Utils.Utils

import static java.util.ResourceBundle.getBundle 

class JournalDetailsDataModel extends SelectableTableModel<Booking> {
    static final int ID = 0
    static final int DATE = 1
    static final int DEBIT_ACCOUNT = 2
    static final int CREDIT_ACCOUNT = 3
    static final int DEBIT_AMOUNT = 4
    static final int CREDIT_AMOUNT = 5
    static final int VATINFO = 6
    static final int DESCRIPTION = 7
    static final int NR_OF_COLS = 8

    HashMap<Integer, String> columnNames = new HashMap<>()
    HashMap<Integer, Class> columnClasses = new HashMap<>()

    Journal journal

    JournalDetailsDataModel() {
        createColumnNames()
        createColumnClasses()
    }

    void createColumnNames() {
        columnNames.put(ID, getBundle("Accounting").getString("NR"))
        columnNames.put(DATE, getBundle("Accounting").getString("DATE"))
        columnNames.put(DEBIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"))
        columnNames.put(CREDIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"))
        columnNames.put(DEBIT_AMOUNT, getBundle("Accounting").getString("DEBIT"))
        columnNames.put(CREDIT_AMOUNT, getBundle("Accounting").getString("CREDIT"))
        columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"))
        columnNames.put(DESCRIPTION, getBundle("Accounting").getString("DESCRIPTION"))
    }


    void createColumnClasses() {
        columnClasses.put(ID, String.class)
        columnClasses.put(DATE, String.class)
        columnClasses.put(DEBIT_ACCOUNT, Account.class)
        columnClasses.put(CREDIT_ACCOUNT, Account.class)
        columnClasses.put(DEBIT_AMOUNT, BigDecimal.class)
        columnClasses.put(CREDIT_AMOUNT, BigDecimal.class)
        columnClasses.put(VATINFO, String.class)
        columnClasses.put(DESCRIPTION, String.class)
    }

    void setJournal(Journal journal) {
        this.journal = journal
    }

// DE GET METHODEN
// ===============

    int getRowCount() {
        int size = 0
        if(journal) {
            for (Transaction transaction : journal.businessObjects) {
                size += transaction.businessObjects.size()
            }
        }
        size
    }

    int getColumnCount() {
        NR_OF_COLS
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    Booking getValueAt(int row) {
        ArrayList<Booking> boekingen = new ArrayList<>()
        if(journal==null) null
        for(Transaction transaction : journal.businessObjects){
            boekingen.addAll(transaction.businessObjects)
        }
        boekingen.get(row)
    }

    Object getValueAt(int row, int col) {
        Booking booking = getValueAt(row)
        Transaction transaction = booking.transaction
        boolean first = (booking == transaction.businessObjects.get(0))
        if (col == ID) {
            if(first){
                if(journal==transaction.journal) {
                    return journal.abbreviation + journal.getId(transaction)
                } else {
                    return journal.abbreviation + journal.getId(transaction) +
                            " (" + transaction.abbreviation + transaction.id + ")"
                }
            } else return ""
        } else if (col == DATE) {
            if(first){
                return Utils.toString(transaction.date)
            } else return ""
        } else if (col == DEBIT_ACCOUNT) {
            if (booking.debit)
                return booking.account
            else return null
        } else if (col == CREDIT_ACCOUNT) {
            if(!booking.debit)
                return booking.account
            else return null
        } else if (col == DEBIT_AMOUNT) {
            if (booking.debit) return booking.amount
            else return ""
        } else if (col == CREDIT_AMOUNT) {
            if (!booking.debit) return booking.amount
            return ""
        } else if (col == DESCRIPTION){
            if(first){
                return booking.transaction.description
            } else return ""
        } else if (col == VATINFO){
            return booking.getVATBookingsString()
        }
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
        Booking booking = getObject(row,col)
        if(booking) {
            Transaction transaction = booking.transaction
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
    }

    ArrayList<Booking> getAllItems(){
        ArrayList<Booking> bookings = new ArrayList<>()
        for(Transaction transaction : journal.businessObjects){
            bookings.addAll(transaction.businessObjects)
        }
        bookings
    }

    @Override
    Booking getObject(int row, int col) {
        if(journal==null) null
        ArrayList<Booking> bookings = getAllItems()
        bookings.get(row)
    }

    int getRowInList(ArrayList<Booking> list, Booking booking){
        int row = 0
        for(Booking search:list){
            if(search!=booking){
                row++
            } else{
                return row
            }
        }
        // TODO: -1 and catch effects
        return 0
    }

    int getRow(Booking booking) {
        if(journal==null) return -1
        ArrayList<Booking> bookings = getAllItems()
        getRowInList(bookings,booking)
    }
}
