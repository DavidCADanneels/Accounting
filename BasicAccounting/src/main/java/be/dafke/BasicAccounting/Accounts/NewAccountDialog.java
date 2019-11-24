package be.dafke.BasicAccounting.Accounts;

import be.dafke.Accounting.BusinessModel.Account;
import be.dafke.Accounting.BusinessModel.AccountType;
import be.dafke.Accounting.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewAccountDialog extends RefreshableDialog {
    private NewAccountPanel newAccountPanel;

    public NewAccountDialog(Accounts accounts, ArrayList<AccountType> accountTypes) {
        super(getBundle("Accounting").getString("NEW_ACCOUNT_GUI_TITLE"));
        newAccountPanel = new NewAccountPanel(accounts, accountTypes);
        setContentPane(newAccountPanel);
        pack();
    }

    public void setAccount(Account account) {
        newAccountPanel.setAccount(account);
    }
}
