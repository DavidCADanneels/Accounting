package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.GUIActions;
import be.dafke.BusinessActions.SetJournalListener;
import be.dafke.BusinessModel.Journal;

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

	public JournalsGUI() {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<>();
        combo.addActionListener(e -> {
            Journal newJournal = (Journal) combo.getSelectedItem();
            Journal journal = GUIActions.switchJournal(accounting.getAccounts(), selectedJournal, newJournal);
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
		if(accounting!=null) {
			if (MANAGE.equals(actionCommand)) {
				GUIActions.showJournalManager(accounting);
			} else if (DETAILS.equals(actionCommand)) {
				GUIActions.showDetails(accounting.getJournals().getCurrentObject(), accounting.getJournals());
			}
		}
	}

	public void refresh() {
        combo.removeActionListener(this);
        combo.removeAllItems();
		if (accounting!=null && accounting.getJournals()!=null) {
            for(Journal journal: accounting.getJournals().getBusinessObjects()){
                combo.addItem(journal);
            }
			combo.setSelectedItem(accounting.getJournals().getCurrentObject());
            details.setEnabled(accounting.getJournals()!=null && accounting.getJournals().getCurrentObject()!=null);
            combo.setEnabled(accounting.getJournals()!=null);
            journalManagement.setEnabled(accounting.getJournals() != null);
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