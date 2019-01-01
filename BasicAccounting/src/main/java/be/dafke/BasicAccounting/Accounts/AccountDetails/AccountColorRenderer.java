package be.dafke.BasicAccounting.Accounts.AccountDetails;

import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AccountColorRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Booking booking = ((AccountDetailsDataModel) table.getModel()).getObject(row, column);
        if(booking!=null) {
            Transaction transaction = booking.getTransaction();
            if(isSelected){
                cell.setBackground(Color.CYAN);
            } else {
                cell.setBackground(Color.WHITE);
            }
            if (transaction.isBalanceTransaction()) {
                cell.setForeground(Color.RED);
            } else {
                cell.setForeground(Color.BLACK);
            }
        }
        return cell;
    }
}