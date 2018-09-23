package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.util.ArrayList;

/**
 * Created by ddanneels on 14/05/2017.
 */
public class AccountSelectorPanel extends JPanel {
    private Account account;
    private JComboBox<Account> combo;
    private DefaultComboBoxModel<Account> model;
    private Accounts accounts;
    private ArrayList<AccountType> accountTypes;

    AccountSelectorPanel(Accounts accounts, ArrayList<AccountType> accountTypes) {
        this.accountTypes = accountTypes;
        model = new DefaultComboBoxModel<>();
        combo = new JComboBox<>(model);
        combo.addActionListener(e -> account = (Account) combo.getSelectedItem());
        JButton create = new JButton("Add account(s) ...");
        create.addActionListener(e -> {
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes);
            newAccountDialog.setLocation(getLocationOnScreen());
            newAccountDialog.setVisible(true);
        });
        add(combo);
        add(create);
        setAccounts(accounts);
    }

    public Account getSelection() {
        return account;
    }

    public void fireAccountDataChanged() {
        model.removeAllElements();
        accounts.getAccountsByType(accountTypes).forEach(account -> model.addElement(account));
        invalidate();
        combo.invalidate();
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
        fireAccountDataChanged();
    }
}
