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
	private JList<AccountType> debit, credit, types;
	private JButton addLeft, addRight, removeLeft, removeRight;
	private ArrayList<AccountType> debitTypes, creditTypes, allTypes;
	private AlphabeticListModel<AccountType> debitModel, creditModel, typesModel;

	public JournalTypeManagementGUI(AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"));
		setContentPane(createContentPanel());
		setAccountTypes(accountTypes);
		pack();
	}

	public void setAccountTypes(AccountTypes accountTypes) {
		debitTypes = new ArrayList<>();
		creditTypes = new ArrayList<>();
		allTypes = new ArrayList<>();

		allTypes.clear();
		allTypes.addAll(accountTypes.getBusinessObjects());
		creditModel.removeAllElements();
		debitModel.removeAllElements();
		typesModel.removeAllElements();
		for(AccountType type : allTypes) {
			typesModel.addElement(type);
		}
	}

	public JPanel createSouthPanel(){
		addLeft = new JButton(getBundle("Accounting").getString("ADD_TYPE_TO_DEBITS"));
		addRight = new JButton(getBundle("Accounting").getString("ADD_TYPE_TO_CREDITS"));
		removeLeft = new JButton(getBundle("Accounting").getString("REMOVE_TYPE_FROM_DEBITS"));
		removeRight = new JButton(getBundle("Accounting").getString("REMOVE_TYPE_FROM_CREDITS"));
		addLeft.addActionListener(e -> addLeft());
		addRight.addActionListener(e -> addRight());
		removeLeft.addActionListener(e -> removeLeft());
		removeRight.addActionListener(e -> removeRight());

		JPanel south = new JPanel(new GridLayout(0, 2));

		south.add(addLeft);
		south.add(addRight);
		south.add(removeLeft);
		south.add(removeRight);

		return south;
	}

	public JPanel createContentPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createCenterPanel(), BorderLayout.CENTER);
		panel.add(createSouthPanel(), BorderLayout.SOUTH);

//		JPanel north = new JPanel();
		// TODO: add components to enter the name and save
		// + show existing types and check for doubles

		return panel;
	}

	public JPanel createCenterPanel(){
		debitModel = new AlphabeticListModel<>();
		debit = new JList<>(debitModel);
		creditModel = new AlphabeticListModel<>();
		credit = new JList<>(creditModel);
		typesModel = new AlphabeticListModel<>();
		types = new JList<>(typesModel);

		JPanel east = new JPanel();
		east.setLayout(new BorderLayout());
		east.add(new JLabel(getBundle("Accounting").getString("CREDIT_TYPES")), BorderLayout.NORTH);
		east.add(new JScrollPane(credit), BorderLayout.CENTER);

		JPanel west = new JPanel();
		west.setLayout(new BorderLayout());
		west.add(new JLabel(getBundle("Accounting").getString("DEBIT_TYPES")), BorderLayout.NORTH);
		west.add(new JScrollPane(debit), BorderLayout.CENTER);

		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		center.add(new JLabel(getBundle("Accounting").getString("ALL_TYPES")), BorderLayout.NORTH);
		center.add(new JScrollPane(types), BorderLayout.CENTER);

		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(1, 0));
		middle.add(west);
		middle.add(center);
		middle.add(east);

		return middle;
	}

	public void addLeft() {
		int rows[] = types.getSelectedIndices();
		if (rows.length != 0) {
			for(int i : rows) {
				AccountType type = allTypes.get(i);
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
				AccountType type = allTypes.get(i);
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

}