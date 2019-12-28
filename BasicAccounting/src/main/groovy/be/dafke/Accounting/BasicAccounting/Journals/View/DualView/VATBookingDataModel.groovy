package be.dafke.Accounting.BasicAccounting.Journals.View.DualView

import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.Accounting.BusinessModel.VATBooking
import be.dafke.Accounting.BusinessModel.VATField
import be.dafke.Accounting.BusinessModel.VATMovement
import be.dafke.ComponentModel.SelectableTableModel

class VATBookingDataModel extends SelectableTableModel<VATBooking> {

    Transaction transaction

    void setTransaction(Transaction transaction){
        this.transaction = transaction
    }

    @Override
    String getColumnName(int col) {
        if(col == 1) "Field"
        else "Value"
    }
    @Override
    Class getColumnClass(int col) {
        if(col == 1) String.class
        else BigDecimal.class
    }

    @Override
    VATBooking getObject(int row, int col) {
        if(!transaction) return null
        ArrayList<VATBooking> vatBookings = transaction.mergedVatBookings
        vatBookings.get(row)
    }

    @Override
    int getRowCount() {
        if (!transaction) return 0
        ArrayList<VATBooking> vatBookings = transaction.mergedVatBookings
        vatBookings.size()
    }

    @Override
    int getColumnCount() {
        2
    }

    @Override
    Object getValueAt(int rowIndex, int col) {
        VATBooking vatBooking = getObject(rowIndex, col)
        if(col==0){
            VATField vatField = vatBooking.vatField
            return vatField.name
        } else {
            VATMovement vatMovement = vatBooking.vatMovement
            return vatMovement.amount
        }
        null
    }
}
