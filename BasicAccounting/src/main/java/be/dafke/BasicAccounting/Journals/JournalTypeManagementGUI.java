package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.AccountsList;
import be.dafke.BusinessModel.JournalType;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
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