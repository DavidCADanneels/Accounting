package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.Balances.BalancesManagementPanel;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Balances;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class DeliverooGUI extends JFrame {
	private static DeliverooGUI deliverooGUI = null;

	private DeliverooGUI() {
		super(getBundle("Accounting").getString("BALANCE_MANAGEMENT_TITLE"));
		setContentPane(new DeliverooPanel());
		pack();
	}

	public static DeliverooGUI getInstance() {
		if(deliverooGUI == null){
			deliverooGUI = new DeliverooGUI();
			Main.addFrame(deliverooGUI);
		}
		return deliverooGUI;
	}
}