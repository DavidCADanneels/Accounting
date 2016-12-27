package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import static be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI.showJournalTypeManager;
import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewJournalGUI extends RefreshableDialog {
    private final JTextField name, abbr;
    private final JComboBox<JournalType> type;
    private final JButton add, newType;
    private Accounts accounts;
    private Journals journals;
    private VATTransactions vatTransactions;

    public NewJournalGUI(Journals journals, JournalTypes journalTypes, Accounts accounts, AccountTypes accountTypes, VATTransactions vatTransactions) {
        super(getBundle("Accounting").getString("NEW_JOURNAL_GUI_TITLE"));
        this.accounts = accounts;
        this.journals = journals;
        this.vatTransactions = vatTransactions;
        JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JPanel line1 = new JPanel();
        name = new JTextField(20);
        abbr = new JTextField(6);
        line1.add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")));
		line1.add(name);
        line1.add(new JLabel(getBundle("Accounting").getString("ABBR_LABEL")));
        line1.add(abbr);
		JPanel line2 = new JPanel();
		line2.add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")));
		type = new JComboBox<>();
        DefaultComboBoxModel<JournalType> model = new DefaultComboBoxModel<>();
        for(JournalType accountType : journalTypes.getBusinessObjects()){
            model.addElement(accountType);
        }
        type.setModel(model);
		line2.add(type);
		add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_JOURNAL"));
		add.addActionListener(e -> addJournal());
		line2.add(add);
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        newType.addActionListener(e -> showJournalTypeManager(journalTypes,accountTypes));
        line2.add(newType);
        north.add(line1);
		north.add(line2);
        setContentPane(north);
        pack();
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
            Journal journal = new Journal(accounts, newName, abbreviation, vatTransactions);
            journal.setType(journalType);
            journals.addBusinessObject(journal);
            journals.setCurrentObject(journal);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_NAME_AND_OR_ABBR,newName.trim(), abbreviation.trim());
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_NAME_ABBR_EMPTY);
        }
        name.setText("");
        abbr.setText("");
    }
}
