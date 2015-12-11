package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.JournalDetailsLauncher;
import be.dafke.BasicAccounting.Actions.JournalManagementLauncher;
import be.dafke.BasicAccounting.Actions.SwitchJournalActionListener;
import be.dafke.BasicAccounting.GUI.AccountingPanel;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Journals;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
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
	private JComboBox<Journal> combo;
	private final JButton journalManagement, details;
    private Journals journals;
    private SwitchJournalActionListener switchJournalActionListener;
	private Accounting accounting;
	public final String MANAGE = "manage";
	public final String DETAILS = "details";
	private final JournalDetailsLauncher journalDetailsLauncher = new JournalDetailsLauncher();
	private final JournalManagementLauncher journalManagementLauncher = new JournalManagementLauncher();

	public JournalsGUI(final Accountings accountings) {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<Journal>();
        switchJournalActionListener = new SwitchJournalActionListener(accountings, combo);
        combo.addActionListener(switchJournalActionListener);
		combo.setEnabled(false);
		add(combo);

		JPanel paneel = new JPanel();

		journalManagement = new JButton(getBundle("Accounting").getString("JOURNAL_MANAGEMENT"));
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
		if(MANAGE.equals(actionCommand)){
			journalManagementLauncher.showJournalManager(accounting);
		} else if (DETAILS.equals(actionCommand)){
			journalDetailsLauncher.showDetails(accounting, journals.getCurrentObject());
		}
	}

    public void setAccounting(Accounting accounting){
		this.accounting = accounting;
        if(accounting==null){
            journals = null;
        } else {
            this.journals = accounting.getJournals();
        }
    }

	public void refresh() {
        combo.removeActionListener(switchJournalActionListener);
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
        combo.addActionListener(switchJournalActionListener);
	}
}