package be.dafke.Balances;

import be.dafke.Balances.Actions.BalanceActionListener;
import be.dafke.Balances.Actions.TestBalanceActionListener;
import be.dafke.Balances.Objects.Balances;
import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 11:03
 */
public class BalancesExtension implements AccountingExtension {
    private static JMenu balancesMenu = null;


    public BalancesExtension(Accountings accountings, AccountingMenuBar menuBar){
        if(balancesMenu == null) createMenu(menuBar, accountings);
        for(Accounting accounting: accountings.getBusinessObjects()) {
            new Balances(accounting);
            accounting.addExtension(this);
        }
    }

    private void createMenu(AccountingMenuBar menuBar, Accountings accountings) {
        balancesMenu = new JMenu(getBundle("Balances").getString("BALANSES"));
        balancesMenu.setMnemonic(KeyEvent.VK_B);
        JMenuItem testBalance = new JMenuItem(getBundle("Balances").getString(
                "TESTBALANCE"));
        JMenuItem yearBalance = new JMenuItem(getBundle("Balances").getString("YEARBALANCE"));
        JMenuItem resultBalance = new JMenuItem(getBundle("Balances").getString(
                "RESULTBALANCE"));
        JMenuItem relationsBalance = new JMenuItem(getBundle("Balances").getString(
                "RELATIONSBALANCE"));
        testBalance.addActionListener(new TestBalanceActionListener(accountings));
        yearBalance.addActionListener(new BalanceActionListener(accountings, Balances.YEAR_BALANCE));
        resultBalance.addActionListener(new BalanceActionListener(accountings, Balances.RESULT_BALANCE));
        relationsBalance.addActionListener(new BalanceActionListener(accountings, Balances.RELATIONS_BALANCE));
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

    public void extendReadCollection(Accounting accounting, File xmlFolder){

    }

    public void extendWriteCollection(Accounting accounting, File xmlFolder){
    }
}
