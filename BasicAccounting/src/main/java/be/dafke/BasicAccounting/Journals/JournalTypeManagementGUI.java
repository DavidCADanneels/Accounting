package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.Utils.AlphabeticListModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

public class JournalTypeManagementGUI extends RefreshableFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList<AccountType> debit, credit, types;
	private final JButton addLeft, addRight, removeLeft, removeRight;
	private final ArrayList<AccountType> debitTypes, creditTypes, allTypes;
	private final AlphabeticListModel<AccountType> debitModel, creditModel, typesModel;
	private final AccountTypes accountTypes;

	public JournalTypeManagementGUI(AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"));
		this.accountTypes = accountTypes;
		debitTypes = new ArrayList<>();
		creditTypes = new ArrayList<>();
		allTypes = new ArrayList<>();
		allTypes.addAll(accountTypes.getBusinessObjects());
		debitModel = new AlphabeticListModel<>();
		debit = new JList<>(debitModel);
		creditModel = new AlphabeticListModel<>();
		credit = new JList<>(creditModel);
        typesModel = new AlphabeticListModel<>();
		types = new JList<>(typesModel);
		addLeft = new JButton(getBundle("Accounting").getString("ADD_TYPE_TO_DEBITS"));
		addRight = new JButton(getBundle("Accounting").getString("ADD_TYPE_TO_CREDITS"));
		removeLeft = new JButton(getBundle("Accounting").getString("REMOVE_TYPE_FROM_DEBITS"));
		removeRight = new JButton(getBundle("Accounting").getString("REMOVE_TYPE_FROM_CREDITS"));
		addLeft.addActionListener(e -> addLeft());
		addRight.addActionListener(e -> addRight());
		removeLeft.addActionListener(e -> removeLeft());
		removeRight.addActionListener(e -> removeRight());
//		addLeft.setEnabled(false);
//		addRight.setEnabled(false);
//		removeLeft.setEnabled(false);
//		removeRight.setEnabled(false);
		JPanel east = new JPanel();
		JPanel west = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel(new GridLayout(0, 2));
		east.setLayout(new BorderLayout());
		west.setLayout(new BorderLayout());
		center.setLayout(new BorderLayout());
		west.add(new JLabel(getBundle("Accounting").getString("DEBIT_TYPES")), BorderLayout.NORTH);
		JScrollPane debitScroll = new JScrollPane(debit);
		west.add(debitScroll, BorderLayout.CENTER);
		east.add(new JLabel(getBundle("Accounting").getString("CREDIT_TYPES")), BorderLayout.NORTH);
		JScrollPane creditScroll = new JScrollPane(credit);
		east.add(creditScroll, BorderLayout.CENTER);
		center.add(new JLabel(getBundle("Accounting").getString("ALL_TYPES")), BorderLayout.NORTH);
		JScrollPane typesScroll = new JScrollPane(types);
		center.add(typesScroll, BorderLayout.CENTER);
		south.add(addLeft);
		south.add(addRight);
		south.add(removeLeft);
		south.add(removeRight);
		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(1, 0));
//		west.setSize(200, 400);
//		center.setSize(200, 400);
//		east.setSize(200, 400);
		middle.add(west);
		middle.add(center);
		middle.add(east);
		JPanel panel = new JPanel(new BorderLayout());
		// panel.add(west, BorderLayout.WEST);
		// panel.add(east, BorderLayout.EAST);
		panel.add(middle, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);

		JPanel north = new JPanel();
		// TODO: add components to enter the name and save
		// + show existing types and check for doubles

//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(panel);
		refresh();
		pack();
	}

	public void addLeft() {
		int rows[] = types.getSelectedIndices();
		if (rows.length != 0) {
			for(int i : rows) {
				AccountType type = accountTypes.getBusinessObjects().get(i);
				if (!debitTypes.contains(type)) {
					debitTypes.add(type);
					debitModel.addElement(type);
				}
			}
		}
	}
	public void addRight() {
		int rows[] = types.getSelectedIndices();
		if (rows.length != 0) {
			for (int i : rows) {
				AccountType type = accountTypes.getBusinessObjects().get(i);
				if (!creditTypes.contains(type)) {
					creditTypes.add(type);
					creditModel.addElement(type);
				}
			}
		}
	}
	public void removeLeft() {
		List<AccountType> accountTypeList = debit.getSelectedValuesList();
		for(AccountType type : accountTypeList) {
			debitTypes.remove(type);
			debitModel.removeElement(type);
		}
	}
	public void removeRight() {
		List<AccountType> accountTypeList = credit.getSelectedValuesList();
		for(AccountType type : accountTypeList) {
			creditTypes.remove(type);
			creditModel.removeElement(type);
		}
	}

	public void refresh() {
		creditModel.removeAllElements();
		for(AccountType type : creditTypes) {
			creditModel.addElement(type);
		}
		debitModel.removeAllElements();
		for(AccountType type : debitTypes) {
			debitModel.addElement(type);
		}
		typesModel.removeAllElements();
		for(AccountType type : allTypes) {
			typesModel.addElement(type);
		}
	}
}