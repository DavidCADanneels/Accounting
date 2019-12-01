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

    private HashMap<Integer, String> columnNames = new HashMap<>()
    private HashMap<Integer, Class> columnClasses = new HashMap<>()

    private Journal journal

    JournalDetailsDataModel() {
        createColumnNames()
        createColumnClasses()
    }

    private void createColumnNames() {
        columnNames.put(ID, getBundle("Accounting").getString("NR"))
        columnNames.put(DATE, getBundle("Accounting").getString("DATE"))
        columnNames.put(DEBIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"))
        columnNames.put(CREDIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"))
        columnNames.put(DEBIT_AMOUNT, getBundle("Accounting").getString("DEBIT"))
        columnNames.put(CREDIT_AMOUNT, getBundle("Accounting").getString("CREDIT"))
        columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"))
        columnNames.put(DESCRIPTION, getBundle("Accounting").getString("DESCRIPTION"))
    }


    private void createColumnClasses() {
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
            for (Transaction transaction : journal.getBusinessObjects()) {
                size += transaction.getBusinessObjects().size()
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
        for(Transaction transaction : journal.getBusinessObjects()){
            boekingen.addAll(transaction.getBusinessObjects())
        }
        boekingen.get(row)
    }

    Object getValueAt(int row, int col) {
        Booking booking = getValueAt(row)
        Transaction transaction = booking.getTransaction()
        boolean first = (booking == transaction.getBusinessObjects().get(0))
        if (col == ID) {
            if(first){
                if(journal==transaction.getJournal()) {
                    journal.getAbbreviation() + journal.getId(transaction)
                } else {
                    journal.getAbbreviation() + journal.getId(transaction) +
                            " (" + transaction.getAbbreviation() + transaction.getId() + ")"
                }
            } else ""
        } else if (col == DATE) {
            if(first){
                Utils.toString(transaction.getDate())
            } else ""
        } else if (col == DEBIT_ACCOUNT) {
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
        } else if (col == DESCRIPTION){
            if(first){
                booking.getTransaction().getDescription()
            } else ""
        } else if (col == VATINFO){
            booking.getVATBookingsString()
        } else null
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
        if(booking!=null) {
            Transaction transaction = booking.getTransaction()
            if (col == DATE) {
                Calendar date = transaction.getDate()
                Calendar newDate = Utils.toCalendar((String) value)
                if (journal != null && newDate != null) {
                    transaction.setDate(newDate)
                } else setValueAt(Utils.toString(date), row, col)
            } else if (col == DESCRIPTION) {
                transaction.setDescription((String) value)
            }
            fireTableDataChanged()
        }
    }

    private ArrayList<Booking> getAllItems(){
        ArrayList<Booking> bookings = new ArrayList<>()
        for(Transaction transaction : journal.getBusinessObjects()){
            bookings.addAll(transaction.getBusinessObjects())
        }
        bookings
    }

    @Override
    Booking getObject(int row, int col) {
        if(journal==null) null
        ArrayList<Booking> bookings = getAllItems()
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
        if(journal==null) -1
        ArrayList<Booking> bookings = getAllItems()
        getRowInList(bookings,booking)
    }
}
