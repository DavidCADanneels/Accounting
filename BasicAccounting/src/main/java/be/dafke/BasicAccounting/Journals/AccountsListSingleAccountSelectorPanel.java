package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.NewAccountDialog;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.AccountsList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class AccountsListSingleAccountSelectorPanel extends JPanel {
    private JButton create;
    private JComboBox<Account> combo;
    private DefaultComboBoxModel<Account> model;
    private AccountsList accountsList;

    public AccountsListSingleAccountSelectorPanel(Accounts accounts, AccountTypes accountTypes) {
        model = new DefaultComboBoxModel<>();
        combo = new JComboBox<>(model);
        combo.addActionListener(e -> selectionChanged());
        create = new JButton("Add account(s) ...");
        create.addActionListener(e -> {
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes.getBusinessObjects());
            newAccountDialog.setLocation(getLocationOnScreen());
            newAccountDialog.setVisible(true);
        });
        add(combo);
        add(create);
        accounts.getBusinessObjects().stream().forEach(account -> model.addElement(account));
    }
    private void selectionChanged(){
        Account account = (Account) combo.getSelectedItem();
        if(accountsList!=null) accountsList.setAccount(account);
    }

    public void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList;
    }

    @Override
    public void setEnabled(boolean enabled){
        combo.setEnabled(enabled);
        create.setEnabled(enabled);
    }

    public void refresh() {
        boolean singleAccount = accountsList.isSingleAccount();
        Account account = singleAccount?accountsList.getAccount():null;
        combo.setSelectedItem(account);
    }
}
