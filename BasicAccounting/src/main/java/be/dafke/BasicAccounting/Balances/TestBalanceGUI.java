package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Journals;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class TestBalanceGUI extends JFrame {
	private static HashMap<Accounts,TestBalanceGUI> testBalanceMap = new HashMap<>();
	private final TestBalancePanel testBalancePanel;

	private TestBalanceGUI(Journals journals, Accounts accounts, JournalEditPanel journalEditPanel) {
		super(getBundle("BusinessModel").getString("TESTBALANCE"));
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testBalancePanel = new TestBalancePanel(journals, accounts, journalEditPanel);
		setContentPane(testBalancePanel);
		pack();
	}

	public static TestBalanceGUI getTestBalance(Journals journals, Accounts accounts, JournalEditPanel journalEditPanel) {
		TestBalanceGUI testBalanceGUI = testBalanceMap.get(accounts);
		if(testBalanceGUI ==null){
			testBalanceGUI = new TestBalanceGUI(journals, accounts, journalEditPanel);
			testBalanceMap.put(accounts, testBalanceGUI);
			Main.addFrame(testBalanceGUI);
		}
		testBalanceGUI.setVisible(true);
		return testBalanceGUI;
	}

	public static void fireAccountDataChangedForAll(){
		for (TestBalanceGUI testBalanceGUI :testBalanceMap.values()) {
			testBalanceGUI.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		testBalancePanel.fireAccountDataChanged();
	}
}
