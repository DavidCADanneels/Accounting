package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.GUIActions;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewJournalGUI extends RefreshableDialog implements ActionListener{
    private final JTextField name, abbr;
    private final JComboBox<JournalType> type;
    private final JButton add, newType;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private Journals journals;

    public NewJournalGUI(JournalTypes journalTypes) {
        super(getBundle("Accounting").getString("NEW_JOURNAL_GUI_TITLE"));
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        this.journals = journals;
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
		add.addActionListener(this);
		line2.add(add);
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        newType.addActionListener(this);
        line2.add(newType);
        north.add(line1);
		north.add(line2);
        setContentPane(north);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add || e.getSource() == name || e.getSource() == abbr) {
            addJournal();
            ComponentMap.refreshAllFrames();
        }else if (e.getSource() == newType) {
            GUIActions.showJournalTypeManager(accountTypes);
        }
    }

//    @Override
//    public void refresh(){
//        type.removeAllItems();
//        for(JournalType journalType : accounting.getJournalTypes().getBusinessObjects()){
//            type.addItem(journalType);
//        }
//        super.refresh();
//    }

    private void addJournal() {
        String newName = name.getText().trim();
        String abbreviation = abbr.getText().trim();
        if(!newName.isEmpty() && abbreviation.isEmpty() && newName.length() > 2) {
            abbreviation = newName.substring(0, 3).toUpperCase();
            abbr.setText(abbreviation);
        }
        JournalType journalType = (JournalType)type.getSelectedItem();
        try {
            Journal journal = new Journal(accounts, /*journals,*/ newName, abbreviation);
            journal.setType(journalType);
            journals.addBusinessObject(journal);
            journals.setCurrentObject(journal);
            ComponentMap.refreshAllFrames();
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_NAME_AND_OR_ABBR,newName.trim(), abbreviation.trim());
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.JOURNAL_NAME_ABBR_EMPTY);
        }
        name.setText("");
        abbr.setText("");
    }

    public void refresh() {
        // nothing to do here
    }
}
