package be.dafke.BasicAccounting.VAT;

import be.dafke.BusinessModel.VATBooking;
import be.dafke.BusinessModel.VATTransaction;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class VATColorRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        VATBooking vatBooking = ((VATTransactionsDataModel) table.getModel()).getObject(row, column);
        if(vatBooking!=null) {
            VATTransaction vatTransaction = vatBooking.getVatTransaction();
            if (vatTransaction.isRegistered()) {
                cell.setForeground(Color.BLACK);
            } else {
                cell.setForeground(Color.RED);
            }
        }
        return cell;
    }
}