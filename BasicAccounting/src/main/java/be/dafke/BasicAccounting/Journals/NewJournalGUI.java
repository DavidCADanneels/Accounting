package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableDialog;
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
public class NewJournalGUI extends RefreshableDialog {
    private JTextField name, abbr;
    private JComboBox<JournalType> type;
    private JButton add, newType;
    private Journals journals;

    public NewJournalGUI(Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("NEW_JOURNAL_GUI_TITLE"));
        this.journals = journals;
        setContentPane(createContentPanel(journalTypes, accountTypes));
        pack();
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
        add.addActionListener(e -> addJournal());
        panel.add(add);
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        newType.addActionListener(e -> showJournalTypeManager(journalTypes,accountTypes));
        panel.add(newType);
        return panel;
    }

    private void addJournal() {
        String newName = name.getText().trim();
        String abbreviation = abbr.getText().trim();
        if(!newName.isEmpty() && abbreviation.isEmpty() && newName.length() > 2) {
            abbreviation = newName.substring(0, 3).toUpperCase();
            abbr.setText(abbreviation);
        }
        JournalType journalType = (JournalType)type.getSelectedItem();
        try {
            Journal journal = new Journal(newName, abbreviation);
            journal.setType(journalType);
            journals.addBusinessObject(journal);
            Main.addJournal(journal);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_NAME_AND_OR_ABBR,newName.trim(), abbreviation.trim());
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_NAME_ABBR_EMPTY);
        }
        name.setText("");
        abbr.setText("");
    }
}
