package be.dafke.Balances;

import be.dafke.Balances.Actions.BalanceLauncher;
import be.dafke.Balances.Actions.TestBalanceLauncher;
import be.dafke.Balances.Objects.Balance;
import be.dafke.Balances.Objects.Balances;
import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 11:03
 */
public class BalancesExtension implements AccountingExtension, ActionListener {
    private static JMenu balancesMenu = null;
    private final TestBalanceLauncher testBalanceLauncher = new TestBalanceLauncher();
    private final BalanceLauncher balanceLauncher = new BalanceLauncher();
    private JMenuItem testBalance, yearBalance, resultBalance, relationsBalance;
    private final Accountings accountings;

    public BalancesExtension(Accountings accountings, AccountingMenuBar menuBar){
        this.accountings = accountings;
        if(balancesMenu == null) createMenu(menuBar, accountings);
        for(Accounting accounting: accountings.getBusinessObjects()) {
            new Balances(accounting);
            accounting.addExtension(this);
        }
    }

    private void createMenu(AccountingMenuBar menuBar, Accountings accountings) {
        balancesMenu = new JMenu(getBundle("Balances").getString("BALANSES"));
        balancesMenu.setMnemonic(KeyEvent.VK_B);
        testBalance = new JMenuItem(getBundle("Balances").getString("TESTBALANCE"));
        yearBalance = new JMenuItem(getBundle("Balances").getString("YEARBALANCE"));
        resultBalance = new JMenuItem(getBundle("Balances").getString("RESULTBALANCE"));
        relationsBalance = new JMenuItem(getBundle("Balances").getString("RELATIONSBALANCE"));

        testBalance.addActionListener(this);
        yearBalance.addActionListener(this);
        resultBalance.addActionListener(this);
        relationsBalance.addActionListener(this);
        relationsBalance.setEnabled(false);
        resultBalance.setEnabled(false);
        testBalance.setEnabled(false);
        yearBalance.setEnabled(false);
        balancesMenu.add(testBalance);
        balancesMenu.add(resultBalance);
        balancesMenu.add(yearBalance);
        balancesMenu.add(relationsBalance);
        menuBar.addRefreshableMenuItem(testBalance);
        menuBar.addRefreshableMenuItem(resultBalance);
        menuBar.addRefreshableMenuItem(yearBalance);
        menuBar.addRefreshableMenuItem(relationsBalance);
        menuBar.add(balancesMenu);
    }
    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        if (e.getSource() == testBalance) {
            testBalanceLauncher.showBalance(accounting.getJournals(), accounting.getAccounts(), accounting.getAccountTypes());
        } else{
            BusinessCollection<BusinessObject> balances = accounting.getBusinessObject(Balances.BALANCES);
            Balance balance = null;
            if (e.getSource() == yearBalance) {
                balance = (Balance)balances.getBusinessObject(Balances.YEAR_BALANCE);
            } else if (e.getSource() == resultBalance) {
                balance = (Balance)balances.getBusinessObject(Balances.RESULT_BALANCE);
            } else if (e.getSource() == relationsBalance) {
                balance = (Balance)balances.getBusinessObject(Balances.RELATIONS_BALANCE);
            }
            balanceLauncher.showBalance(accounting.getJournals(), balance);
        }
    }
    public void extendReadCollection(Accounting accounting, File xmlFolder){

    }

    public void extendWriteCollection(Accounting accounting, File xmlFolder){
    }
}
