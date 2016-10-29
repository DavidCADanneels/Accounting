package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.GUIActions;
import be.dafke.BusinessModel.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class JournalsGUI extends AccountingPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SWITCH = "SWITCH";
	public static final String MANAGE = "manage";
	public static final String DETAILS = "details";
	private JComboBox<Journal> combo;
	private final JButton journalManagement, details;

	public JournalsGUI() {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<>();
        combo.setActionCommand(SWITCH);
        combo.addActionListener(this);
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

	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		if(accounting!=null) {
			if (MANAGE.equals(actionCommand)) {
				GUIActions.showJournalManager(accounting);
			} else if (DETAILS.equals(actionCommand)) {
				GUIActions.showDetails(accounting.getJournals().getCurrentObject(), accounting.getJournals());
			} else if (SWITCH.equals(actionCommand)) {
				Journal newJournal = (Journal) combo.getSelectedItem();
				GUIActions.switchJournal(accounting.getAccounts(), accounting.getJournals(), newJournal);
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
}