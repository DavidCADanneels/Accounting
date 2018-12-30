package be.dafke.BasicAccounting.Accounts.AccountDetails;

/**
 *
 * @author David Danneels
 */

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class AccountDetailsGUI extends JFrame implements WindowListener {
	private static HashMap<Account,AccountDetailsGUI> accountDetailsMap = new HashMap<>();
	private final AccountDetailsPanel accountDetailsPanel;

	private AccountDetailsGUI(Point location, Account account, Journals journals) {
		super(getBundle("Accounting").getString("ACCOUNT_DETAILS") + account.getName());
		accountDetailsPanel = new AccountDetailsPanel(account, journals);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(location);
		setContentPane(accountDetailsPanel);
		pack();
	}

	public static AccountDetailsGUI getAccountDetails(Point location, Account account, Journals journals){
		AccountDetailsGUI accountDetailsGUI = accountDetailsMap.get(account);
		if(accountDetailsGUI ==null){
			accountDetailsGUI = new AccountDetailsGUI(location, account, journals);
			accountDetailsMap.put(account, accountDetailsGUI);
			Main.addFrame(accountDetailsGUI);
		}
		accountDetailsGUI.setVisible(true);
		return accountDetailsGUI;
	}

	public void selectObject(Booking object){
		accountDetailsPanel.selectObject(object);
	}

	public void windowClosing(WindowEvent we) {
		accountDetailsPanel.closePopups();
	}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	public static void fireAccountDataChangedForAll(Account account) {
		AccountDetailsGUI accountDetailsGUI = accountDetailsMap.get(account);
		if(accountDetailsGUI !=null) {
			accountDetailsGUI.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		accountDetailsPanel.fireAccountDataChanged();
	}
}