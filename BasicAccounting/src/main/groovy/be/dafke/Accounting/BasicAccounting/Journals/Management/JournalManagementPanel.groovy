package be.dafke.Accounting.BasicAccounting.Journals.Management


import be.dafke.Accounting.BasicAccounting.Journals.New.NewJournalDialog
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.table.TableColumn
import java.awt.*

import static java.util.ResourceBundle.getBundle

class JournalManagementPanel extends JPanel implements ListSelectionListener {

    JButton addButton, delete, newType, edit
    final DefaultListSelectionModel selection
    SelectableTable<Journal> tabel
    JournalManagementTableModel journalManagementTableModel
    Journals journals
    JournalTypes journalTypes
    Accounts accounts
    AccountTypes accountTypes
    JComboBox<JournalType> comboBox
    TableColumn column

    JournalManagementPanel(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        setLayout(new BorderLayout())
        this.journals = journals
        this.journalTypes = journalTypes
        this.accounts = accounts
        this.accountTypes = accountTypes
        journalManagementTableModel = new JournalManagementTableModel(this, journals)

        tabel = new SelectableTable<>(journalManagementTableModel)
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))

        selection = new DefaultListSelectionModel()
        selection.addListSelectionListener(this)
        tabel.setSelectionModel(selection)

        comboBox=createComboBox()

        column = tabel.getColumnModel().getColumn(JournalManagementTableModel.TYPE_COL)
        column.setCellEditor(new DefaultCellEditor(comboBox))

        JScrollPane scrollPane = new JScrollPane(tabel)
        add(scrollPane, BorderLayout.CENTER)

        JPanel south = createButtonPanel()
        add(south, BorderLayout.SOUTH)
    }

    JComboBox<JournalType> createComboBox() {
        JComboBox<JournalType> comboBox = new JComboBox<>()
        comboBox.removeAllItems()
        journalTypes.businessObjects.forEach({ journalType -> comboBox.addItem(journalType) })
        comboBox
    }

    JPanel createButtonPanel(){
        JPanel south = new JPanel()
        delete = new JButton(getBundle("Accounting").getString("DELETE_JOURNAL"))
        edit = new JButton(getBundle("Accounting").getString("EDIT_JOURNAL"))
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"))
        addButton = new JButton(getBundle("Accounting").getString("NEW_JOURNAL"))
        delete.addActionListener({ e -> deleteJournal() })
        edit.addActionListener({ e ->
            Journal journal = tabel.selectedObject
            NewJournalDialog newJournalDialog = new NewJournalDialog(accounts, journals, journalTypes, accountTypes)
            newJournalDialog.journal = journal
            newJournalDialog.visible = true
        })
        newType.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            JournalTypeManagementGUI journalTypeManagementGUI = JournalTypeManagementGUI.getInstance(accounts, journalTypes, accountTypes)
            journalTypeManagementGUI.setLocation(locationOnScreen)
            journalTypeManagementGUI.visible = true
        })
        addButton.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            NewJournalDialog newJournalDialog = new NewJournalDialog(accounts, journals, journalTypes, accountTypes)
            newJournalDialog.setLocation(locationOnScreen)
            newJournalDialog.visible = true
        })
        delete.enabled = false
        edit.enabled = false
        south.add(delete)
        south.add(edit)
        south.add(newType)
        south.add(addButton)
        south
    }

    void deleteJournal(ArrayList<Journal> journalList, Journals journals) {
        ArrayList<String> failed = new ArrayList<>()
        for(Journal journal : journalList) {
            try{
                journals.removeBusinessObject(journal)
            }catch (NotEmptyException e){
                failed.add(journal.name)
            }
        }
        if (failed.size() > 0) {
            if (failed.size() == 1) {
                JOptionPane.showMessageDialog(null, failed.get(0) + " "+ getBundle("BusinessActions").getString("JOURNAL_NOT_EMPTY"))
            } else {
                StringBuilder builder = new StringBuilder(getBundle("BusinessActions").getString("MULTIPLE_JOURNALS_NOT_EMPTY")+"\n")
                for(String s : failed){
                    builder.append("- ").append(s).append("\n")
                }
                JOptionPane.showMessageDialog(null, builder.toString())
            }
        }
    }

    void deleteJournal() {
        ArrayList<Journal> journalList = tabel.selectedObjects
        if (!journalList.empty) {
            deleteJournal(journalList, journals)
            fireJournalDataChanged()
        }
    }

    void fireJournalDataChanged() {
        journalManagementTableModel.fireTableDataChanged()
    }

    void fireJournalTypeDataChanged(JournalTypes journalTypes){
        this.journalTypes = journalTypes
        comboBox = createComboBox()
        column.setCellEditor(new DefaultCellEditor(comboBox))
//        fireJournalDataChanged()
        revalidate()
    }

    void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int[] rows = tabel.selectedRows
            boolean enabled = rows.length != 0
            delete.enabled = enabled
            edit.enabled = enabled
        }
    }
}
