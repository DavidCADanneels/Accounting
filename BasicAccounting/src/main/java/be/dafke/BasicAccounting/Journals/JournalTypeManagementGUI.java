package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.AccountsList;
import be.dafke.BusinessModel.JournalType;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.util.ResourceBundle.getBundle;

public class JournalTypeManagementGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<JournalType> combo;
	private JournalTypes journalTypes;
	private static final HashMap<JournalTypes, JournalTypeManagementGUI> journalTypeManagementGuis = new HashMap<>();
	private JournalType journalType;
	private Accounts accounts;
	private AccountTypes accountTypes;
	private JComboBox<VATTransaction.VATType> taxType;
//	private JPanel left, right;
	private AccountsListConfigPanel accountsListConfigPanelLeft, accountsListConfigPanelRight;
//	private AccountsListConfigPanel left, right;

	private JournalTypeManagementGUI(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
		super(journalTypes.getAccounting().getName() + " / " + getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"));
		this.accounts = accounts;
		this.accountTypes = accountTypes;
		this.journalTypes = journalTypes;
		setContentPane(createContentPanel());
		setJournalTypes(journalTypes);
		pack();
//		comboAction();
	}

	public static void showJournalTypeManager(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
		JournalTypeManagementGUI gui = journalTypeManagementGuis.get(journalTypes);
		if(gui == null){
			gui = new JournalTypeManagementGUI(accounts, journalTypes, accountTypes);
			journalTypeManagementGuis.put(journalTypes,gui);
			Main.addFrame(gui);
		}
		gui.setVisible(true);
	}

	public void setJournalTypes(JournalTypes journalTypes){
		this.journalTypes = journalTypes;
		combo.removeAllItems();
		for(JournalType type : journalTypes.getBusinessObjects()){
			((DefaultComboBoxModel<JournalType>) combo.getModel()).addElement(type);
		}
	}

	public JPanel createContentPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createCenterPanel(), CENTER);
		panel.add(createSavePanel(), NORTH);
		return panel;
	}

	private JPanel createSavePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,0));
		JButton newType = new JButton(getBundle("Accounting").getString("NEW_JOURNAL_TYPE"));
		newType.addActionListener(e -> createNewJournalType());
		combo = new JComboBox<>();
		combo.addActionListener(e -> comboAction());
		taxType = new JComboBox<>();
		taxType.addItem(null);
		taxType.addItem(VATTransaction.VATType.PURCHASE);
		taxType.addItem(VATTransaction.VATType.SALE);
		taxType.setSelectedItem(null);
		taxType.addActionListener(e -> {
			VATTransaction.VATType vatType = (VATTransaction.VATType) taxType.getSelectedItem();
			journalType.setVatType(vatType);
			VATTransaction.VATType vatTypeLeft = JournalType.calculateLeftVatType(vatType);
			VATTransaction.VATType vatTypeRight = JournalType.calculateRightVatType(vatType);
			accountsListConfigPanelLeft.setVatType(vatTypeLeft);
			accountsListConfigPanelRight.setVatType(vatTypeRight);
		});

		panel.add(new JLabel("Selected JournalType:"));
		panel.add(combo);
		panel.add(newType);

		panel.add(new JLabel("Journal VATType:"));
		panel.add(taxType);

		return panel;
	}

	private void comboAction() {
		journalType = (JournalType) combo.getSelectedItem();
		taxType.setSelectedItem(journalType.getVatType());
		AccountsList leftList = journalType.getLeft();
		accountsListConfigPanelLeft.setAccountsList(leftList);
		accountsListConfigPanelLeft.setJournalType(journalType);
		AccountsList rightList = journalType.getRight();
		accountsListConfigPanelRight.setAccountsList(rightList);
		accountsListConfigPanelRight.setJournalType(journalType);
		accountsListConfigPanelLeft.refresh();
		accountsListConfigPanelRight.refresh();
	}

	public void createNewJournalType(){
		String name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
		while (name != null) {
			name = name.trim();
			JournalType journalType = new JournalType(name, accountTypes);
			try {
				journalTypes.addBusinessObject(journalType);
				JournalManagementGUI.fireJournalTypeDataChangedForAll(journalTypes);
				((DefaultComboBoxModel<JournalType>) combo.getModel()).addElement(journalType);
				(combo.getModel()).setSelectedItem(journalType);
				name = null;
			} catch (DuplicateNameException e) {
				ActionUtils.showErrorMessage(ActionUtils.JOURNAL_TYPE_DUPLICATE_NAME, name);
				name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
			} catch (EmptyNameException e) {
				ActionUtils.showErrorMessage(ActionUtils.JOURNAL_TYPE_NAME_EMPTY);
				name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
			}
		}
	}

	public JPanel createCenterPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));

//		panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("ACCOUNTTYPES")));;
		accountsListConfigPanelLeft = new AccountsListConfigPanel(accounts, accountTypes, true);
		JPanel left = new JPanel();
		left.add(accountsListConfigPanelLeft);
		left.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("LEFT")));
		panel.add(left);
		accountsListConfigPanelRight = new AccountsListConfigPanel(accounts, accountTypes, false);
		JPanel right = new JPanel();
		right.add(accountsListConfigPanelRight);
		right.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("RIGHT")));

		panel.add(right);


		return panel;
	}
}