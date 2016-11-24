package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.Utils.AlphabeticListModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.WEST;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JSplitPane.*;

public class JournalTypeManagementGUI extends RefreshableFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<AccountType> debit, credit, types;
	private JButton addDebitType, addCreditType, removeDebitType, removeCreditType;
	private ArrayList<AccountType> debitTypes, creditTypes, allTypes;
	private AlphabeticListModel<AccountType> debitModel, creditModel, typesModel;

	public JournalTypeManagementGUI(AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"));
		setContentPane(createCenterPanel());
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

	public JSplitPane createCenterPanel(){
		addDebitType = new JButton(getBundle("Accounting").getString("ADD"));
		addCreditType = new JButton(getBundle("Accounting").getString("ADD"));
		removeDebitType = new JButton(getBundle("Accounting").getString("DELETE"));
		removeCreditType = new JButton(getBundle("Accounting").getString("DELETE"));
		addDebitType.addActionListener(e -> addLeft());
		addCreditType.addActionListener(e -> addRight());
		removeDebitType.addActionListener(e -> removeLeft());
		removeCreditType.addActionListener(e -> removeRight());

		debitModel = new AlphabeticListModel<>();
		debit = new JList<>(debitModel);
		JPanel debetTypesPanel = new JPanel();
		debetTypesPanel.setLayout(new BorderLayout());
		debetTypesPanel.add(new JLabel(getBundle("Accounting").getString("DEBIT_TYPES")), NORTH);
		debetTypesPanel.add(new JScrollPane(debit), CENTER);
		JPanel debetButtonPanel = new JPanel();
		debetButtonPanel.setLayout(new BoxLayout(debetButtonPanel,Y_AXIS));
		debetButtonPanel.add(addDebitType);
		debetButtonPanel.add(removeDebitType);
		debetTypesPanel.add(debetButtonPanel, WEST);

		creditModel = new AlphabeticListModel<>();
		credit = new JList<>(creditModel);
		JPanel creditTypesPanel = new JPanel();
		creditTypesPanel.setLayout(new BorderLayout());
		creditTypesPanel.add(new JLabel(getBundle("Accounting").getString("CREDIT_TYPES")), NORTH);
		creditTypesPanel.add(new JScrollPane(credit), CENTER);
		JPanel creditButtonPanel = new JPanel();
		creditButtonPanel.setLayout(new BoxLayout(creditButtonPanel,Y_AXIS));
		creditButtonPanel.add(addCreditType);
		creditButtonPanel.add(removeCreditType);
		creditTypesPanel.add(creditButtonPanel, WEST);

		typesModel = new AlphabeticListModel<>();
		types = new JList<>(typesModel);
		JPanel allTypesPanel = new JPanel();
		allTypesPanel.setLayout(new BorderLayout());
		allTypesPanel.add(new JLabel(getBundle("Accounting").getString("ALL_TYPES")), NORTH);
		allTypesPanel.add(new JScrollPane(types), CENTER);

		JSplitPane selectedTypesPanel = new JSplitPane(VERTICAL_SPLIT);
		selectedTypesPanel.add(debetTypesPanel, TOP);
		selectedTypesPanel.add(creditTypesPanel, BOTTOM);

		JSplitPane middle = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		middle.add(allTypesPanel, LEFT);
		middle.add(selectedTypesPanel, RIGHT);

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