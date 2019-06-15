package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;

public class VATBookingDataModel extends SelectableTableModel<VATBooking> {

    Transaction transaction;

    public void setTransaction(Transaction transaction){
        this.transaction = transaction;
    }

    @Override
    public String getColumnName(int col) {
        if(col == 1) return "Field";
        else return "Value";
    }
    @Override
    public Class getColumnClass(int col) {
        if(col == 1) return String.class;
        else return BigDecimal.class;
    }

    @Override
    public VATBooking getObject(int row, int col) {
        if(transaction==null) return null;
        VATTransaction vatTransaction = transaction.getVatTransaction();
        ArrayList<VATBooking> vatBookings = vatTransaction.getBusinessObjects();
        return vatBookings.get(row);
    }

    @Override
    public int getRowCount() {
        if (transaction==null) return 0;
        VATTransaction vatTransaction = transaction.getVatTransaction();
        if(vatTransaction==null) return 0;
        return vatTransaction.getBusinessObjects().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int col) {
        VATBooking vatBooking = getObject(rowIndex, col);
        if(col==0){
            VATField vatField = vatBooking.getVatField();
            return vatField.getName();
        } else {
            VATMovement vatMovement = vatBooking.getVatMovement();
            return vatMovement.getAmount();
        }
    }
}
