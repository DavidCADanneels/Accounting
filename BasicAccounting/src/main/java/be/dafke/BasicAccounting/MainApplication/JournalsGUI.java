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
    private Journals journals;
	private Accounts accounts;
    private JournalTypes journalTypes;
    private AccountTypes accountTypes;

	public JournalsGUI(final Accounts accounts, final Journals journals, final JournalTypes journalTypes, final AccountTypes accountTypes) {
		setAccounting(accounts, journals,journalTypes, accountTypes);
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
			GUIActions.showJournalManager(journals,journalTypes, accounts,accountTypes);
		} else if (DETAILS.equals(actionCommand)){
			GUIActions.showDetails(journals.getCurrentObject(), journals);
		} else if (SWITCH.equals(actionCommand)){
			Journal newJournal = (Journal)combo.getSelectedItem();
			GUIActions.switchJournal(accounts, journals, newJournal);
		}
	}

    public void setAccounting(Accounting accounting){
		if(accounting==null){
			setAccounting(null, null,null,null);
		} else {
			setAccounting(accounting.getAccounts(), accounting.getJournals(),accounting.getJournalTypes(), accounting.getAccountTypes());
		}
	}
    public void setAccounting(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes){
        this.accounts = accounts;
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