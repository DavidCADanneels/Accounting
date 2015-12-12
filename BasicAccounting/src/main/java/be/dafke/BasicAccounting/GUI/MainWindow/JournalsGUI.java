package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.JournalActions;
import be.dafke.BasicAccounting.GUI.AccountingPanel;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.JournalTypes;
import be.dafke.BasicAccounting.Objects.Journals;

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
    private Journals journals;
    private JournalTypes journalTypes;
    private AccountTypes accountTypes;

	public JournalsGUI(final Journals journals, final JournalTypes journalTypes, final AccountTypes accountTypes) {
		setAccounting(journals,journalTypes, accountTypes);
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<Journal>();
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
		if(MANAGE.equals(actionCommand)){
			JournalActions.showJournalManager(journals,journalTypes,accountTypes);
		} else if (DETAILS.equals(actionCommand)){
			JournalActions.showDetails(journals.getCurrentObject(), journals);
		} else if (SWITCH.equals(actionCommand)){
			Journal newJournal = (Journal)combo.getSelectedItem();
			JournalActions.switchJournal(journals, newJournal);
		}
	}

    public void setAccounting(Accounting accounting){
		if(accounting==null){
			setAccounting(null,null,null);
		} else {
			setAccounting(accounting.getJournals(),accounting.getJournalTypes(), accounting.getAccountTypes());
		}
	}
    public void setAccounting(Journals journals, JournalTypes journalTypes, AccountTypes accountTypes){
        this.journals = journals;
		this.journalTypes = journalTypes;
		this.accountTypes = accountTypes;
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
}