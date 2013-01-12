package be.dafke.Coda;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import be.dafke.ParentFrame;
import be.dafke.Accounting.NewAccountGUI;
import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Accountings;

public class AccountSelector extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton create, ok;
	private Account account;
	private final JComboBox combo;
	private final ParentFrame parent;

	public AccountSelector(ParentFrame parent) {
		super(parent, "Select Account", true);
		this.parent = parent;
		Object[] accounts = Accountings.getCurrentAccounting().getAccounts().getAccounts().toArray();
		combo = new JComboBox(accounts);
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
			NewAccountGUI.getInstance(parent).setVisible(true);
		} else if (e.getSource() == ok) {
			dispose();
		}

	}

	public Account getSelection() {
		return account;
	}

}
