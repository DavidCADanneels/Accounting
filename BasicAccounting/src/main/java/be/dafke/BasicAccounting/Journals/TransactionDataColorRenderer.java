package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.VAT.VATTransactionsDataModel;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TransactionDataColorRenderer<T> extends DefaultTableCellRenderer {
    private Journal journal;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        SelectableTableModel<Booking> model = (SelectableTableModel<Booking>)table.getModel();
        Booking booking = model.getObject(row, column);
        if(booking!=null) {
            Transaction transaction = booking.getTransaction();
            if(column == TransactionDataModel.VATINFO) {
                if (transaction.isRegistered()) {
                    cell.setForeground(Color.BLACK);
                } else {
                    cell.setForeground(Color.RED);
                }
            }
        }
        return cell;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }
}