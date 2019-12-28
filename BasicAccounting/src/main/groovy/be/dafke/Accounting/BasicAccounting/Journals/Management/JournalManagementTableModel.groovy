package be.dafke.Accounting.BasicAccounting.Journals.Management

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.JournalType
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTableModel

import java.awt.*

import static java.util.ResourceBundle.getBundle

class JournalManagementTableModel extends SelectableTableModel<Journal> {
    static final int NAME_COL = 0
    static final int ABBR_COL = 1
    static final int TYPE_COL = 2
    static final int NEXT_ID_COL = 3
    HashMap<Integer, String> columnNames = new HashMap<>()
    HashMap<Integer, Class> columnClasses = new HashMap<>()
    final Journals journals
    Component parent

    JournalManagementTableModel(Component parent, Journals journals) {
        this.parent = parent
        this.journals = journals
        createColumnNames()
        createColumnClasses()
    }

    void createColumnClasses() {
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(ABBR_COL, String.class)
        columnClasses.put(TYPE_COL, String.class)
        columnClasses.put(NEXT_ID_COL, Integer.class)
    }

    void createColumnNames() {
        columnNames.put(NAME_COL, getBundle("Accounting").getString("NAME"))
        columnNames.put(ABBR_COL, getBundle("Accounting").getString("ABBR"))
        columnNames.put(TYPE_COL, getBundle("Accounting").getString("TYPE"))
        columnNames.put(NEXT_ID_COL, getBundle("Accounting").getString("NEXT_INDEX"))
    }

    int getColumnCount() {
        columnNames.size()
    }

    int getRowCount() {
        journals.businessObjects.size()
    }

    Object getValueAt(int row, int col) {
        Journal journal = journals.businessObjects.get(row)
        if (col == NAME_COL) {
            return journal.name
        } else if (col == ABBR_COL) {
            return journal.abbreviation
        } else if (col == TYPE_COL) {
            return journal.type
        } else if (col == NEXT_ID_COL) {
            return journal.id
        }
        null
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        col!=NEXT_ID_COL
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        Journal journal = journals.businessObjects.get(row)
        if (col == NAME_COL) {
            String oldName = journal.name
            String newName = (String)value
            if(newName && !oldName.trim().equals(newName.trim())) {
                try {
                    journals.modifyName(oldName, newName)
                    Main.fireJournalDataChanged(journal)
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.JOURNAL_NAME_EMPTY)
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.JOURNAL_DUPLICATE_NAME, newName.trim())
                }
            }
        } else if (col == ABBR_COL) {
            String oldAbbreviation = journal.abbreviation
            String newAbbreviation = (String)value
            if(newAbbreviation && !oldAbbreviation.trim().equals(newAbbreviation.trim())){
                try {
                    journals.modifyJournalAbbreviation(oldAbbreviation, newAbbreviation)
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.JOURNAL_DUPLICATE_ABBR,newAbbreviation.trim())
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.JOURNAL_ABBR_EMPTY)
                }
            }
        } else if (col == TYPE_COL) {
            JournalType journalType = (JournalType)value
            journal.type = journalType
            Main.fireJournalTypeChanges journal, journalType
        }
    }

    @Override
    Journal getObject(int row, int col) {
        journals.businessObjects.get(row)
    }
}