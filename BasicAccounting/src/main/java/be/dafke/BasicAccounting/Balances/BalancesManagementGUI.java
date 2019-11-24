package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.*;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class BalancesManagementGUI extends JFrame {
	private static final HashMap<Balances, BalancesManagementGUI> balancesManagementGuis = new HashMap<>();

	private BalancesManagementGUI(Balances balances, Accounts accounts, AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("BALANCE_MANAGEMENT_TITLE"));
		setContentPane(new BalancesManagementPanel(balances, accounts, accountTypes));
		pack();
	}

	public static BalancesManagementGUI getInstance(Balances balances, Accounts accounts, AccountTypes accountTypes) {
		BalancesManagementGUI gui = balancesManagementGuis.get(balances);
		if(gui == null){
			gui = new BalancesManagementGUI(balances, accounts, accountTypes);
			balancesManagementGuis.put(balances,gui);
			Main.addFrame(gui);
		}
		return gui;
	}
}