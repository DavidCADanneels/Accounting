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
        super(getBundle("BusinessModel").getString("BALANCES"));
        this.journalInputGUI = journalInputGUI;
        setMnemonic(KeyEvent.VK_B);
        manage = new JMenuItem(getBundle("BusinessModel").getString("MANAGE_BALANCES"));
        manage.addActionListener(e -> BalancesManagementGUI.showBalancesManager(balances, accounts, accountTypes));
        add(manage);
    }

    public void setAccounting(Accounting accounting) {
        journals = accounting==null?null:accounting.getJournals();
        accounts = accounting==null?null:accounting.getAccounts();
        balances = accounting==null?null:accounting.getBalances();
        accountTypes = accounting==null?null:accounting.getAccountTypes();

        fireBalancesChanged();
    }

    public void fireBalancesChanged(){
        removeAll();
        if(balances!=null) {
            balances.getBusinessObjects().stream().forEach(balance -> {
                String name = balance.getName();
                JMenuItem item = new JMenuItem(name);
                item.addActionListener(e -> BalanceGUI.getBalance(journals, balances.getBusinessObject(name), journalInputGUI));
                add(item);
            });
            add(manage);
        }
    }
}
