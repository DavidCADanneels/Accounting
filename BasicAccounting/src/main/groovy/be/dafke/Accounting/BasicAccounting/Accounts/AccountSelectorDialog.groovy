package be.dafke.Accounting.BasicAccounting.Accounts

import be.dafke.Accounting.BusinessModel.Account

import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.ComponentModel.RefreshableDialog

import javax.swing.*
import java.awt.*

class AccountSelectorDialog extends RefreshableDialog {
    private JButton ok
    private AccountSelectorPanel selectorPanel
    private static AccountSelectorDialog selectorDialog = null

    private AccountSelectorDialog(Accounts accounts, ArrayList<AccountType> accountTypes) {
        this(accounts, accountTypes, "Select Account")
    }
    AccountSelectorDialog(Accounts accounts, ArrayList<AccountType> accountTypes, String title) {
        super(title)
        selectorPanel = new AccountSelectorPanel(accounts, accountTypes)
        JPanel innerPanel = new JPanel(new BorderLayout())
        innerPanel.add(selectorPanel, BorderLayout.CENTER)

        ok = new JButton("Ok (Close popup)")
        ok.addActionListener({ e -> dispose() })
        innerPanel.add(ok, BorderLayout.SOUTH)

        setContentPane(innerPanel)
        setAccounts(accounts)
        pack()
    }

    static AccountSelectorDialog getAccountSelector(Accounts accounts, ArrayList<AccountType> accountTypes, String title){
        if(selectorDialog ==null){
            selectorDialog = new AccountSelectorDialog(accounts, accountTypes, title)
        } else selectorDialog.setTitle(title)
        selectorDialog
    }

    static AccountSelectorDialog getAccountSelector(Accounts accounts, ArrayList<AccountType> accountTypes){
        if(selectorDialog ==null){
            selectorDialog = new AccountSelectorDialog(accounts, accountTypes)
        }
        selectorDialog
    }

    Account getSelection() {
        selectorPanel.getSelection()
    }

    void setAccounts(Accounts accounts) {
        selectorPanel.setAccounts(accounts)
    }

    static void fireAccountDataChangedForAll() {
        if(selectorDialog !=null){
            selectorDialog.fireAccountDataChanged()
        }
    }

    void fireAccountDataChanged() {
        selectorPanel.fireAccountDataChanged()
    }
}
