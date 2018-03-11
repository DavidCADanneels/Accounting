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
    private JTextField nameField, numberField, defaultAmountField;
    private JComboBox<AccountType> type;
    private JButton add;
    private Accounts accounts;
    private Account account;

    public NewAccountDialog(Accounts accounts, ArrayList<AccountType> accountTypes) {
        super(getBundle("Accounting").getString("NEW_ACCOUNT_GUI_TITLE"));
        this.accounts = accounts;
        setContentPane(createContentPanel(accountTypes));
        pack();
    }

    private JPanel createContentPanel(ArrayList<AccountType> accountTypes){
        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")));
        nameField = new JTextField(20);
        panel.add(nameField);
        panel.add(new JLabel(getBundle("Accounting").getString("ACCOUNT_NUMBER")));
        numberField = new JTextField(10);
        panel.add(numberField);
        panel.add(new JLabel(getBundle("Accounting").getString("DEFAULT_AMOUNT_LABEL")));
        defaultAmountField = new JTextField(10);
        panel.add(defaultAmountField);
        panel.add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")));
        type = new JComboBox<>();
        DefaultComboBoxModel<AccountType> model = new DefaultComboBoxModel<>();
        for (AccountType accountType : accountTypes) {
            model.addElement(accountType);
        }
        type.setModel(model);
        panel.add(type);
        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNT"));
        add.addActionListener(e -> saveAccount());
        panel.add(add);
        return panel;
    }

    private void saveAccount() {
        String newName = nameField.getText().trim();
        try {
            if (account == null) {
                account = new Account(newName.trim());
                accounts.addBusinessObject(account);
                Main.fireAccountDataChanged(account);
                saveOtherProperties();
                account = null;
                clearFields();
            } else {
                String oldName = account.getName();
                accounts.modifyName(oldName, newName);
                saveOtherProperties();
            }
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_DUPLICATE_NAME, newName);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NAME_EMPTY);
        }
    }

    private void saveOtherProperties(){
        account.setType((AccountType) type.getSelectedItem());
        String defaultAmountFieldText = defaultAmountField.getText();
        if (defaultAmountFieldText != null && !defaultAmountFieldText.trim().equals("")) {
            try {
                BigDecimal defaultAmount = new BigDecimal(defaultAmountFieldText);
                defaultAmount = defaultAmount.setScale(2);
                account.setDefaultAmount(defaultAmount);
            } catch (NumberFormatException nfe) {
                account.setDefaultAmount(null);
            }
        }
        String numberText = numberField.getText();
        if(numberText != null && !numberText.trim().equals("")){
            try {
                BigInteger number = new BigInteger(numberText);
                account.setNumber(number);
            } catch (NumberFormatException nfe) {
                account.setNumber(null);
            }
        }
        Main.fireAccountDataChanged(account);
    }

    private void clearFields() {
        nameField.setText("");
        numberField.setText("");
        defaultAmountField.setText("");
    }

    public void setAccount(Account account) {
        this.account = account;
        nameField.setText(account.getName());
        BigInteger number = account.getNumber();
        numberField.setText(number==null?"":number.toString());
        BigDecimal defaultAmount = account.getDefaultAmount();
        defaultAmountField.setText(defaultAmount==null?"":defaultAmount.toString());
    }
}