package be.dafke.BasicAccounting.Trade;

import be.dafke.BasicAccounting.VAT.VATTransactionsDataModel;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.BusinessModel.VATBooking;
import be.dafke.BusinessModel.VATTransaction;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SalesOrderInvoiceColorRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        SalesOrder salesOrder = ((SalesOrdersOverviewDataTableModel) table.getModel()).getObject(row, column);
        if(isSelected){
            cell.setBackground(Color.CYAN);
        } else if (salesOrder != null) {
            if (salesOrder.isInvoice()) {
                cell.setBackground(Color.GREEN);
            } else {
                cell.setBackground(Color.WHITE);
            }
        }
        return cell;
    }
}