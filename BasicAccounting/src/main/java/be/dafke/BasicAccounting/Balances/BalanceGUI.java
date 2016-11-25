package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessActions.AccountDataChangeListener;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Balance;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;

public class BalanceGUI extends JFrame implements AccountDataChangeListener {
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;
	private RefreshableTable<Account> tabel;
	private BalanceDataModel balanceDataModel;

	public BalanceGUI(Journals journals, Balance balance, JournalInputGUI journalInputGUI) {
		super(balance.getName());
		balanceDataModel = new BalanceDataModel(balance);

		tabel = new RefreshableTable<>(balanceDataModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(contentPanel);
		pack();

		popup = new BalancePopupMenu(journals, tabel, journalInputGUI);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel));
	}

	@Override
	public void fireAccountDataChanged() {
		balanceDataModel.fireTableDataChanged();
	}
}
