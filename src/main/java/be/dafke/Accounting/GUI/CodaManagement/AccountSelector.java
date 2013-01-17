package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.RefreshableFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountSelector extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton create, ok;
	private Account account;
	private final JComboBox combo;

	private final Accountings accountings;

	public AccountSelector(Accountings accountings) {
		super();
		this.accountings = accountings;
		setTitle("Select Account");
		setModal(true);
		Accounts accounts = accountings.getCurrentAccounting().getAccounts();
		combo = new JComboBox(accounts.getAccounts().toArray());
		combo.addActionListener(this);
		create = new JButton("Manage accounts ...");
		create.addActionListener(this);
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(this);
		JPanel innerPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		panel.add(combo);
		panel.add(create);
		innerPanel.add(panel, BorderLayout.CENTER);
		innerPanel.add(ok, BorderLayout.SOUTH);
		setContentPane(innerPanel);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == combo) {
			account = (Account) combo.getSelectedItem();
		} else if (e.getSource() == create) {
            RefreshableFrame frame = AccountingMenuBar.getFrame(AccountingMenuBar.NEW_ACCOUNT);
            if(frame == null){
                System.err.println("frame not found");
            }
			frame.setVisible(true);
		} else if (e.getSource() == ok) {
			dispose();
		}

	}

	public Account getSelection() {
		return account;
	}
}
