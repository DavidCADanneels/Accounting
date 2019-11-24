package be.dafke.BasicAccounting.Trade;

import be.dafke.Accounting.BusinessModel.SalesOrder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SalesOrderPayedColorRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        SalesOrder salesOrder = ((SalesOrdersOverviewDataTableModel) table.getModel()).getObject(row, column);
        if(isSelected){
            cell.setBackground(Color.CYAN);
        } else {
            cell.setBackground(Color.WHITE);
        }
        if (salesOrder != null) {
            if (salesOrder.getPaymentTransaction()!= null) {
                cell.setForeground(Color.BLACK);
            } else {
                cell.setForeground(Color.RED);
            }
        }
        return cell;
    }
}