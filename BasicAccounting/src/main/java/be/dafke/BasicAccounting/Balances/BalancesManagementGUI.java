package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.AlphabeticListModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.awt.BorderLayout.*;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JSplitPane.*;

public class BalancesManagementGUI extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Accounts accounts;
	private JList<AccountType> debit, credit, types;
	private ArrayList<AccountType> allTypes;
	private AlphabeticListModel<AccountType> debitModel, creditModel, typesModel;
	private JTextField leftName, rightName, leftTotalName, rightTotalName, leftResultName, rightResultName;
	private JComboBox<Balance> combo;
	private static final HashMap<Balances, BalancesManagementGUI> balancesManagementGuis = new HashMap<>();
	private Balance balance;
	private Balances balances;

	private BalancesManagementGUI(Balances balances, Accounts accounts, AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("BALANCE_MANAGEMENT_TITLE"));
		setContentPane(createContentPanel());
		this.accounts = accounts;
		setAccountTypes(accountTypes);
		setBalances(balances);
		pack();
	}

	public static void showBalancesManager(Balances balances, Accounts accounts, AccountTypes accountTypes) {
		BalancesManagementGUI gui = balancesManagementGuis.get(balances);
		if(gui == null){
			gui = new BalancesManagementGUI(balances, accounts, accountTypes);
			balancesManagementGuis.put(balances,gui);
			SaveAllActionListener.addFrame(gui);
		}
		gui.setVisible(true);
	}

	public void setAccountTypes(AccountTypes accountTypes) {
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

	public void setBalances(Balances balances){
		this.balances = balances;
		combo.removeAllItems();
		for(Balance balance : balances.getBusinessObjects()){
			((DefaultComboBoxModel<Balance>) combo.getModel()).addElement(balance);
		}
	}

	public JPanel createContentPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createSavePanel());
		panel.add(createNameFieldPanel());
		panel.add(createCenterPanel());
		return panel;
	}

	public JPanel createNameFieldPanel(){
		leftName = new JTextField(20);
		rightName = new JTextField(20);
		leftTotalName = new JTextField(20);
		rightTotalName = new JTextField(20);
		leftResultName = new JTextField(20);
		rightResultName = new JTextField(20);

		leftName.addActionListener(e -> balance.setLeftName(leftName.getText().trim()));
		rightName.addActionListener(e -> balance.setRightName(rightName.getText().trim()));
		leftTotalName.addActionListener(e -> balance.setLeftTotalName(leftTotalName.getText().trim()));
		rightTotalName.addActionListener(e -> balance.setRightTotalName(rightTotalName.getText().trim()));
		leftResultName.addActionListener(e -> balance.setLeftResultName(leftResultName.getText().trim()));
		rightResultName.addActionListener(e -> balance.setRightResultName(rightResultName.getText().trim()));

		JPanel panel = new JPanel(new GridLayout(0,2));
		panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Names"));
		panel.add(new JLabel("LeftName:"));
		panel.add(leftName);
		panel.add(new JLabel("RightName:"));
		panel.add(rightName);
		panel.add(new JLabel("LeftTotalName:"));
		panel.add(leftTotalName);
		panel.add(new JLabel("RightTotalName:"));
		panel.add(rightTotalName);
		panel.add(new JLabel("LeftResultName:"));
		panel.add(leftResultName);
		panel.add(new JLabel("RightResultName:"));
		panel.add(rightResultName);
		return panel;
	}

	private JPanel createSavePanel() {
		JPanel panel = new JPanel();
		JButton newType = new JButton(getBundle("Accounting").getString("NEW_BALANCE"));
		newType.addActionListener(e -> createNewBalance());
		combo = new JComboBox<>();
		combo.addActionListener(e -> comboAction());
		panel.add(combo);
		panel.add(newType);
		return panel;
	}

	private void comboAction() {
		balance = (Balance) combo.getSelectedItem();
		debitModel.removeAllElements();
		creditModel.removeAllElements();
		if(balance!=null){
			for (AccountType type : balance.getLeftTypes()) {
				debitModel.addElement(type);
			}
			for (AccountType type : balance.getRightTypes()) {
				creditModel.addElement(type);
			}
		}
		leftName.setText(balance==null?"":balance.getLeftName());
		rightName.setText(balance==null?"":balance.getRightName());
		leftTotalName.setText(balance==null?"":balance.getLeftTotalName());
		rightTotalName.setText(balance==null?"":balance.getRightTotalName());
		leftResultName.setText(balance==null?"":balance.getLeftResultName());
		rightResultName.setText(balance==null?"":balance.getRightResultName());
	}

	public void createNewBalance(){
		String name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
		while (name != null && name.equals(""))
			name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
		if (name != null) {
			Balance balance = new Balance(accounts);
			balance.setName(name);
			try {
				balances.addBusinessObject(balance);
				Main.fireBalancesChanged();
			} catch (EmptyNameException e) {
				e.printStackTrace();
			} catch (DuplicateNameException e) {
				e.printStackTrace();
			}
			((DefaultComboBoxModel<Balance>) combo.getModel()).addElement(balance);
			(combo.getModel()).setSelectedItem(balance);
		}
	}

	public JSplitPane createCenterPanel(){
		JPanel allTypesPanel = createAccountTypesPanel();
		JPanel debetTypesPanel = createDebitTypesPanel();
		JPanel creditTypesPanel = createCreditTypesPanel();

		JSplitPane selectedTypesPanel = new JSplitPane(VERTICAL_SPLIT);
		selectedTypesPanel.add(debetTypesPanel, TOP);
		selectedTypesPanel.add(creditTypesPanel, BOTTOM);

		JSplitPane middle = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		middle.add(allTypesPanel, LEFT);
		middle.add(selectedTypesPanel, RIGHT);

		middle.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("ACCOUNTTYPES")));;

		return middle;
	}

	private JPanel createCreditTypesPanel() {
		JButton removeCreditType = new JButton(getBundle("Accounting").getString("DELETE"));
		removeCreditType.addActionListener(e -> removeCredit());

		creditModel = new AlphabeticListModel<>();
		credit = new JList<>(creditModel);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(getBundle("Accounting").getString("CREDIT_TYPES")), NORTH);
		panel.add(new JScrollPane(credit), CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,Y_AXIS));
		buttonPanel.add(removeCreditType);
		panel.add(buttonPanel, SOUTH);
		return panel;
	}

	private JPanel createDebitTypesPanel() {
		JButton removeDebitType = new JButton(getBundle("Accounting").getString("DELETE"));
		removeDebitType.addActionListener(e -> removeDebit());

		debitModel = new AlphabeticListModel<>();
		debit = new JList<>(debitModel);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(getBundle("Accounting").getString("DEBIT_TYPES")), NORTH);
		panel.add(new JScrollPane(debit), CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,Y_AXIS));
		buttonPanel.add(removeDebitType);
		panel.add(buttonPanel, SOUTH);
		return panel;
	}

	private JPanel createAccountTypesPanel() {
		JButton addDebitType = new JButton(getBundle("Accounting").getString("ADD_TO_DEBIT_TYPES"));
		JButton addCreditType = new JButton(getBundle("Accounting").getString("ADD_TO_CREDIT_TYPES"));
		addDebitType.addActionListener(e -> addDebit());
		addCreditType.addActionListener(e -> addCredit());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,Y_AXIS));
		buttonPanel.add(addDebitType);
		buttonPanel.add(addCreditType);

		typesModel = new AlphabeticListModel<>();
		types = new JList<>(typesModel);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(getBundle("Accounting").getString("ACCOUNT_TYPES")), NORTH);
		panel.add(new JScrollPane(types), CENTER);
		panel.add(buttonPanel, SOUTH);
		return panel;
	}

	public void addDebit() {
		for (AccountType type:types.getSelectedValuesList()) {
			balance.addLeftType(type);
			debitModel.addElement(type);
		}
	}
	public void addCredit() {
		for (AccountType type:types.getSelectedValuesList()) {
			balance.addRightType(type);
			creditModel.addElement(type);
		}
	}
	public void removeDebit() {
		for(AccountType type : debit.getSelectedValuesList()) {
			balance.removeLeftType(type);
			debitModel.removeElement(type);
		}
	}

	public void removeCredit() {
		for(AccountType type : credit.getSelectedValuesList()) {
			balance.removeRightType(type);
			creditModel.removeElement(type);
		}
	}
}