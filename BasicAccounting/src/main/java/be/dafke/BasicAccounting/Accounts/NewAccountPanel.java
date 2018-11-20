package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class NewAccountPanel extends JPanel {
    private JTextField nameField, numberField, defaultAmountField;
    private JComboBox<AccountType> type;
    private JButton add;
    private Accounts accounts;
    private Account account;

    public NewAccountPanel(Accounts accounts, ArrayList<AccountType> accountTypes) {
        this.accounts = accounts;

        setLayout(new GridLayout(0,2));
        add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")));
        nameField = new JTextField(20);
        add(nameField);
        add(new JLabel(getBundle("Accounting").getString("ACCOUNT_NUMBER")));
        numberField = new JTextField(10);
        add(numberField);
        add(new JLabel(getBundle("Accounting").getString("DEFAULT_AMOUNT_LABEL")));
        defaultAmountField = new JTextField(10);
        add(defaultAmountField);
        add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")));
        type = new JComboBox<>();
        DefaultComboBoxModel<AccountType> model = new DefaultComboBoxModel<>();
        for (AccountType accountType : accountTypes) {
            model.addElement(accountType);
        }
        type.setModel(model);
        add(type);
        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNT"));
        add.addActionListener(e -> saveAccount());
        add(add);
    }

    public void setAccount(Account account) {
        this.account = account;
        nameField.setText(account.getName());
        BigInteger number = account.getNumber();
        numberField.setText(number==null?"":number.toString());
        BigDecimal defaultAmount = account.getDefaultAmount();
        defaultAmountField.setText(defaultAmount==null?"":defaultAmount.toString());
        type.setSelectedItem(account.getType());
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
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_DUPLICATE_NAME, newName);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_NAME_EMPTY);
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

}
