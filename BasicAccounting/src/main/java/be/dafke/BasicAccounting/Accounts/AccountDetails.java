package be.dafke.BasicAccounting.Accounts;

/**
 *
 * @author David Danneels
 */

import be.dafke.BasicAccounting.DetailsPopupMenu;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.util.ResourceBundle.getBundle;

public class AccountDetails extends RefreshableFrame implements WindowListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final DetailsPopupMenu popup;
	private RefreshableTable<Booking> tabel;
	private AccountDetailsDataModel dataModel;

	public AccountDetails(Account account, Journals journals) {
		super(getBundle("Accounting").getString("ACCOUNT_DETAILS")+ " " + account.getName());
		dataModel = new AccountDetailsDataModel(account);

		tabel = new RefreshableTable<>(dataModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);

		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(contentPanel);
		pack();

		popup = new DetailsPopupMenu(journals, tabel, DetailsPopupMenu.Mode.ACCOUNT);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel, 0,2,3));
	}

	public void refresh() {
//		tabel.refresh();
		dataModel.fireTableDataChanged();
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
}