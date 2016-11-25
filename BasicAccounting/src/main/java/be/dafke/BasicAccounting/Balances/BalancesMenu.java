package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.MainApplication.Main;
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
    private static JMenuItem testBalance, yearBalance, resultBalance, relationsBalance;
    private static Journals journals;
    private static Accounts accounts;
    private static AccountTypes accountTypes;
    private static Balances balances;

    public BalancesMenu(){
        super(getBundle("BusinessModel").getString("BALANSES"));
        setMnemonic(KeyEvent.VK_B);
        testBalance = new JMenuItem(getBundle("BusinessModel").getString("TESTBALANCE"));
        yearBalance = new JMenuItem(getBundle("BusinessModel").getString("YEARBALANCE"));
        resultBalance = new JMenuItem(getBundle("BusinessModel").getString("RESULTBALANCE"));
        relationsBalance = new JMenuItem(getBundle("BusinessModel").getString("RELATIONSBALANCE"));

        testBalance.addActionListener(e -> Main.getTestBalance(journals, accounts, accountTypes));
        yearBalance.addActionListener(e -> Main.getBalance(journals, balances.getBusinessObject(Balances.YEAR_BALANCE)));
        resultBalance.addActionListener(e -> Main.getBalance(journals, balances.getBusinessObject(Balances.RESULT_BALANCE)));
        relationsBalance.addActionListener(e -> Main.getBalance(journals, balances.getBusinessObject(Balances.RELATIONS_BALANCE)));
        relationsBalance.setEnabled(false);
        resultBalance.setEnabled(false);
        testBalance.setEnabled(false);
        yearBalance.setEnabled(false);
        add(testBalance);
        add(resultBalance);
        add(yearBalance);
        add(relationsBalance);
    }

    public static void setAccounting(Accounting accounting) {
        journals = accounting==null?null:accounting.getJournals();
        accounts = accounting==null?null:accounting.getAccounts();
        accountTypes = accounting==null?null:accounting.getAccountTypes();
        balances = accounting==null?null:accounting.getBalances();
        relationsBalance.setEnabled(balances!=null);
        resultBalance.setEnabled(balances!=null);
        testBalance.setEnabled(balances!=null);
        yearBalance.setEnabled(balances!=null);
    }
}
