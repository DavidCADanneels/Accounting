package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.JournalType;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.awt.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 5-1-14
 * Time: 16:22
 */
public class JournalManagementTableModel extends SelectableTableModel<Journal> {
    public static final int NAME_COL = 0;
    public static final int ABBR_COL = 1;
    public static final int TYPE_COL = 2;
    public static final int NEXT_ID_COL = 3;
    private HashMap<Integer, String> columnNames = new HashMap<>();
    private HashMap<Integer, Class> columnClasses = new HashMap<>();
    private final Journals journals;
    private Component parent;

    public JournalManagementTableModel(Component parent, Journals journals) {
        this.parent = parent;
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
        return col!=NEXT_ID_COL;
    }

    // DE SET METHODEN
    // ===============
    @Override
    public void setValueAt(Object value, int row, int col) {
        Journal journal = journals.getBusinessObjects().get(row);
        if (col == NAME_COL) {
            String oldName = journal.getName();
            String newName = (String)value;
            if(newName!=null && !oldName.trim().equals(newName.trim())) {
                try {
                    journals.modifyName(oldName, newName);
                    Main.fireJournalDataChanged(journal);
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.JOURNAL_NAME_EMPTY);
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.JOURNAL_DUPLICATE_NAME, newName.trim());
                }
            }
        } else if (col == ABBR_COL) {
            String oldAbbreviation = journal.getAbbreviation();
            String newAbbreviation = (String)value;
            if(newAbbreviation!=null && !oldAbbreviation.trim().equals(newAbbreviation.trim())){
                try {
                    journals.modifyJournalAbbreviation(oldAbbreviation, newAbbreviation);
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.JOURNAL_DUPLICATE_ABBR,newAbbreviation.trim());
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.JOURNAL_ABBR_EMPTY);
                }
            }
        } else if (col == TYPE_COL) {
            JournalType journalType = (JournalType)value;
            journal.setType(journalType);
            Main.fireJournalTypeChanges(journal, journalType);
        }
    }

    @Override
    public Journal getObject(int row, int col) {
        return journals.getBusinessObjects().get(row);
    }
}