package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountSelector extends RefreshableDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton create, ok;
	private Account account;
	private final JComboBox<Account> combo;
    private final DefaultComboBoxModel<Account> model;
	private final Accounts accounts;

	public AccountSelector(Accounts accounts, ActionListener actionListener) {
        super("Select Account");
        this.accounts = accounts;
        model = new DefaultComboBoxModel<Account>();
		combo = new JComboBox<Account>(model);
		combo.addActionListener(this);
		create = new JButton("Add account(s) ...");
		create.addActionListener(actionListener);
        create.setActionCommand(AccountingActionListener.NEW_ACCOUNT);
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(this);
		JPanel innerPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		panel.add(combo);
		panel.add(create);
		innerPanel.add(panel, BorderLayout.CENTER);
		innerPanel.add(ok, BorderLayout.SOUTH);
		setContentPane(innerPanel);
        refresh();
        pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == combo) {
			account = (Account) combo.getSelectedItem();
		} else if (e.getSource() == ok) {
			dispose();
		}
	}

	public Account getSelection() {
		return account;
	}

    @Override
    public void refresh() {
        model.removeAllElements();
//        Accounts accounts = accounting.getAccounts();
        for(Account account : accounts.getBusinessObjects()) {
            model.addElement(account);
        }
    }
}
