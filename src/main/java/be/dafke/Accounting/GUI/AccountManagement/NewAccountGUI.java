package be.dafke.Accounting.GUI.AccountManagement;

import be.dafke.Accounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.DuplicateAccountNameException;
import be.dafke.Accounting.Objects.Accounting.EmptyAccountNameException;
import be.dafke.RefreshableDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewAccountGUI extends RefreshableDialog implements ActionListener{
    private final JTextField nameField;
    private final JComboBox<Account.AccountType> type;
    private final JButton add;
    private final Accounting accounting;

    public NewAccountGUI(String title, Accounting accounting) {
        super(title);
        this.accounting = accounting;
        JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JPanel line1 = new JPanel();
		line1.add(new JLabel("Name:"));
		nameField = new JTextField(20);
		nameField.addActionListener(this);
		line1.add(nameField);
		JPanel line2 = new JPanel();
		line2.add(new JLabel("Type:"));
		type = new JComboBox<Account.AccountType>(Account.AccountType.values());
		line2.add(type);
		add = new JButton("Create new Account");
		add.addActionListener(this);
		line2.add(add);
		north.add(line1);
		north.add(line2);
        setContentPane(north);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == add || event.getSource() == nameField) {
            addAccount();
        }
    }

    private void addAccount() {
        String name = nameField.getText().trim();
        try {
            accounting.getAccounts().add(name, (Account.AccountType) type.getSelectedItem());
            AccountingMenuBar.refreshAllFrames();
        } catch (DuplicateAccountNameException e) {
            JOptionPane.showMessageDialog(this, "There is already an account with the name \""+name+"\".\r\n"+
                    "Please provide a new name");
        } catch (EmptyAccountNameException e) {
            JOptionPane.showMessageDialog(this, "The name cannot be empty.\r\nPlease provide a new name");
        }
        nameField.setText("");
    }

    @Override
    public void refresh() {
        // nothing to do here
    }
}
