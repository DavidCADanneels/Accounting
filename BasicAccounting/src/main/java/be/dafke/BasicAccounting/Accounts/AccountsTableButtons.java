package be.dafke.BasicAccounting.Accounts;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 1/05/2017.
 */
public class AccountsTableButtons extends JPanel {
    private JButton debet, credit, accountDetails;

    public AccountsTableButtons(AccountsGUI accountsGUI) {
        debet = new JButton(getBundle("Accounting").getString("DEBIT_ACTION"));
        credit = new JButton(getBundle("Accounting").getString("CREDIT_ACTION"));
        accountDetails = new JButton(getBundle("Accounting").getString("VIEW_ACCOUNT"));

        debet.setMnemonic(KeyEvent.VK_D);
        credit.setMnemonic(KeyEvent.VK_C);
        accountDetails.setMnemonic(KeyEvent.VK_T);

        debet.addActionListener(e -> accountsGUI.book(true));
        credit.addActionListener(e -> accountsGUI.book(false));
        accountDetails.addActionListener(e -> accountsGUI.showDetails());

        debet.setEnabled(false);
        credit.setEnabled(false);
        accountDetails.setEnabled(false);

        add(debet);
        add(credit);
        add(accountDetails);
    }

    public void setActive(boolean active){
        accountDetails.setEnabled(active);
        debet.setEnabled(active);
        credit.setEnabled(active);
    }

}
