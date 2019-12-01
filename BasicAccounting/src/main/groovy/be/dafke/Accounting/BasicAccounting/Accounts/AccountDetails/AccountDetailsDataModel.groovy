package be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.ComponentModel.SelectableTableModel
import be.dafke.Utils.Utils

import static java.util.ResourceBundle.getBundle

/**
 * @author David Danneels
 */
class AccountDetailsDataModel extends SelectableTableModel<Booking> {
    private final Account rekening

    static final int NR = 0
    static final int DATE = 1
    static final int DEBIT = 2
    static final int CREDIT = 3
    static final int VATINFO = 4
    static final int DESCRIPTION = 5
    private HashMap<Integer, String> columnNames = new HashMap<>()
    private HashMap<Integer, Class> columnClasses = new HashMap<>()

    AccountDetailsDataModel(Account account) {
        rekening = account
        createColumnNames()
        createColumnClasses()
    }

    private void createColumnNames() {
        columnNames.put(NR, getBundle("Accounting").getString("NR"))
        columnNames.put(DATE, getBundle("Accounting").getString("DATE"))
        columnNames.put(DEBIT, getBundle("Accounting").getString("DEBIT"))
        columnNames.put(CREDIT, getBundle("Accounting").getString("CREDIT"))
        columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"))
        columnNames.put(DESCRIPTION, getBundle("Accounting").getString("DESCRIPTION"))
    }

    private void createColumnClasses() {
        columnClasses.put(NR, String.class)
        columnClasses.put(DATE, String.class)
        columnClasses.put(DEBIT, BigDecimal.class)
        columnClasses.put(CREDIT, BigDecimal.class)
        columnClasses.put(VATINFO, String.class)
        columnClasses.put(DESCRIPTION, String.class)
    }

// DE GET METHODEN
// ===============

    int getRowCount() {
        rekening.getBusinessObjects().size()
    }

    int getColumnCount() {
        columnClasses.size()
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    Booking getObject(int row, int col) {
        rekening.getBusinessObjects().get(row).getBooking()
    }

    int getRow(Booking booking){
        int row=0
        for(Movement movement:rekening.getBusinessObjects()){
            if(movement.getBooking()!=booking){
                row++
            } else{
                row
            }
        }
        0
    }

    Object getValueAt(int row, int col) {
        Movement movement = rekening.getBusinessObjects().get(row)
        if (col == NR) {
            movement.getTransactionString()
        } else if (col == DATE) {
            Utils.toString(movement.getDate())
        } else if (col == DEBIT) {
            if (movement.isDebit()) movement.getAmount()
            ""
        } else if (col == CREDIT) {
            if (!movement.isDebit()) movement.getAmount()
            ""
        } else if (col == DESCRIPTION) {
            movement.getDescription()
        } else if (col == VATINFO) {
            movement.getBooking().getVATBookingsString()
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
        Movement movement = rekening.getBusinessObjects().get(row)
        if (col == 1) {
            Calendar newDate = Utils.toCalendar((String) value)
            Transaction transaction = getTransaction(movement)
            Journal journal = getJournal(movement)
            if(newDate!=null && transaction != null && journal != null) {
                transaction.setDate(newDate)
                Main.fireJournalDataChanged(journal)
            }
        } else if (col == 4) {
            if(movement!=null) {
                movement.setDescription((String) value)
            }
        }
        fireTableDataChanged()
    }

    private Journal getJournal(Movement movement){
        getTransaction(movement)==null?null:getTransaction(movement).getJournal()
    }

    private Transaction getTransaction(Movement movement){
        movement==null?null:movement.getBooking()==null?null:movement.getBooking().getTransaction()
    }
}

