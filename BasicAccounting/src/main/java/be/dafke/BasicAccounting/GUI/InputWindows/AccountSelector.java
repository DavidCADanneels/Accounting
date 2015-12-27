package be.dafke.BasicAccounting.GUI.InputWindows;

import be.dafke.BasicAccounting.GUI.AccountManagement.NewAccountGUI;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.BorderLayout;
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

	public AccountSelector(final Accounts accounts, final AccountTypes accountTypes) {
        super("Select Account");
        this.accounts = accounts;
        model = new DefaultComboBoxModel<Account>();
		combo = new JComboBox<Account>(model);
		combo.addActionListener(this);
		create = new JButton("Add account(s) ...");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NewAccountGUI(accounts, accountTypes).setVisible(true);
			}
		});
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

    public void refresh() {
        model.removeAllElements();
        for(Account account : accounts.getBusinessObjects()) {
            model.addElement(account);
        }
        invalidate();
        combo.invalidate();
    }
}
