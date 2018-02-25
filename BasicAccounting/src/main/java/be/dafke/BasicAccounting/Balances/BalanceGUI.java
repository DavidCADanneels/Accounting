package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Balance;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class BalanceGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;
	private SelectableTable<Account> tabel;
	private BalanceDataModel balanceDataModel;
	private static HashMap<Balance,BalanceGUI> otherBalanceMap = new HashMap<>();

	private BalanceGUI(Journals journals, Balance balance, JournalInputGUI journalInputGUI) {
		super(balance.getName());
		balanceDataModel = new BalanceDataModel(balance);

		tabel = new SelectableTable<>(balanceDataModel);
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
		tabel.addMouseListener(PopupForTableActivator.getInstance(popup,tabel));
		tabel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public static BalanceGUI getBalance(Journals journals, Balance balance, JournalInputGUI journalInputGUI) {
		BalanceGUI balanceGUI = otherBalanceMap.get(balance);
		if(balanceGUI==null){
			balanceGUI = new BalanceGUI(journals, balance, journalInputGUI);
			otherBalanceMap.put(balance,balanceGUI);
			Main.addFrame(balanceGUI);
		}
		balanceGUI.setVisible(true);
		return balanceGUI;
	}

	public static void fireAccountDataChangedForAll(){
		for (BalanceGUI testBalance:otherBalanceMap.values()) {
			testBalance.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		balanceDataModel.fireTableDataChanged();
	}
}
