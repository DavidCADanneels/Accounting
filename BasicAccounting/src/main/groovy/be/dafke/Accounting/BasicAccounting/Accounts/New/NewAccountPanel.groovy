package be.dafke.Accounting.BasicAccounting.Accounts.New

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class NewAccountPanel extends JPanel {
    JTextField nameField, numberField, defaultAmountField
    JComboBox<AccountType> type
    JButton addButton
    Accounts accounts
    Account account

    NewAccountPanel(Accounts accounts, ArrayList<AccountType> accountTypes) {
        this.accounts = accounts

        setLayout(new GridLayout(0,2))
        add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")))
        nameField = new JTextField(20)
        add(nameField)
        add(new JLabel(getBundle("Accounting").getString("ACCOUNT_NUMBER")))
        numberField = new JTextField(10)
        add(numberField)
        add(new JLabel(getBundle("Accounting").getString("DEFAULT_AMOUNT_LABEL")))
        defaultAmountField = new JTextField(10)
        add(defaultAmountField)
        add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")))
        type = new JComboBox<>()
        DefaultComboBoxModel<AccountType> model = new DefaultComboBoxModel<>()
        for (AccountType accountType : accountTypes) {
            model.addElement(accountType)
        }
        type.setModel(model)
        add(type)
        addButton = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNT"))
        addButton.addActionListener({ e -> saveAccount() })
        add(addButton)
    }

    void setAccount(Account account) {
        this.account = account
        nameField.setText(account.name)
        BigInteger number = account.number
        numberField.setText(number==null?"":number.toString())
        BigDecimal defaultAmount = account.defaultAmount
        defaultAmountField.setText(defaultAmount==null?"":defaultAmount.toString())
        type.setSelectedItem(account.type)
    }

    void saveAccount() {
        String newName = nameField.getText().trim()
        try {
            if (account == null) {
                account = new Account(newName.trim())
                accounts.addBusinessObject(account)
                Main.fireAccountDataChanged(account)
                saveOtherProperties()
                account = null
                clearFields()
            } else {
                String oldName = account.name
                accounts.modifyName(oldName, newName)
                saveOtherProperties()
            }
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_DUPLICATE_NAME, newName)
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_NAME_EMPTY)
        }
    }


    void saveOtherProperties(){
        account.type = (AccountType) type.selectedItem
        String defaultAmountFieldText = defaultAmountField.getText()
        if (defaultAmountFieldText != null && !defaultAmountFieldText.trim().equals("")) {
            try {
                BigDecimal defaultAmount = new BigDecimal(defaultAmountFieldText)
                defaultAmount = defaultAmount.setScale(2)
                account.defaultAmount = defaultAmount
            } catch (NumberFormatException nfe) {
                account.defaultAmount = null
            }
        }
        String numberText = numberField.getText()
        if(numberText != null && !numberText.trim().equals("")){
            try {
                BigInteger number = new BigInteger(numberText)
                account.number = number
            } catch (NumberFormatException nfe) {
                account.number = null
            }
        }
        Main.fireAccountDataChanged(account)
    }

    void clearFields() {
        nameField.setText("")
        numberField.setText("")
        defaultAmountField.setText("")
    }

}
