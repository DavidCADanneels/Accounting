package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;

import javax.swing.table.AbstractTableModel;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 5-1-14
 * Time: 16:22
 */
public class JournalManagementTableModel extends AbstractTableModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String[] columnNames = { getBundle("Accounting").getString("JOURNAL_NAME"),
            getBundle("Accounting").getString("TYPE"), getBundle("Accounting").getString("NEXT_INDEX") };
    private final Class[] columnClasses = { Journal.class, String.class, Integer.class };
    private final Journals journals;

    public JournalManagementTableModel(Journals journals) {
        this.journals = journals;
    }

    public int getColumnCount() {
        return columnClasses.length;
    }

    public int getRowCount() {
        return journals.getBusinessObjects().size();
    }

    public Object getValueAt(int row, int col) {
        Journal journal = journals.getBusinessObjects().get(row);
        if (col == 0) {
            return journal;
        } else if (col == 1) {
            return journal.getType();
        } else if (col == 2) {
            return journal.getId();
        } else return null;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    // DE SET METHODEN
    // ===============
    @Override
    public void setValueAt(Object value, int row, int col) {
    }
}