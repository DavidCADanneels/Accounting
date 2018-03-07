package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.AlphabeticListModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;

import static java.awt.BorderLayout.*;
import static java.util.ResourceBundle.getBundle;

public class BalancesManagementGUI extends JFrame {
	private static final HashMap<Balances, BalancesManagementGUI> balancesManagementGuis = new HashMap<>();

	private BalancesManagementGUI(Balances balances, Accounts accounts, AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("BALANCE_MANAGEMENT_TITLE"));
		setContentPane(new BalancesManagementPanel(balances, accounts, accountTypes));
		pack();
	}

	public static void showBalancesManager(Balances balances, Accounts accounts, AccountTypes accountTypes) {
		BalancesManagementGUI gui = balancesManagementGuis.get(balances);
		if(gui == null){
			gui = new BalancesManagementGUI(balances, accounts, accountTypes);
			balancesManagementGuis.put(balances,gui);
			Main.addFrame(gui);
		}
		gui.setVisible(true);
	}
}