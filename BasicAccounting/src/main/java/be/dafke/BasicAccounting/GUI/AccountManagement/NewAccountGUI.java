package be.dafke.BasicAccounting.GUI.AccountManagement;

import be.dafke.BasicAccounting.GUI.ComponentMap;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

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
    private final JComboBox<AccountType> type;
    private final JButton add;
    private final Accounting accounting;

    public NewAccountGUI(Accounting accounting) {
        super("Create new Account in " + accounting.toString());
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
		type = new JComboBox<AccountType>();
        DefaultComboBoxModel<AccountType> model = new DefaultComboBoxModel<AccountType>();
        for(AccountType accountType : accounting.getAccountTypes().getBusinessObjects()){
            model.addElement(accountType);
        }
        type.setModel(model);
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
            Account account = new Account(name.trim());
//            account.setName(name.trim());
            account.setType((AccountType) type.getSelectedItem());
            accounting.getAccounts().addBusinessObject(account);
            ComponentMap.refreshAllFrames();
        } catch (DuplicateNameException e) {
            JOptionPane.showMessageDialog(this, "There is already an account with the name \""+name+"\".\r\n"+
                    "Please provide a new name.");
        } catch (EmptyNameException e) {
            JOptionPane.showMessageDialog(this, "The name cannot be empty.\r\nPlease provide a new name.");
        }
        nameField.setText("");
    }

    @Override
    public void refresh() {
        // nothing to do here
    }
}
