package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessActions.JournalListener;
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
public class JournalsGUI extends JPanel implements ActionListener, JournalListener, AccountingListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Journal> combo;
	private final JButton journalManagement, details;
	private Journals journals;
	private JournalTypes journalTypes;
	private Accounts accounts;
	private AccountTypes accountTypes;
	private JournalInputGUI journalInputGUI;

	public JournalsGUI(JournalInputGUI journalInputGUI) {
		this.journalInputGUI=journalInputGUI;
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<>();
        combo.addActionListener(this);
		combo.setEnabled(false);
		add(combo);

		JPanel paneel = new JPanel();

		journalManagement = new JButton(getBundle("Accounting").getString("MANAGE_JOURNALS"));
		journalManagement.addActionListener(e -> {
			if(journals!=null) {
				showJournalManager(journals, journalTypes, accounts, accountTypes).setVisible(true);
			}
		});
		journalManagement.setEnabled(false);
		details = new JButton(getBundle("Accounting").getString("VIEW_JOURNAL_DETAILS"));
		details.addActionListener(e -> {
			if(journals!=null) {
				JournalDetails.getJournalDetails(journals.getCurrentObject(), journals, journalInputGUI);
			}
		});

		details.setEnabled(false);

		paneel.add(journalManagement);
		paneel.add(details);
		add(paneel);
	}

	public void actionPerformed(ActionEvent ae) {
		Journal newJournal = (Journal) combo.getSelectedItem();
		Journal journal = journalInputGUI.switchJournal(accounts, newJournal);
		Main.setJournal(journal);
	}

	@Override
	public void setAccounting(Accounting accounting) {
		journalTypes=accounting==null?null:accounting.getJournalTypes();
		accountTypes=accounting==null?null:accounting.getAccountTypes();
		accounts=accounting==null?null:accounting.getAccounts();
		setJournals(accounting==null?null:accounting.getJournals());
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
		setJournal(journals==null?null:journals.getCurrentObject());
	}

	@Override
	public void setJournal(Journal journal) {
		combo.removeActionListener(this);
		combo.setSelectedItem(journal);
		combo.addActionListener(this);

		details.setEnabled(journal!=null);
	}
}