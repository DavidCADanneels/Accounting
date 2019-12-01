package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BusinessModel.Mortgage
import be.dafke.Accounting.BusinessModel.MortgageTransaction

import javax.swing.table.AbstractTableModel

class MortgageDataModel extends AbstractTableModel {
    private final String[] columnNames = [ "Nr", "Mensualiteit", "Intrest", "Kapitaal", "RestKapitaal" ]
    private final Class[] columnClasses = [ Integer.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,
        BigDecimal.class ]
    private Mortgage data

    MortgageDataModel(Mortgage data) {
        this.data = data
    }

    void revalidate(Mortgage data) {
        this.data = data
    }

    int getColumnCount() {
        5
    }

    int getRowCount() {
        data == null ? 0 : data.getBusinessObjects().size()
    }

    /**
     * Is called to refresh the displayed data from the Mortgages table --> only read data !!!
     *
     * @param row
     * @param col
     * @return
     */
    Object getValueAt(int row, int col) {
        if (col == 0) {
            data.getBusinessObjects().get(row).getNr()
        } else if (col == 1) {
            data.getBusinessObjects().get(row).getMensuality()
        } else if (col == 2) {
            data.getBusinessObjects().get(row).getIntrest()
        } else if (col == 3) {
            data.getBusinessObjects().get(row).getCapital()
        } else if (col == 4) {
            data.getBusinessObjects().get(row).getRestCapital()
        } else null
    }

    @Override
    String getColumnName(int col) {
        columnNames[col]
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses[col]
    }

    @Override
    boolean isCellEditable(int row, int col) {
        (col == 2 || col == 3)
    }

    // DE SET METHODEN
    // ===============
    @Override
    /**
     * Is called when user updates the value in the table -> update Mortgages table
     */
    void setValueAt(Object value, int row, int col) {
        BigDecimal amount = (BigDecimal) value
        MortgageTransaction mortgageTransaction = data.getBusinessObjects().get(row)
        if (col == 2) {
            mortgageTransaction.setIntrest(amount, true)
        } else if (col == 3) {
            mortgageTransaction.setCapital(amount, true)
        }
//		if(...){ // TODO add option to disable auto update of below rows.
        data.recalculateTable(row)
//		}
        fireTableDataChanged()
    }
}
