package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.MainWindow.AccountsGUI;
import be.dafke.BasicAccounting.Objects.Accountings;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsPopupMenu extends JPopupMenu implements ActionListener{
    private final JMenuItem debit, credit, manage, details;

    public AccountsPopupMenu(Accountings accountings, AccountsGUI gui) {
        debit = new JMenuItem(getBundle("Accounting").getString("DEBIT_ACTION"));
        credit = new JMenuItem(getBundle("Accounting").getString("CREDIT_ACTION"));
        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        details = new JMenuItem(getBundle("Accounting").getString("VIEW_ACCOUNT"));
        debit.setActionCommand(AddBookingToTransactionActionListener.DEBIT);
        credit.setActionCommand(AddBookingToTransactionActionListener.CREDIT);
        debit.addActionListener(new AddBookingToTransactionActionListener(accountings, gui));
        credit.addActionListener(new AddBookingToTransactionActionListener(accountings, gui));
        details.addActionListener(new AccountDetailsActionListener(accountings));
        manage.addActionListener(new AccountManagementActionListener(accountings));
        add(debit);
        add(credit);
        add(manage);
        add(details);
        debit.addActionListener(this);
        credit.addActionListener(this);
        manage.addActionListener(this);
        details.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
