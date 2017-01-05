package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 11:03
 */
public class BalancesMenu extends JMenu {
    private static JMenuItem testBalance, yearBalance, resultBalance, relationsBalance, manage;
    private static Journals journals;
    private static Accounts accounts;
    private static Balances balances;
    private static AccountTypes accountTypes;

    public BalancesMenu(JournalInputGUI journalInputGUI){
        super(getBundle("BusinessModel").getString("BALANSES"));
        setMnemonic(KeyEvent.VK_B);
        testBalance = new JMenuItem(getBundle("BusinessModel").getString("TESTBALANCE"));
        yearBalance = new JMenuItem(getBundle("BusinessModel").getString("YEARBALANCE"));
        resultBalance = new JMenuItem(getBundle("BusinessModel").getString("RESULTBALANCE"));
        relationsBalance = new JMenuItem(getBundle("BusinessModel").getString("RELATIONSBALANCE"));
        manage = new JMenuItem(getBundle("BusinessModel").getString("MANAGE_BALANCES"));

        testBalance.addActionListener(e -> TestBalance.getTestBalance(journals, accounts, journalInputGUI));
        yearBalance.addActionListener(e -> BalanceGUI.getBalance(journals, balances.getBusinessObject(Balances.YEAR_BALANCE),journalInputGUI));
        resultBalance.addActionListener(e -> BalanceGUI.getBalance(journals, balances.getBusinessObject(Balances.RESULT_BALANCE),journalInputGUI));
        relationsBalance.addActionListener(e -> BalanceGUI.getBalance(journals, balances.getBusinessObject(Balances.RELATIONS_BALANCE),journalInputGUI));
        manage.addActionListener(e -> BalancesManagementGUI.showBalancesManager(balances, accounts, accountTypes));
        relationsBalance.setEnabled(false);
        resultBalance.setEnabled(false);
        testBalance.setEnabled(false);
        yearBalance.setEnabled(false);
        manage.setEnabled(false);
        add(testBalance);
        add(resultBalance);
        add(yearBalance);
        add(relationsBalance);
        add(manage);
    }

    public static void setAccounting(Accounting accounting) {
        journals = accounting==null?null:accounting.getJournals();
        accounts = accounting==null?null:accounting.getAccounts();
        balances = accounting==null?null:accounting.getBalances();
        accountTypes = accounting==null?null:accounting.getAccountTypes();

        relationsBalance.setEnabled(balances!=null);
        resultBalance.setEnabled(balances!=null);
        testBalance.setEnabled(balances!=null);
        yearBalance.setEnabled(balances!=null);
        manage.setEnabled(balances!=null);
    }
}
