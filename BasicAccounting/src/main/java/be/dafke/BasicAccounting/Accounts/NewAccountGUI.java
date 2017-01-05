package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewAccountGUI extends RefreshableDialog {
    private JTextField nameField, numberField, defaultAmountField;
    private JComboBox<AccountType> type;
    private JButton add;
    private Accounts accounts;

    public NewAccountGUI(Accounts accounts, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("NEW_ACCOUNT_GUI_TITLE"));
        this.accounts = accounts;
        setContentPane(createContentPanel(accountTypes));
        pack();
    }

    private JPanel createContentPanel(AccountTypes accountTypes){
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
        for (AccountType accountType : accountTypes.getBusinessObjects()) {
            model.addElement(accountType);
        }
        type.setModel(model);
        panel.add(type);
        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNT"));
        add.addActionListener(e -> addAccount());
        panel.add(add);
        return panel;
    }

    private void addAccount() {
        String name = nameField.getText().trim();
        try {
            Account account = new Account(name.trim());
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
            accounts.addBusinessObject(account);
            Main.fireAccountDataChanged(account);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_DUPLICATE_NAME, name);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NAME_EMPTY);
        }
        nameField.setText("");
        numberField.setText("");
        defaultAmountField.setText("");
    }
}
