package be.dafke.BasicAccounting.Accounts.AccountManagement;

import be.dafke.BasicAccounting.Accounts.NewAccountDialog;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class AccountManagementGUI extends JFrame implements ListSelectionListener {
	private JButton newAccount, delete, edit;
	private final AccountManagementTableModel accountManagementTableModel;
	private final SelectableTable<Account> tabel;
	private Accounts accounts;
	private ArrayList<AccountType> accountTypes;
	private static final HashMap<Accounts, AccountManagementGUI> accountManagementGuis = new HashMap<>();

	private AccountManagementGUI(final Accounts accounts, ArrayList<AccountType> accountTypes) {
		super(getBundle("Accounting").getString("ACCOUNT_MANAGEMENT_TITLE"));
		this.accounts = accounts;
		this.accountTypes = accountTypes;
		this.accountManagementTableModel = new AccountManagementTableModel(accounts);

		// COMPONENTS
		//
		// Table
		tabel = new SelectableTable<>(accountManagementTableModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		DefaultListSelectionModel selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);

		JComboBox<AccountType> comboBox=createComboBox();

		TableColumn column = tabel.getColumnModel().getColumn(AccountManagementTableModel.TYPE_COL);
		column.setCellEditor(new DefaultCellEditor(comboBox));

		JScrollPane scrollPane = new JScrollPane(tabel);
		//
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel south = createContentPanel();

		panel.add(south, BorderLayout.SOUTH);
		setContentPane(panel);
		pack();
	}

	private JComboBox<AccountType> createComboBox() {
		JComboBox<AccountType> comboBox = new JComboBox<>();
		accountTypes.forEach(accountType -> comboBox.addItem(accountType));
		return comboBox;
	}

	public static AccountManagementGUI showAccountManager(Accounts accounts, ArrayList<AccountType> accountTypes) {
		AccountManagementGUI gui = accountManagementGuis.get(accounts);
		if(gui == null){
			gui = new AccountManagementGUI(accounts, accountTypes);
			accountManagementGuis.put(accounts, gui);
			Main.addFrame(gui);
		}
		return gui;
	}

	private JPanel createContentPanel(){
		JPanel south = new JPanel();
		delete = new JButton(getBundle("Accounting").getString("DELETE_ACCOUNT"));
		edit = new JButton(getBundle("Accounting").getString("EDIT_ACCOUNT"));
        newAccount = new JButton(getBundle("Accounting").getString("ADD_ACCOUNT"));
		delete.addActionListener(e -> deleteAccounts(tabel.getSelectedObjects(), accounts));
		edit.addActionListener(e -> {
			int selectedRow = tabel.getSelectedRow();
			if(selectedRow!=-1) {
				Account account = accountManagementTableModel.getObject(selectedRow, 0);
				NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes);
				newAccountDialog.setAccount(account);
				newAccountDialog.setVisible(true);
			}
		});
		newAccount.addActionListener(e -> new NewAccountDialog(accounts, accountTypes).setVisible(true));
		delete.setEnabled(false);
		edit.setEnabled(false);
        south.add(delete);
        south.add(edit);
        south.add(newAccount);
		return south;
	}

 	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */

	public static void fireAccountDataChangedForAll() {
		for(AccountManagementGUI accountManagementGUI:accountManagementGuis.values()){
			accountManagementGUI.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		accountManagementTableModel.fireTableDataChanged();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int[] rows = tabel.getSelectedRows();
			if (rows.length != 0) {
				delete.setEnabled(true);
				edit.setEnabled(true);
			} else {
                delete.setEnabled(false);
                edit.setEnabled(false);
            }
		}
	}

	public void deleteAccounts(ArrayList<Account> accountList, Accounts accounts){
		if(!accountList.isEmpty()) {
			ArrayList<String> failed = new ArrayList<>();
			for(Account account : accountList) {
				try{
					accounts.removeBusinessObject(account);
				}catch (NotEmptyException e){
					failed.add(account.getName());
				}
			}
			if (failed.size() > 0) {
				if (failed.size() == 1) {
					ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NOT_EMPTY, failed.get(0));
				} else {
					StringBuilder builder = new StringBuilder(getBundle("BusinessActions").getString("MULTIPLE_ACCOUNTS_NOT_EMPTY")+"\n");
					for(String s : failed){
						builder.append("- ").append(s).append("\n");
					}
					JOptionPane.showMessageDialog(null, builder.toString());
				}
			}
		}
		fireAccountDataChanged();
	}
}
