package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.VAT.VATTransactionsDataModel;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Transaction;
import be.dafke.BusinessModel.VATBooking;
import be.dafke.BusinessModel.VATTransaction;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class JournalColorRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Booking booking = ((JournalDetailsDataModel) table.getModel()).getObject(row, column);
        if(booking!=null) {
            Transaction transaction = booking.getTransaction();
            if (transaction.isBalanceTransaction()) {
                cell.setForeground(Color.RED);
            } else {
                cell.setForeground(Color.BLACK);
            }
        }
        return cell;
    }
}