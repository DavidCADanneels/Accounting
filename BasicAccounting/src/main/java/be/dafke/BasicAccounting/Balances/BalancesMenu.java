package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 11:03
 */
public class BalancesMenu extends JMenu {
//    private static JMenuItem testBalance;
    private ArrayList<JMenuItem> items = new ArrayList<>();
    private static JMenuItem manage;
    private static Journals journals;
    private static Accounts accounts;
    private static Balances balances;
    private static AccountTypes accountTypes;
    private JournalInputGUI journalInputGUI;

    public BalancesMenu(JournalInputGUI journalInputGUI){
        super(getBundle("BusinessModel").getString("BALANSES"));
        this.journalInputGUI = journalInputGUI;
        setMnemonic(KeyEvent.VK_B);
//        testBalance = new JMenuItem(getBundle("BusinessModel").getString("TESTBALANCE"));
        manage = new JMenuItem(getBundle("BusinessModel").getString("MANAGE_BALANCES"));

//        testBalance.addActionListener(e -> TestBalance.getTestBalance(journals, accounts, journalInputGUI));
        manage.addActionListener(e -> BalancesManagementGUI.showBalancesManager(balances, accounts, accountTypes));
        manage.setEnabled(false);
//        add(testBalance);
        add(manage);
    }

    public void setAccounting(Accounting accounting) {
        journals = accounting==null?null:accounting.getJournals();
        accounts = accounting==null?null:accounting.getAccounts();
        balances = accounting==null?null:accounting.getBalances();
        accountTypes = accounting==null?null:accounting.getAccountTypes();

        setItems(balances!=null);
    }

    private void setItems(boolean enabled){
        removeAll();
        balances.getBusinessObjects().stream().forEach(balance -> {
            String name = balance.getName();
            JMenuItem item = new JMenuItem(name);
            item.addActionListener(e -> BalanceGUI.getBalance(journals, balances.getBusinessObject(name),journalInputGUI));
            item.setEnabled(enabled);
            add(item);
        });
        add(manage);
        manage.setEnabled(enabled);
    }
}
