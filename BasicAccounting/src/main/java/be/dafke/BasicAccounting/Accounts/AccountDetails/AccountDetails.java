package be.dafke.BasicAccounting.Accounts.AccountDetails;

/**
 *
 * @author David Danneels
 */

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class AccountDetails extends JFrame implements WindowListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final AccountDetailsPopupMenu popup;
	private SelectableTable<Booking> tabel;
	private AccountDetailsDataModel accountDetailsDataModel;
	private static HashMap<Account,AccountDetails> accountDetailsMap = new HashMap<>();

	private AccountDetails(Account account, Journals journals, JournalInputGUI journalInputGUI) {
		super(getBundle("Accounting").getString("ACCOUNT_DETAILS") + account.getName());
		accountDetailsDataModel = new AccountDetailsDataModel(account);

		tabel = new SelectableTable<>(accountDetailsDataModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);

		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(contentPanel);
		pack();

		popup = new AccountDetailsPopupMenu(journals, tabel, journalInputGUI);
		tabel.addMouseListener(PopupForTableActivator.getInstance(popup,tabel));
	}

	public static AccountDetails getAccountDetails(Account account, Journals journals, JournalInputGUI journalInputGUI){
		AccountDetails accountDetails = accountDetailsMap.get(account);
		if(accountDetails==null){
			accountDetails = new AccountDetails(account, journals, journalInputGUI);
			accountDetailsMap.put(account, accountDetails);
			Main.addFrame(accountDetails);
		}
		accountDetails.setVisible(true);
		return accountDetails;
	}

	public void selectObject(Booking object){
		int row = accountDetailsDataModel.getRow(object);
		if(tabel!=null){
			tabel.setRowSelectionInterval(row, row);
			Rectangle cellRect = tabel.getCellRect(row, 0, false);
			tabel.scrollRectToVisible(cellRect);
		}
	}

	public void windowClosing(WindowEvent we) {
		popup.setVisible(false);
	}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	public static void fireAccountDataChangedForAll(Account account) {
		AccountDetails accountDetails = accountDetailsMap.get(account);
		if(accountDetails!=null) {
			accountDetails.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		accountDetailsDataModel.fireTableDataChanged();
	}
}