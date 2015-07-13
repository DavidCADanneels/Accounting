package be.dafke.BasicAccounting.GUI.Details;

/**
 *
 * @author David Danneels
 */

import be.dafke.BasicAccounting.Actions.DetailsPopupMenu;
import be.dafke.BasicAccounting.Actions.PopupActivator;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.util.ResourceBundle.getBundle;

public class AccountDetails extends RefreshableTableFrame<Booking> implements WindowListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;

	public AccountDetails(Account account, Accounting accounting) {
		super(accounting.toString() + "/" +
                getBundle("Accounting").getString("ACCOUNT_DETAILS") + "/"
                + account.getName(), new AccountDetailsDataModel(account));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		popup = new DetailsPopupMenu(accounting, tabel, DetailsPopupMenu.Mode.ACCOUNT);
		tabel.addMouseListener(new PopupActivator(popup,tabel, 0,2,3));
	}

	@Override
	public void windowClosing(WindowEvent we) {
		popup.setVisible(false);
	}

	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
}