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
    final Account rekening

    static final int NR = 0
    static final int DATE = 1
    static final int DEBIT = 2
    static final int CREDIT = 3
    static final int VATINFO = 4
    static final int DESCRIPTION = 5
    HashMap<Integer, String> columnNames = new HashMap<>()
    HashMap<Integer, Class> columnClasses = new HashMap<>()

    AccountDetailsDataModel(Account account) {
        rekening = account
        createColumnNames()
        createColumnClasses()
    }

    void createColumnNames() {
        columnNames.put(NR, getBundle("Accounting").getString("NR"))
        columnNames.put(DATE, getBundle("Accounting").getString("DATE"))
        columnNames.put(DEBIT, getBundle("Accounting").getString("DEBIT"))
        columnNames.put(CREDIT, getBundle("Accounting").getString("CREDIT"))
        columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"))
        columnNames.put(DESCRIPTION, getBundle("Accounting").getString("DESCRIPTION"))
    }

    void createColumnClasses() {
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
        rekening.businessObjects.size()
    }

    int getColumnCount() {
        columnClasses.size()
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    Booking getObject(int row, int col) {
        rekening.businessObjects.get(row).booking
    }

    int getRow(Booking booking){
        int row=0
        for(Movement movement:rekening.businessObjects){
            if(movement.booking!=booking){
                row++
            } else{
                return row
            }
        }
        return 0
    }

    Object getValueAt(int row, int col) {
        Movement movement = rekening.businessObjects.get(row)
        if (col == NR) return movement.transactionString
        if (col == DATE) return Utils.toString(movement.date)
        if (col == DEBIT) {
            if (movement.debit) return movement.amount
            return ""
        }
        if (col == CREDIT) {
            if (!movement.debit) return movement.amount
            return ""
        }
        if (col == DESCRIPTION) return movement.description
        if (col == VATINFO) return movement.booking.VATBookingsString
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
        Movement movement = rekening.businessObjects.get(row)
        if (col == 1) {
            Calendar newDate = Utils.toCalendar((String) value)
            Transaction transaction = getTransaction(movement)
            Journal journal = getJournal(movement)
            if(newDate!=null && transaction != null && journal != null) {
                transaction.date = newDate
                Main.fireJournalDataChanged(journal)
            }
        } else if (col == 4) {
            if(movement!=null) {
                movement.setDescription((String) value)
            }
        }
        fireTableDataChanged()
    }

    Journal getJournal(Movement movement){
        getTransaction(movement)==null?null:getTransaction(movement).journal
    }

    Transaction getTransaction(Movement movement){
        movement==null?null:movement.booking==null?null:movement.booking.transaction
    }
}

