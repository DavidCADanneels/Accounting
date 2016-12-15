package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.JournalType;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.AlphabeticListModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private ArrayList<AccountType> debitTypes, creditTypes, allTypes;
	private AlphabeticListModel<AccountType> debitModel, creditModel, typesModel;
	private JournalTypes journalTypes;
	private JTextField nameField;
	private static final HashMap<JournalTypes, JournalTypeManagementGUI> journalTypeManagementGuis = new HashMap<>();

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

	public void setJournalTypes(JournalTypes journalTypes){
		this.journalTypes = journalTypes;
	}

	public JPanel createContentPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createCenterPanel(), CENTER);
		panel.add(createSavePanel(), NORTH);
		return panel;
	}

	private JPanel createSavePanel() {
		JPanel panel = new JPanel();
		nameField = new JTextField(20);
		JButton save = new JButton(getBundle("Accounting").getString("SAVE_TYPE"));
		save.addActionListener(e -> saveType());
		panel.add(new JLabel("Name:"));
		panel.add(nameField);
		panel.add(save);
		return panel;
	}

	private void saveType() {
		String name = nameField.getText();
		JournalType journalType = new JournalType(name);
		try {
			journalTypes.addBusinessObject(journalType);
		} catch (EmptyNameException e) {
			e.printStackTrace();
		} catch (DuplicateNameException e) {
			e.printStackTrace();
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
		removeCreditType.addActionListener(e -> removeRight());

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
		removeDebitType.addActionListener(e -> removeLeft());

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
		addDebitType.addActionListener(e -> addLeft());
		addCreditType.addActionListener(e -> addRight());

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