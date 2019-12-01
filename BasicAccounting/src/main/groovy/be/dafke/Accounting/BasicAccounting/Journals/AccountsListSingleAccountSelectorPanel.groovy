package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.NewAccountDialog
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.AccountsList

import javax.swing.*

class AccountsListSingleAccountSelectorPanel extends JPanel {
    private JButton create
    private JComboBox<Account> combo
    private DefaultComboBoxModel<Account> model
    private AccountsList accountsList

    AccountsListSingleAccountSelectorPanel(Accounts accounts, AccountTypes accountTypes) {
        model = new DefaultComboBoxModel<>()
        combo = new JComboBox<>(model)
        combo.addActionListener({ e -> selectionChanged() })
        create = new JButton("Add account(s) ...")
        create.addActionListener({ e ->
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes.getBusinessObjects())
            newAccountDialog.setLocation(getLocationOnScreen())
            newAccountDialog.setVisible(true)
        })
        add(combo)
        add(create)
        accounts.getBusinessObjects().stream().forEach({ account -> model.addElement(account) })
    }
    private void selectionChanged(){
        Account account = (Account) combo.getSelectedItem()
        if(accountsList!=null) accountsList.setAccount(account)
    }

    void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList
    }

    @Override
    void setEnabled(boolean enabled){
        combo.setEnabled(enabled)
        create.setEnabled(enabled)
    }

    void refresh() {
        boolean singleAccount = accountsList.isSingleAccount()
        Account account = singleAccount?accountsList.getAccount():null
        combo.setSelectedItem(account)
    }
}
