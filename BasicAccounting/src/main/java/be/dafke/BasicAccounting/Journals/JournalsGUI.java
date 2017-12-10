package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static be.dafke.BasicAccounting.Journals.JournalManagementGUI.showJournalManager;
import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class JournalsGUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Journal> combo;
	private final JButton journalManagement, details;
	private Journals journals;
	private JournalTypes journalTypes;
	private Accounts accounts;
	private Accounting accounting;
	private AccountTypes accountTypes;
	private JournalInputGUI journalInputGUI;
	private JournalGUI journalGUI;

	public JournalsGUI(JournalGUI journalGUI, JournalInputGUI journalInputGUI){//Accounts accounts,  ) {
		this.journalInputGUI=journalInputGUI;
		this.journalGUI = journalGUI;
//		this.accounts = accounts;
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<>();
        combo.addActionListener(e -> {
			Journal newJournal = (Journal) combo.getSelectedItem();
			Journal journal = journalInputGUI.switchJournal(newJournal);
			Main.setJournal(journal);
		});
		combo.setEnabled(false);
		add(combo);

		JPanel paneel = new JPanel();

		journalManagement = new JButton(getBundle("Accounting").getString("MANAGE_JOURNALS"));
		journalManagement.addActionListener(e -> {
			if(journals!=null) {
				showJournalManager(accounts, journals, journalTypes, accountTypes).setVisible(true);
			}
		});
		journalManagement.setEnabled(false);
		details = new JButton(getBundle("Accounting").getString("VIEW_JOURNAL_DETAILS"));
		details.addActionListener(e -> {
			if(journals!=null) {
				JournalDetails.getJournalDetails(accounting.getActiveJournal(), journals, journalInputGUI);
			}
		});

		details.setEnabled(false);

		paneel.add(journalManagement);
		paneel.add(details);
		add(paneel);
	}

	public void actionPerformed(ActionEvent ae) {

	}

	public void setAccounting(Accounting accounting) {
		this.accounting = accounting;
		setAccounts(accounting==null?null:accounting.getAccounts());
		setJournalTypes(accounting==null?null:accounting.getJournalTypes());
		setAccountTypes(accounting==null?null:accounting.getAccountTypes());
		setJournals(accounting==null?null:accounting.getJournals());
		setJournal(accounting==null?null:accounting.getActiveJournal());
	}

	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}

	public void setJournalTypes(JournalTypes journalTypes) {
		this.journalTypes = journalTypes;
	}

	public void setAccountTypes(AccountTypes accountTypes) {
		this.accountTypes = accountTypes;
	}

	public void setJournals(Journals journals){
		this.journals = journals;

		combo.removeActionListener(this);
		combo.removeAllItems();
		if (journals!=null) {
			for (Journal journal : journals.getBusinessObjects()) {
				combo.addItem(journal);
			}
		}
		combo.addActionListener(this);
		combo.setEnabled(journals!=null);
		journalManagement.setEnabled(journals != null);
	}

	public void addJournal(Journal journal){
		int itemCount = combo.getItemCount();
		combo.addItem(journal);
		if(itemCount==0){
			setJournal(journal);
		}
	}

	public void setJournal(Journal journal) {
		combo.removeActionListener(this);
		combo.setSelectedItem(journal);
		combo.addActionListener(this);

		journalGUI.setJournal(journal);
		journalInputGUI.setJournal(journal);

		details.setEnabled(journal!=null);
	}
}