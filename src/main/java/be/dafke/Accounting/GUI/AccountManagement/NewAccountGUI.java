package be.dafke.Accounting.GUI.AccountManagement;

import be.dafke.Accounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.AccountAlreadyHasBookings;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.RefreshableFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NewAccountGUI extends RefreshableFrame implements ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField name;
	private final JComboBox type;
	private final JButton add, delete, modifyName, modifyType;
	private final NewAccountDataModel model;
	private final JTable tabel;
	private final DefaultListSelectionModel selection;
	private final Accountings accountings;

	public NewAccountGUI(Accountings accountings) {
		super("Create and modify accounts");
		this.accountings = accountings;
		this.model = new NewAccountDataModel(accountings);
		tabel = new JTable(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JPanel line1 = new JPanel();
		line1.add(new JLabel("Name:"));
		name = new JTextField(20);
		name.addActionListener(this);
		line1.add(name);
		JPanel line2 = new JPanel();
		line2.add(new JLabel("Type:"));
		type = new JComboBox(AccountType.values());
		line2.add(type);
		add = new JButton("Create new Account");
		add.addActionListener(this);
		line2.add(add);
		north.add(line1);
		north.add(line2);
		panel.add(north, BorderLayout.NORTH);
		JPanel south = new JPanel();
		modifyName = new JButton("Modify name");
		modifyType = new JButton("Modify type");
		delete = new JButton("Delete account");
		modifyName.addActionListener(this);
		modifyType.addActionListener(this);
		delete.addActionListener(this);
		modifyName.setEnabled(false);
		modifyType.setEnabled(false);
		delete.setEnabled(false);
		south.add(modifyName);
		south.add(modifyType);
		south.add(delete);
		panel.add(south, BorderLayout.SOUTH);
		setContentPane(panel);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == add || event.getSource() == name) {
			addAccount();
		} else if (event.getSource() == modifyName) {
            ArrayList<Account> accountList = getSelectedAccounts();
            if(accountList!=null){
			    modifyNames(accountList);
            }
		} else if (event.getSource() == modifyType) {
            ArrayList<Account> accountList = getSelectedAccounts();
            if(accountList!=null){
                modifyTypes(accountList);
            }
        } else if (event.getSource() == delete) {
            ArrayList<Account> accountList = getSelectedAccounts();
            if(accountList!=null){
                deleteAccounts(accountList);
            }
		}
        AccountingMenuBar.refreshAllFrames();
	}

    private void deleteAccounts(ArrayList<Account> accountList) {
		ArrayList<String> failed = new ArrayList<String>();
        for(Account account : accountList) {
            try{
                accountings.getCurrentAccounting().getAccounts().removeAccount(account);
            }catch (AccountAlreadyHasBookings e){
                failed.add(account.getName());
            }
        }
        if (failed.size() > 0) {
            if (failed.size() == 1) {
                JOptionPane.showMessageDialog(this, failed.get(0) + " already has bookings, so it can not be deleted.");
            } else {
                StringBuilder builder = new StringBuilder("The following accounts already have bookings, so they can not be deleted:\r\n");
                for(String s : failed){
                    builder.append("- "+s+"\r\n");
                }
                JOptionPane.showMessageDialog(this, builder.toString());
            }
        }
	}

	private void modifyNames(ArrayList<Account> accountList) {
		Accounting accounting = accountings.getCurrentAccounting();
        for(Account account : accountList){
            String oldName = account.getName();
            String newName = JOptionPane.showInputDialog("New name", oldName);
            while(newName!=null && !newName.trim().equals(oldName) && (accounting.getAccounts().containsKey(newName.trim()) || "".equals(newName.trim()))){
                if("".equals(newName)){
                    newName = JOptionPane.showInputDialog("The name cannot be empty. Please provide another name", oldName);
                }else{
                    newName = JOptionPane.showInputDialog(accounting.toString() + " already contains an account with the name "+ newName +
                            ". Please provide another name", oldName);
                }
            }
            if(newName!=null && !newName.trim().equals(oldName)){
                accounting.getAccounts().rename(oldName, newName.trim());
            }
		}
	}

	private void modifyTypes(ArrayList<Account> accountList) {
        boolean singleMove;
        if (accountList.size() == 1) {
            singleMove = true;
        } else {
            int option = JOptionPane.showConfirmDialog(this, "Apply same type for all selected accounts?", "All",
                    JOptionPane.YES_NO_OPTION);
            singleMove = (option == JOptionPane.YES_OPTION);
        }
        if (singleMove) {
            AccountType[] types = AccountType.values();
            int nr = JOptionPane.showOptionDialog(this, "Choose new type", "Change type",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
            for(Account account : accountList) {
                account.setType(types[nr]);
            }
        } else {
            for(Account account : accountList) {
                AccountType[] types = AccountType.values();
                int nr = JOptionPane.showOptionDialog(this, "Choose new type for " + account.getName(),
                        "Change type", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
                        account.getType());
                account.setType(types[nr]);
            }
        }
	}

	private void addAccount() {
		Accounting accounting = accountings.getCurrentAccounting();
		String newName = name.getText().trim();
		if (newName.equals("")) {
			JOptionPane.showMessageDialog(this, "Account name cannot be empty");
		} else {
			if (accounting.getAccounts().containsKey(newName)) {
				JOptionPane.showMessageDialog(this, "Account name already exists");
			} else {
				Account account = new Account(newName, (AccountType) type.getSelectedItem());
				account.setAccounting(accounting);
				accounting.getAccounts().add(account);
			}
		}
		name.setText("");
	}

	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */

	@Override
	public void refresh() {
		model.fireTableDataChanged();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int[] rows = tabel.getSelectedRows();
			if (rows.length != 0) {
				delete.setEnabled(true);
				modifyName.setEnabled(true);
				modifyType.setEnabled(true);
			} else {
                delete.setEnabled(false);
                modifyName.setEnabled(false);
                modifyType.setEnabled(false);
            }
		}
	}

    private ArrayList<Account> getSelectedAccounts() {
        int[] rows = tabel.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select an account first");
            return null;
        }
        ArrayList<Account> accountList = new ArrayList<Account>();
        for(int row : rows) {
            Account account = (Account) model.getValueAt(row, 0);
            accountList.add(account);
        }
        return accountList;
    }
}
