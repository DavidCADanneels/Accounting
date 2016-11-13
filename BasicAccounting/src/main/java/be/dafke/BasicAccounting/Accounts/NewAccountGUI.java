package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewAccountGUI extends RefreshableDialog implements ActionListener{
    private final JTextField nameField, defaultAmountField;
    private final JComboBox<AccountType> type;
    private final JButton add;
    private Accounts accounts;

    public NewAccountGUI(Accounts accounts, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("NEW_ACCOUNT_GUI_TITLE"));
        this.accounts = accounts;
        JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JPanel line1 = new JPanel();
		line1.add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")));
		nameField = new JTextField(20);
		nameField.addActionListener(this);
        defaultAmountField = new JTextField(10);
		line1.add(nameField);
        line1.add(new JLabel(getBundle("Accounting").getString("DEFAULT_AMOUNT_LABEL")));
        line1.add(defaultAmountField);
		JPanel line2 = new JPanel();
		line2.add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")));
		type = new JComboBox<>();
        DefaultComboBoxModel<AccountType> model = new DefaultComboBoxModel<>();
        for(AccountType accountType : accountTypes.getBusinessObjects()){
            model.addElement(accountType);
        }
        type.setModel(model);
		line2.add(type);
		add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNT"));
		add.addActionListener(this);
		line2.add(add);
		north.add(line1);
		north.add(line2);
        setContentPane(north);
        pack();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == add || event.getSource() == nameField) {
            addAccount();
        }
    }

    private void addAccount() {
        String name = nameField.getText().trim();
        try {
            Account account = new Account(name.trim());
            account.setType((AccountType) type.getSelectedItem());
            String text = defaultAmountField.getText();
            if(text!=null && !text.trim().equals("")){
                try{
                    BigDecimal defaultAmount = new BigDecimal(text);
                    defaultAmount = defaultAmount.setScale(2);
                    account.setDefaultAmount(defaultAmount);
                } catch (NumberFormatException nfe){
                    account.setDefaultAmount(null);
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
        defaultAmountField.setText("");
    }

    public void refresh() {
        // nothing to do here
    }
}
