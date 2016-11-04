package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.GUIActions;
import be.dafke.BusinessActions.SetJournalListener;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class JournalsGUI extends AccountingPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MANAGE = "manage";
	public static final String DETAILS = "details";
	private JComboBox<Journal> combo;
	private final JButton journalManagement, details;
	private Journal selectedJournal = null;
	private ArrayList<SetJournalListener> setJournalListeners = new ArrayList<>();
	private Journals journals;
	private JournalTypes journalTypes;
	private Accounts accounts;

	public JournalsGUI() {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<>();
        combo.addActionListener(e -> {
            Journal newJournal = (Journal) combo.getSelectedItem();
            Journal journal = GUIActions.switchJournal(accounts, selectedJournal, newJournal);
            for (SetJournalListener setJournalListener : setJournalListeners){
				setJournalListener.setJournal(journal);
            }
            setJournal(newJournal);
        });
		combo.setEnabled(false);
		add(combo);

		JPanel paneel = new JPanel();

		journalManagement = new JButton(getBundle("Accounting").getString("MANAGE_JOURNALS"));
		journalManagement.addActionListener(this);
		journalManagement.setEnabled(false);
		journalManagement.setActionCommand(MANAGE);
		details = new JButton(getBundle("Accounting").getString("VIEW_JOURNAL_DETAILS"));
		details.addActionListener(this);
		details.setEnabled(false);
		details.setActionCommand(DETAILS);

		paneel.add(journalManagement);
		paneel.add(details);
		add(paneel);
	}

	public void addSetJournalListener(SetJournalListener dataChangeListener){
		setJournalListeners.add(dataChangeListener);
	}

	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		if(journals!=null) {
			if (MANAGE.equals(actionCommand)) {
				GUIActions.showJournalManager(journals, journalTypes);
			} else if (DETAILS.equals(actionCommand)) {
				GUIActions.showDetails(journals.getCurrentObject(), journals);
			}
		}
	}

	@Override
	public void setAccounting(Accounting accounting) {
		journals = accounting.getJournals();
		journalTypes = accounting.getJournalTypes();
		accounts = accounting.getAccounts();
	}

	public void refresh() {
        combo.removeActionListener(this);
        combo.removeAllItems();
		if (journals!=null) {
            for(Journal journal: journals.getBusinessObjects()){
                combo.addItem(journal);
            }
			combo.setSelectedItem(journals.getCurrentObject());
            details.setEnabled(journals!=null && journals.getCurrentObject()!=null);
            combo.setEnabled(journals!=null);
            journalManagement.setEnabled(journals != null);
		} else {
			combo.setSelectedItem(null);
            details.setEnabled(false);
            combo.setEnabled(false);
            journalManagement.setEnabled(false);
		}
        combo.addActionListener(this);
	}

	public void setJournal(Journal journal) {
		selectedJournal = journal;
	}
}