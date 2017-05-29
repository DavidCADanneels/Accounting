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
    private JComboBox<Account> combo;
    private DefaultComboBoxModel<Account> model;
    private AccountsList accountsList;

    public AccountsListSingleAccountSelectorPanel(AccountsList accountsList, Accounts accounts, AccountTypes accountTypes) {
        model = new DefaultComboBoxModel<>();
        combo = new JComboBox<>(model);
        combo.addActionListener(e -> selectionChanged());
        create = new JButton("Add account(s) ...");
        create.addActionListener(e -> new NewAccountGUI(accounts, accountTypes.getBusinessObjects()).setVisible(true));
        add(combo);
        add(create);
        this.accountsList = accountsList;
        accounts.getBusinessObjects().stream().forEach(account -> model.addElement(account));
        refresh();
    }
    private void selectionChanged(){
        Account account = (Account) combo.getSelectedItem();
        if(accountsList!=null) accountsList.setAccount(account);
    }

    public void refresh() {
        boolean singleAccount = accountsList.isSingleAccount();
        Account account = singleAccount?accountsList.getAccount():null;
        combo.setSelectedItem(account);
    }
}
