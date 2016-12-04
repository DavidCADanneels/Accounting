package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class TestBalance extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;
	private RefreshableTable<Account> tabel;
	private TestBalanceDataModel testBalanceDataModel;
	private static HashMap<Accounts,TestBalance> testBalanceMap = new HashMap<>();

	private TestBalance(Journals journals, Accounts accounts, JournalInputGUI journalInputGUI) {
		super(getBundle("BusinessModel").getString("TESTBALANCE"));
		testBalanceDataModel = new TestBalanceDataModel(accounts);

		tabel = new RefreshableTable<>(testBalanceDataModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(contentPanel);
		pack();

		popup = new BalancePopupMenu(journals, tabel, journalInputGUI);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel));
	}

	public static TestBalance getTestBalance(Journals journals, Accounts accounts, JournalInputGUI journalInputGUI) {
		TestBalance testBalance = testBalanceMap.get(accounts);
		if(testBalance==null){
			testBalance = new TestBalance(journals, accounts, journalInputGUI);
			testBalanceMap.put(accounts,testBalance);
			SaveAllActionListener.addFrame(testBalance);
		}
		testBalance.setVisible(true);
		return testBalance;
	}

	public static void fireAccountDataChangedForAll(){
		for (TestBalance testBalance:testBalanceMap.values()) {
			testBalance.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		testBalanceDataModel.fireTableDataChanged();
	}
}
