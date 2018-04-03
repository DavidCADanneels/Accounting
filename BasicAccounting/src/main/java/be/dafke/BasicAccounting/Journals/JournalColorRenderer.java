package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.VAT.VATTransactionsDataModel;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class JournalColorRenderer extends DefaultTableCellRenderer {
    private Journal journal;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Booking booking = ((JournalDetailsDataModel) table.getModel()).getObject(row, column);
        if(booking!=null) {
            Transaction transaction = booking.getTransaction();
            Journal transactionJournal = transaction.getJournal();
            if(isSelected){
                cell.setBackground(Color.CYAN);
            } else if(journal == null || transactionJournal == null){
                cell.setBackground(Color.RED);
            } else if(journal != transactionJournal){
                cell.setBackground(Color.GREEN);
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

    public void setJournal(Journal journal) {
        this.journal = journal;
    }
}