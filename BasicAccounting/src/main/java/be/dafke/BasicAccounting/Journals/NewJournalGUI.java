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

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewJournalGUI extends JFrame {
    private static NewJournalGUI newJournalGUI = null;
    private JTextField name, abbr;
    private JComboBox<JournalType> type;
    private JButton add, newType;
    private Journals journals;
    private Journal journal;

    private NewJournalGUI(Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("NEW_JOURNAL_GUI_TITLE"));
        this.journals = journals;
        setContentPane(createContentPanel(journalTypes, accountTypes));
        pack();
    }

    public static NewJournalGUI getInstance(Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        if(newJournalGUI == null) {
            newJournalGUI = new NewJournalGUI(journals, journalTypes, accountTypes);
            Main.addFrame(newJournalGUI);
        }
        return newJournalGUI;
    }

    private JPanel createContentPanel(JournalTypes journalTypes, AccountTypes accountTypes){
        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")));
        name = new JTextField(20);
        panel.add(name);
        panel.add(new JLabel(getBundle("Accounting").getString("ABBR_LABEL")));
        abbr = new JTextField(6);
        panel.add(abbr);
        panel.add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")));
        type = new JComboBox<>();
        DefaultComboBoxModel<JournalType> model = new DefaultComboBoxModel<>();
        for(JournalType accountType : journalTypes.getBusinessObjects()){
            model.addElement(accountType);
        }
        type.setModel(model);
        panel.add(type);
        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_JOURNAL"));
        add.addActionListener(e -> saveJournal());
        panel.add(add);
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        newType.addActionListener(e -> showJournalTypeManager(journalTypes,accountTypes));
        panel.add(newType);
        return panel;
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
                clearFields();
                journal=null;
            } else {
                String oldName = journal.getName();
                String oldAbbreviation = journal.getAbbreviation();
                journals.modifyName(oldName, newName);
                journals.modifyJournalAbbreviation(oldAbbreviation, newAbbreviation);
            }
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_NAME_AND_OR_ABBR, newName.trim(), newAbbreviation.trim());
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_NAME_ABBR_EMPTY);
        }
        journal.setType(journalType);
        Main.fireJournalDataChanged(journal);
    }

    private void clearFields() {
        name.setText("");
        abbr.setText("");
    }
}
