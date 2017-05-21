package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTableModel;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 5-1-14
 * Time: 16:22
 */
public class JournalManagementTableModel extends SelectableTableModel<Journal> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int NAME_COL = 0;
    public static final int ABBR_COL = 1;
    public static final int TYPE_COL = 2;
    public static final int NEXT_ID_COL = 3;
    private HashMap<Integer, String> columnNames = new HashMap<>();
    private HashMap<Integer, Class> columnClasses = new HashMap<>();
    private final Journals journals;

    public JournalManagementTableModel(Journals journals) {
        this.journals = journals;
        createColumnNames();
        createColumnClasses();
    }

    private void createColumnClasses() {
        columnClasses.put(NAME_COL, String.class);
        columnClasses.put(ABBR_COL, String.class);
        columnClasses.put(TYPE_COL, String.class);
        columnClasses.put(NEXT_ID_COL, Integer.class);
    }

    private void createColumnNames() {
        columnNames.put(NAME_COL, getBundle("Accounting").getString("NAME"));
        columnNames.put(ABBR_COL, getBundle("Accounting").getString("ABBR"));
        columnNames.put(TYPE_COL, getBundle("Accounting").getString("TYPE"));
        columnNames.put(NEXT_ID_COL, getBundle("Accounting").getString("NEXT_INDEX"));
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public int getRowCount() {
        return journals.getBusinessObjects().size();
    }

    public Object getValueAt(int row, int col) {
        Journal journal = journals.getBusinessObjects().get(row);
        if (col == NAME_COL) {
            return journal.getName();
        } else if (col == ABBR_COL) {
            return journal.getAbbreviation();
        } else if (col == TYPE_COL) {
            return journal.getType();
        } else if (col == NEXT_ID_COL) {
            return journal.getId();
        } else return null;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames.get(col);
    }

    @Override
    public Class getColumnClass(int col) {
        return columnClasses.get(col);
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

    @Override
    public Journal getObject(int row, int col) {
        return null;
    }
}