package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.BigInteger;
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
