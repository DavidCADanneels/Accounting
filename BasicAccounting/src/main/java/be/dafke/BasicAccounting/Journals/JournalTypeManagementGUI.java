package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.AlphabeticListModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.awt.BorderLayout.*;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JSplitPane.*;

public class JournalTypeManagementGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<AccountType> debit, credit, types;
	private ArrayList<AccountType> allTypes;
	private AlphabeticListModel<AccountType> debitModel, creditModel, typesModel;
	private JComboBox<JournalType> combo;
	private JournalTypes journalTypes;
	private static final HashMap<JournalTypes, JournalTypeManagementGUI> journalTypeManagementGuis = new HashMap<>();
	private JournalType journalType;
	private JComboBox<VATTransaction.VATType> taxType;

	private JournalTypeManagementGUI(JournalTypes journalTypes, AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"));
		setContentPane(createContentPanel());
		setAccountTypes(accountTypes);
		setJournalTypes(journalTypes);
		pack();
	}

	public static void showJournalTypeManager(JournalTypes journalTypes, AccountTypes accountTypes) {
		JournalTypeManagementGUI gui = journalTypeManagementGuis.get(journalTypes);
		if(gui == null){
			gui = new JournalTypeManagementGUI(journalTypes, accountTypes);
			journalTypeManagementGuis.put(journalTypes,gui);
			SaveAllActionListener.addFrame(gui);
		}
		gui.setVisible(true);
	}

	public void setAccountTypes(AccountTypes accountTypes) {
//		debitTypes = new ArrayList<>();
//		creditTypes = new ArrayList<>();
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
		JButton newType = new JButton(getBundle("Accounting").getString("NEW_JOURNAL_TYPE"));
		newType.addActionListener(e -> createNewJournalType());
		combo = new JComboBox<>();
		combo.addActionListener(e -> comboAction());
		taxType = new JComboBox<>();
		taxType.addItem(VATTransaction.VATType.NONE);
		taxType.addItem(VATTransaction.VATType.PURCHASE);
		taxType.addItem(VATTransaction.VATType.SALE);
		taxType.setSelectedItem(VATTransaction.VATType.NONE);
		taxType.addActionListener(e -> {
			journalType.setVatType((VATTransaction.VATType) taxType.getSelectedItem());
		});
		panel.add(taxType);
		panel.add(combo);
		panel.add(newType);
		return panel;
	}

	private void comboAction() {
		journalType = (JournalType) combo.getSelectedItem();
		debitModel.removeAllElements();
		creditModel.removeAllElements();
		if(journalType!=null){
			AccountTypes debetTypes = journalType.getDebetTypes();
			if(debetTypes!=null) {
				for (AccountType type : debetTypes.getBusinessObjects()) {
					debitModel.addElement(type);
				}
			}
			AccountTypes creditTypes = journalType.getCreditTypes();
			if(creditTypes!=null) {
				for (AccountType type : creditTypes.getBusinessObjects()) {
					creditModel.addElement(type);
				}
			}
		}
		taxType.setSelectedItem(journalType.getVatType());
	}

	public void createNewJournalType(){
		String name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
		while (name != null && name.equals(""))
			name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
		if (name != null) {
			JournalType journalType = new JournalType(name);
			try {
				journalTypes.addBusinessObject(journalType);
			} catch (EmptyNameException e) {
				e.printStackTrace();
			} catch (DuplicateNameException e) {
				e.printStackTrace();
			}
			((DefaultComboBoxModel<JournalType>) combo.getModel()).addElement(journalType);
			(combo.getModel()).setSelectedItem(journalType);
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
			try {
				journalType.addDebetType(type);
				debitModel.addElement(type);
			} catch (EmptyNameException e) {
				e.printStackTrace();
			} catch (DuplicateNameException e) {
				e.printStackTrace();
			}
		}
	}
	public void addCredit() {
		for (AccountType type:types.getSelectedValuesList()) {
			try {
				journalType.addCreditType(type);
				creditModel.addElement(type);
			} catch (EmptyNameException e) {
				e.printStackTrace();
			} catch (DuplicateNameException e) {
				e.printStackTrace();
			}
		}
	}
	public void removeDebit() {
		for(AccountType type : debit.getSelectedValuesList()) {
			journalType.removeDebitType(type);
			debitModel.removeElement(type);
		}
	}

	public void removeCredit() {
		for(AccountType type : credit.getSelectedValuesList()) {
			journalType.removeCreditType(type);
			creditModel.removeElement(type);
		}
	}
}