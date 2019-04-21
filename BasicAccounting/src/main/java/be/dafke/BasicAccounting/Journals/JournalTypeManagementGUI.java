package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.JournalTypes;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class JournalTypeManagementGUI extends JFrame {
	private static final HashMap<JournalTypes, JournalTypeManagementGUI> journalTypeManagementGuis = new HashMap<>();
	private final JournalTypeManagementPanel journalTypeManagementPanel;

	private JournalTypeManagementGUI(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"));
		journalTypeManagementPanel = new JournalTypeManagementPanel(accounts, journalTypes, accountTypes);
		setContentPane(journalTypeManagementPanel);
		pack();
	}

	public static JournalTypeManagementGUI getInstance(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
		JournalTypeManagementGUI gui = journalTypeManagementGuis.get(journalTypes);
		if(gui == null){
			gui = new JournalTypeManagementGUI(accounts, journalTypes, accountTypes);
			journalTypeManagementGuis.put(journalTypes,gui);
			Main.addFrame(gui);
		}
		return gui;
	}


}