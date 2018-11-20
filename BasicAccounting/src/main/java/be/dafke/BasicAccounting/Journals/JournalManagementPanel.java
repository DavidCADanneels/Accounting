package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class JournalManagementPanel extends JPanel implements ListSelectionListener {

    private JButton add, delete, newType, edit;
    private final DefaultListSelectionModel selection;
    private SelectableTable<Journal> tabel;
    private JournalManagementTableModel journalManagementTableModel;
    private Journals journals;
    private JournalTypes journalTypes;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private JComboBox<JournalType> comboBox;
    private TableColumn column;

    public JournalManagementPanel(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        setLayout(new BorderLayout());
        this.journals = journals;
        this.journalTypes = journalTypes;
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        journalManagementTableModel = new JournalManagementTableModel(this, journals);

        tabel = new SelectableTable<>(journalManagementTableModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        tabel.setRowSorter(null);

        selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(this);
        tabel.setSelectionModel(selection);

        comboBox=createComboBox();

        column = tabel.getColumnModel().getColumn(JournalManagementTableModel.TYPE_COL);
        column.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane scrollPane = new JScrollPane(tabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel south = createButtonPanel();
        add(south, BorderLayout.SOUTH);
    }

    private JComboBox<JournalType> createComboBox() {
        JComboBox<JournalType> comboBox = new JComboBox<>();
        comboBox.removeAllItems();
        journalTypes.getBusinessObjects().forEach(journalType -> comboBox.addItem(journalType));
        return comboBox;
    }

    private JPanel createButtonPanel(){
        JPanel south = new JPanel();
        delete = new JButton(getBundle("Accounting").getString("DELETE_JOURNAL"));
        edit = new JButton(getBundle("Accounting").getString("EDIT_JOURNAL"));
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        add = new JButton(getBundle("Accounting").getString("NEW_JOURNAL"));
        delete.addActionListener(e -> deleteJournal());
        edit.addActionListener(e -> {
            int selectedRow = tabel.getSelectedRow();
            Journal journal = journalManagementTableModel.getObject(selectedRow, 0);
            NewJournalDialog newJournalDialog = new NewJournalDialog(accounts, journals, journalTypes, accountTypes);
            newJournalDialog.setJournal(journal);
            newJournalDialog.setVisible(true);
        });
        newType.addActionListener(e -> {
            Point locationOnScreen = getLocationOnScreen();
            JournalTypeManagementGUI journalTypeManagementGUI = JournalTypeManagementGUI.getInstance(accounts, journalTypes, accountTypes);
            journalTypeManagementGUI.setLocation(locationOnScreen);
            journalTypeManagementGUI.setVisible(true);
        });
        add.addActionListener(e -> {
            Point locationOnScreen = getLocationOnScreen();
            NewJournalDialog newJournalDialog = new NewJournalDialog(accounts, journals, journalTypes, accountTypes);
            newJournalDialog.setLocation(locationOnScreen);
            newJournalDialog.setVisible(true);
        });
        delete.setEnabled(false);
        edit.setEnabled(false);
        south.add(delete);
        south.add(edit);
        south.add(newType);
        south.add(add);
        return south;
    }

    private ArrayList<Journal> getSelectedJournals(){
        int[] rows = tabel.getSelectedRows();
        if (rows.length == 0) {
            ActionUtils.showErrorMessage(this, ActionUtils.SELECT_JOURNAL_FIRST);
        }
        ArrayList<Journal> journalList = new ArrayList<>();
        for(int row : rows) {
            Journal journal = (Journal) tabel.getModel().getValueAt(row, 0);
            journalList.add(journal);
        }
        return journalList;

    }

    private void deleteJournal(ArrayList<Journal> journalList, Journals journals) {
        ArrayList<String> failed = new ArrayList<>();
        for(Journal journal : journalList) {
            try{
                journals.removeBusinessObject(journal);
            }catch (NotEmptyException e){
                failed.add(journal.getName());
            }
        }
        if (failed.size() > 0) {
            if (failed.size() == 1) {
                JOptionPane.showMessageDialog(null, failed.get(0) + " "+ getBundle("BusinessActions").getString("JOURNAL_NOT_EMPTY"));
            } else {
                StringBuilder builder = new StringBuilder(getBundle("BusinessActions").getString("MULTIPLE_JOURNALS_NOT_EMPTY")+"\n");
                for(String s : failed){
                    builder.append("- ").append(s).append("\n");
                }
                JOptionPane.showMessageDialog(null, builder.toString());
            }
        }
    }

    public void deleteJournal() {
        ArrayList<Journal> journalList = getSelectedJournals();
        if (!journalList.isEmpty()) {
            deleteJournal(journalList, journals);
            fireJournalDataChanged();
        }
    }

    public void fireJournalDataChanged() {
        journalManagementTableModel.fireTableDataChanged();
    }

    public void fireJournalTypeDataChanged(JournalTypes journalTypes){
        this.journalTypes = journalTypes;
        comboBox = createComboBox();
        column.setCellEditor(new DefaultCellEditor(comboBox));
//        fireJournalDataChanged();
        revalidate();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int[] rows = tabel.getSelectedRows();
            boolean enabled = rows.length != 0;
            delete.setEnabled(enabled);
            edit.setEnabled(enabled);
        }
    }
}
