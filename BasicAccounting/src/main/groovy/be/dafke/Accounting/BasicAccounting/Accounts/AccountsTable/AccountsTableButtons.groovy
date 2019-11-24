package be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable

import be.dafke.Accounting.BusinessModel.AccountsList

import javax.swing.*
import java.awt.event.KeyEvent

import static java.util.ResourceBundle.getBundle

class AccountsTableButtons extends JPanel {
    private JButton leftButton, rightButton, accountDetails
    private boolean leftAction = true
    private boolean rightAction = false

    AccountsTableButtons(AccountsTablePanel accountsGUI, boolean left) {
        leftButton = new JButton()
        rightButton = new JButton()
        accountDetails = new JButton(getBundle("Accounting").getString("VIEW_ACCOUNT"))

        if (left) {
            leftButton.setMnemonic(KeyEvent.VK_D)
        } else {
            rightButton.setMnemonic(KeyEvent.VK_C)
        }
//        accountDetails.setMnemonic(KeyEvent.VK_T)

        leftButton.addActionListener({ e -> accountsGUI.book(leftAction) })
        rightButton.addActionListener({ e -> accountsGUI.book(rightAction) })
        accountDetails.addActionListener({ e -> accountsGUI.showDetails() })

        leftButton.setText(getBundle("Accounting").getString("DEBIT_ACTION"))
        rightButton.setText(getBundle("Accounting").getString("CREDIT_ACTION"))

        leftButton.setEnabled(false)
        rightButton.setEnabled(false)
        accountDetails.setEnabled(false)

        add(leftButton)
        add(rightButton)
        add(accountDetails)
    }

    void setActive(boolean active){
        accountDetails.setEnabled(active)
        leftButton.setEnabled(active)
        rightButton.setEnabled(active)
    }

    void setAccountsList(AccountsList accountsList) {
        leftButton.setText(accountsList.getLeftButton())
        leftAction = accountsList.isLeftAction()
        rightButton.setText(accountsList.getRightButton())
        rightAction = accountsList.isRightAction()
    }
}
