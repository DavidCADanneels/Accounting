package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.BalancePopupMenu;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

public class TestBalance extends RefreshableFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;
	private RefreshableTable<Account> tabel;
	private TestBalanceDataModel dataModel;

	public TestBalance(Journals journals, Accounts accounts, AccountTypes accountTypes) {
		super(getBundle("BusinessModel").getString("TESTBALANCE"));
		 dataModel = new TestBalanceDataModel(accounts, accountTypes);

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

		popup = new BalancePopupMenu(journals, tabel);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel));
	}
	public void refresh() {
//		tabel.refresh();
		dataModel.fireTableDataChanged();
	}

}
