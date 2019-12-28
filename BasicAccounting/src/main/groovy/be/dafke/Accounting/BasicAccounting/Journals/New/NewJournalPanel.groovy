package be.dafke.Accounting.BasicAccounting.Journals.New

import be.dafke.Accounting.BasicAccounting.Journals.Management.JournalTypeManagementGUI
import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class NewJournalPanel extends JPanel {
    JTextField nameField, abbr
    JComboBox<JournalType> type
    Journals journals
    Journal journal

    NewJournalPanel(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        this.journals = journals

        setLayout(new GridLayout(0,2))
        add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")))
        nameField = new JTextField(20)
        add(nameField)
        add(new JLabel(getBundle("Accounting").getString("ABBR_LABEL")))
        abbr = new JTextField(6)
        add(abbr)
        add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")))
        type = new JComboBox<>()
        DefaultComboBoxModel<JournalType> model = new DefaultComboBoxModel<>()
        for(JournalType accountType : journalTypes.businessObjects){
            model.addElement(accountType)
        }
        type.setModel(model)
        add(type)
        JButton addButton = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_JOURNAL"))
        addButton.addActionListener({ e -> saveJournal() })
        add(addButton)
        JButton newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"))
        newType.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            JournalTypeManagementGUI journalTypeManagementGUI = JournalTypeManagementGUI.getInstance(accounts, journalTypes, accountTypes)
            journalTypeManagementGUI.setLocation(locationOnScreen)
            journalTypeManagementGUI.visible = true
        })
        add(newType)
    }

    void setJournal(Journal journal){
        this.journal = journal
        nameField.setText(journal.name)
        abbr.setText(journal.abbreviation)
        type.setSelectedItem(journal.type)
    }

    void saveJournal() {
        String newName = nameField.getText().trim()
        String newAbbreviation = abbr.getText().trim()
        if(!newName.empty && newAbbreviation.empty && newName.length() > 2) {
            newAbbreviation = newName.substring(0, 3).toUpperCase()
            abbr.setText(newAbbreviation)
        }
        JournalType journalType = (JournalType)type.selectedItem
        try{
            if(journal==null) {
                journal = new Journal(newName, newAbbreviation)
                journal.type = journalType
                journals.addBusinessObject(journal)
                Main.fireJournalAdded(journals)
                clearFields()
                journal=null
            } else {
                String oldName = journal.name
                String oldAbbreviation = journal.abbreviation
                journals.modifyName(oldName, newName)
                journals.modifyJournalAbbreviation(oldAbbreviation, newAbbreviation)
                journal.type = journalType
                Main.fireJournalDataChanged(journal)
            }
            Main.fireJournalTypeChanges(journal, journalType)
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.JOURNAL_DUPLICATE_NAME_AND_OR_ABBR, newName.trim(), newAbbreviation.trim())
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.JOURNAL_NAME_ABBR_EMPTY)
        }

    }

    void clearFields() {
        nameField.setText("")
        abbr.setText("")
    }
}
