package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Accounting;
import be.dafke.Accounting.BusinessModel.Balance;

import javax.swing.*;
import java.util.HashMap;

public class BalanceGUI extends JFrame {
	private static HashMap<Balance,BalanceGUI> otherBalanceMap = new HashMap<>();
	private final BalancePanel balancePanel;

	private BalanceGUI(Accounting accounting, Balance balance) {
		super(balance.getName());
		balancePanel = new BalancePanel(accounting, balance);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(balancePanel);
		pack();

	}

	public static BalanceGUI getBalance(Accounting accounting, Balance balance) {
		BalanceGUI balanceGUI = otherBalanceMap.get(balance);
		if(balanceGUI==null){
			balanceGUI = new BalanceGUI(accounting, balance);
			otherBalanceMap.put(balance,balanceGUI);
			Main.addFrame(balanceGUI);
		}
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
