package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Balance;
import be.dafke.BusinessModel.Journals;

import javax.swing.*;
import java.util.HashMap;

public class BalanceGUI extends JFrame {
	private static HashMap<Balance,BalanceGUI> otherBalanceMap = new HashMap<>();
	private final BalancePanel balancePanel;

	private BalanceGUI(Journals journals, Balance balance, JournalEditPanel journalEditPanel) {
		super(balance.getName());
		balancePanel = new BalancePanel(journals, balance, journalEditPanel);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(balancePanel);
		pack();

	}

	public static BalanceGUI getBalance(Journals journals, Balance balance, JournalEditPanel journalEditPanel) {
		BalanceGUI balanceGUI = otherBalanceMap.get(balance);
		if(balanceGUI==null){
			balanceGUI = new BalanceGUI(journals, balance, journalEditPanel);
			otherBalanceMap.put(balance,balanceGUI);
			Main.addFrame(balanceGUI);
		}
		balanceGUI.setVisible(true);
		return balanceGUI;
	}

	public static void fireAccountDataChangedForAll(){
		for (BalanceGUI testBalance:otherBalanceMap.values()) {
			testBalance.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		balancePanel.fireAccountDataChanged();
	}
}
