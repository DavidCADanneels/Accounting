package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;

import static be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI.showJournalTypeManager;
import static java.util.ResourceBundle.getBundle;

public class NewJournalPanel extends JPanel {
    private JTextField name, abbr;
    private JComboBox<JournalType> type;
    private JButton add, newType;
    private Journals journals;
    private Journal journal;

    public NewJournalPanel(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        this.journals = journals;
        
        setLayout(new GridLayout(0,2));
        add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")));
        name = new JTextField(20);
        add(name);
        add(new JLabel(getBundle("Accounting").getString("ABBR_LABEL")));
        abbr = new JTextField(6);
        add(abbr);
        add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")));
        type = new JComboBox<>();
        DefaultComboBoxModel<JournalType> model = new DefaultComboBoxModel<>();
        for(JournalType accountType : journalTypes.getBusinessObjects()){
            model.addElement(accountType);
        }
        type.setModel(model);
        add(type);
        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_JOURNAL"));
        add.addActionListener(e -> saveJournal());
        add(add);
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        newType.addActionListener(e -> showJournalTypeManager(accounts, journalTypes,accountTypes));
        add(newType);
    }

    public void setJournal(Journal journal){
        this.journal = journal;
        name.setText(journal.getName());
        abbr.setText(journal.getAbbreviation());
        type.setSelectedItem(journal.getType());
    }

    private void saveJournal() {
        String newName = name.getText().trim();
        String newAbbreviation = abbr.getText().trim();
        if(!newName.isEmpty() && newAbbreviation.isEmpty() && newName.length() > 2) {
            newAbbreviation = newName.substring(0, 3).toUpperCase();
            abbr.setText(newAbbreviation);
        }
        JournalType journalType = (JournalType)type.getSelectedItem();
        try{
            if(journal==null) {
                journal = new Journal(newName, newAbbreviation);
                journals.addBusinessObject(journal);
                journal.setType(journalType);
                clearFields();
                journal=null;
            } else {
                String oldName = journal.getName();
                String oldAbbreviation = journal.getAbbreviation();
                journals.modifyName(oldName, newName);
                journals.modifyJournalAbbreviation(oldAbbreviation, newAbbreviation);
                journal.setType(journalType);
            }
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_NAME_AND_OR_ABBR, newName.trim(), newAbbreviation.trim());
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_NAME_ABBR_EMPTY);
        }
        Main.fireJournalDataChanged(journal);
    }

    private void clearFields() {
        name.setText("");
        abbr.setText("");
    }
}