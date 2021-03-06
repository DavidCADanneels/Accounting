package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.AlphabeticListModel

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.awt.event.FocusEvent
import java.awt.event.FocusListener

import static java.awt.BorderLayout.*
import static java.util.ResourceBundle.getBundle

class BalancesManagementPanel extends JPanel implements FocusListener {
    final Accounts accounts
    Balance balance
    Balances balances
    JList<AccountType> debit, credit, types
    ArrayList<AccountType> allTypes
    AlphabeticListModel<AccountType> debitModel, creditModel, typesModel
    JTextField leftName, rightName, leftTotalName, rightTotalName, leftResultName, rightResultName
    JComboBox<Balance> combo

    BalancesManagementPanel(Balances balances, Accounts accounts, AccountTypes accountTypes) {
        this.accounts = accounts

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
        add(createSavePanel())
        add(createNameFieldPanel())
        add(createCenterPanel())

        setAccountTypes(accountTypes)
        setBalances(balances)
    }

    void setAccountTypes(AccountTypes accountTypes) {
        allTypes = new ArrayList<>()

        allTypes.clear()
        allTypes.addAll(accountTypes.businessObjects)
        creditModel.removeAllElements()
        debitModel.removeAllElements()
        typesModel.removeAllElements()
        for(AccountType type : allTypes) {
            typesModel.addElement(type)
        }
    }

    void setBalances(Balances balances){
        this.balances = balances
        combo.removeAllItems()
        for(Balance balance : balances.businessObjects){
            ((DefaultComboBoxModel<Balance>) combo.getModel()).addElement(balance)
        }
    }

    JPanel createSavePanel() {
        JPanel panel = new JPanel()
        JButton newType = new JButton(getBundle("Accounting").getString("NEW_BALANCE"))
        newType.addActionListener({ e -> createNewBalance() })
        combo = new JComboBox<>()
        combo.addActionListener({ e -> comboAction() })
        panel.add(combo)
        panel.add(newType)
        panel
    }

    void comboAction() {
        balance = (Balance) combo.selectedItem
        debitModel.removeAllElements()
        creditModel.removeAllElements()
        if(balance!=null){
            for (AccountType type : balance.getLeftTypes()) {
                debitModel.addElement(type)
            }
            for (AccountType type : balance.getRightTypes()) {
                creditModel.addElement(type)
            }
        }
        leftName.setText(balance==null?"":balance.getLeftName())
        rightName.setText(balance==null?"":balance.getRightName())
        leftTotalName.setText(balance==null?"":balance.getLeftTotalName())
        rightTotalName.setText(balance==null?"":balance.getRightTotalName())
        leftResultName.setText(balance==null?"":balance.getLeftResultName())
        rightResultName.setText(balance==null?"":balance.getRightResultName())
    }

    void createNewBalance(){
        String name = JOptionPane.showInputDialog(this, getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"))
        while (name != null && name.equals(""))
            name = JOptionPane.showInputDialog(this, getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"))
        if (name != null) {
            Balance balance = new Balance(name,accounts)
            try {
                balances.addBusinessObject(balance)
                Main.fireBalancesChanged()
            } catch (EmptyNameException e) {
                e.printStackTrace()
            } catch (DuplicateNameException e) {
                e.printStackTrace()
            }
            ((DefaultComboBoxModel<Balance>) combo.getModel()).addElement(balance)
            (combo.getModel()).setSelectedItem(balance)
        }
    }

    JPanel createNameFieldPanel(){
        leftName = new JTextField(20)
        rightName = new JTextField(20)
        leftTotalName = new JTextField(20)
        rightTotalName = new JTextField(20)
        leftResultName = new JTextField(20)
        rightResultName = new JTextField(20)

        leftName.addFocusListener(this)
        rightName.addFocusListener(this)
        leftTotalName.addFocusListener(this)
        rightTotalName.addFocusListener(this)
        leftResultName.addFocusListener(this)
        rightResultName.addFocusListener(this)

        JPanel panel = new JPanel(new GridLayout(0,2))
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Names"))
        panel.add(new JLabel("LeftName:"))
        panel.add(leftName)
        panel.add(new JLabel("RightName:"))
        panel.add(rightName)
        panel.add(new JLabel("LeftTotalName:"))
        panel.add(leftTotalName)
        panel.add(new JLabel("RightTotalName:"))
        panel.add(rightTotalName)
        panel.add(new JLabel("LeftResultName:"))
        panel.add(leftResultName)
        panel.add(new JLabel("RightResultName:"))
        panel.add(rightResultName)
        panel
    }

    JPanel createCenterPanel(){
        JPanel panel = new JPanel()
        panel.setLayout(new GridLayout(1,0))

        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("ACCOUNTTYPES")))
        JPanel center = new JPanel(new BorderLayout())
        center.add(createButtonPanel(), SOUTH)
        center.add(createAccountTypesPanel(),CENTER)

        panel.add(center)
        panel.add(createDebitTypesPanel())
        panel.add(createCreditTypesPanel())

        panel
    }

    JPanel createCreditTypesPanel() {
        JButton removeCreditType = new JButton(getBundle("Accounting").getString("DELETE"))
        removeCreditType.addActionListener({ e -> removeCredit() })

        creditModel = new AlphabeticListModel<>()
        credit = new JList<>(creditModel)
        JPanel panel = new JPanel()
        panel.setLayout(new BorderLayout())
        panel.add(new JLabel(getBundle("Accounting").getString("CREDIT_TYPES")), NORTH)
        panel.add(new JScrollPane(credit), CENTER)
        JPanel buttonPanel = new JPanel()
        buttonPanel.add(removeCreditType)
        panel.add(buttonPanel, SOUTH)
        panel
    }

    JPanel createDebitTypesPanel() {
        JButton removeDebitType = new JButton(getBundle("Accounting").getString("DELETE"))
        removeDebitType.addActionListener({ e -> removeDebit() })

        debitModel = new AlphabeticListModel<>()
        debit = new JList<>(debitModel)
        JPanel panel = new JPanel()
        panel.setLayout(new BorderLayout())
        panel.add(new JLabel(getBundle("Accounting").getString("DEBIT_TYPES")), NORTH)
        panel.add(new JScrollPane(debit), CENTER)

        JPanel buttonPanel = new JPanel()
        buttonPanel.add(removeDebitType)
        panel.add(buttonPanel, SOUTH)
        panel
    }


    JPanel createButtonPanel(){
        JButton addDebitType = new JButton(getBundle("Accounting").getString("ADD_TO_DEBIT_TYPES"))
        JButton addCreditType = new JButton(getBundle("Accounting").getString("ADD_TO_CREDIT_TYPES"))
        addDebitType.addActionListener({ e -> addDebit() })
        addCreditType.addActionListener({ e -> addCredit() })

        JPanel buttonPanel = new JPanel()
        buttonPanel.add(addDebitType)
        buttonPanel.add(addCreditType)
        buttonPanel
    }

    JPanel createAccountTypesPanel() {
        typesModel = new AlphabeticListModel<>()
        types = new JList<>(typesModel)
        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        panel.add(new JLabel(getBundle("Accounting").getString("ACCOUNT_TYPES")))
        panel.add(new JScrollPane(types))
        panel
    }

    void addDebit() {
        for (AccountType type:types.getSelectedValuesList()) {
            balance.addLeftType(type)
            debitModel.addElement(type)
        }
    }
    void addCredit() {
        for (AccountType type:types.getSelectedValuesList()) {
            balance.addRightType(type)
            creditModel.addElement(type)
        }
    }
    void removeDebit() {
        for(AccountType type : debit.getSelectedValuesList()) {
            balance.removeLeftType(type)
            debitModel.removeElement(type)
        }
    }

    void removeCredit() {
        for(AccountType type : credit.getSelectedValuesList()) {
            balance.removeRightType(type)
            creditModel.removeElement(type)
        }
    }


    @Override
    void focusGained(FocusEvent e) {
        JTextField textField = (JTextField)e.getSource()
        textField.selectAll()
    }

    @Override
    void focusLost(FocusEvent e) {
        JTextField textField = (JTextField)e.getSource()
        String text = textField.getText().trim()
        if(textField == leftName){
            balance.setLeftName(text)
        } else if(textField == rightName){
            balance.setRightName(text)
        } else if(textField == leftTotalName){
            balance.setLeftTotalName(text)
        } else if(textField == rightTotalName){
            balance.setRightTotalName(text)
        } else if(textField == leftResultName){
            balance.setLeftResultName(text)
        } else if(textField == rightResultName){
            balance.setRightResultName(text)
        } else if(textField == leftName){
            balance.setLeftName(text)
        }
    }

}
