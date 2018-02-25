package be.dafke.BasicAccounting.Accounts.AccountsTable;

import be.dafke.BusinessModel.AccountsList;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 1/05/2017.
 */
public class AccountsTableButtons extends JPanel {
    private JButton leftButton, rightButton, accountDetails;
    private boolean leftAction = true;
    private boolean rightAction = false;

    public AccountsTableButtons(AccountsTableGUI accountsGUI, boolean left) {
        leftButton = new JButton();
        rightButton = new JButton();
        accountDetails = new JButton(getBundle("Accounting").getString("VIEW_ACCOUNT"));

        if (left) {
            leftButton.setMnemonic(KeyEvent.VK_D);
        } else {
            rightButton.setMnemonic(KeyEvent.VK_C);
        }
//        accountDetails.setMnemonic(KeyEvent.VK_T);

        leftButton.addActionListener(e -> accountsGUI.book(leftAction));
        rightButton.addActionListener(e -> accountsGUI.book(rightAction));
        accountDetails.addActionListener(e -> accountsGUI.showDetails());

        leftButton.setText(getBundle("Accounting").getString("DEBIT_ACTION"));
        rightButton.setText(getBundle("Accounting").getString("CREDIT_ACTION"));

        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
        accountDetails.setEnabled(false);

        add(leftButton);
        add(rightButton);
        add(accountDetails);
    }

    public void setActive(boolean active){
        accountDetails.setEnabled(active);
        leftButton.setEnabled(active);
        rightButton.setEnabled(active);
    }

    public void setAccountsList(AccountsList accountsList) {
        leftButton.setText(accountsList.getLeftButton());
        leftAction = accountsList.isLeftAction();
        rightButton.setText(accountsList.getRightButton());
        rightAction = accountsList.isRightAction();
    }
}
