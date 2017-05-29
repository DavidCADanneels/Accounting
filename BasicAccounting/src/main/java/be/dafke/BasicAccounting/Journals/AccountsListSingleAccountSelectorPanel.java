package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.AccountsList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * Created by ddanneels on 14/05/2017.
 */
public class AccountsListSingleAccountSelectorPanel extends JPanel {
    private JButton create;
    private Account account;
    private JComboBox<Account> combo;
    private DefaultComboBoxModel<Account> model;
    private Accounts accounts;
    private AccountsList accountsList;

    public AccountsListSingleAccountSelectorPanel(AccountsList accountsList, Accounts accounts, AccountTypes accountTypes) {
        this.accountsList = accountsList;
        model = new DefaultComboBoxModel<>();
        combo = new JComboBox<>(model);
        combo.addActionListener(e -> {
            account = (Account) combo.getSelectedItem();
            if(accountsList!=null) accountsList.setAccount(account);
        });
        create = new JButton("Add account(s) ...");
        create.addActionListener(e -> new NewAccountGUI(accounts, accountTypes).setVisible(true));
        add(combo);
        add(create);
        setAccounts(accounts);
        combo.setSelectedItem(null);
    }

    public Account getSelection() {
        return account;
    }

    public void fireAccountDataChanged() {
        model.removeAllElements();
        accounts.getBusinessObjects().stream().forEach(account -> model.addElement(account));
        invalidate();
        combo.invalidate();
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
        fireAccountDataChanged();
    }
}
