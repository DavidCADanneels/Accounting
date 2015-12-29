package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.GUIActions;
import be.dafke.BasicAccounting.MainApplication.AccountingMenuBar;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.BusinessModel.Balance;
import be.dafke.BusinessModel.Balances;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 11:03
 */
public class BalancesMenu extends JMenu implements ActionListener {
    private JMenuItem testBalance, yearBalance, resultBalance, relationsBalance;
    private final Accountings accountings;

    public BalancesMenu(Accountings accountings, AccountingMenuBar menuBar){
        super(getBundle("Balances").getString("BALANSES"));
        this.accountings = accountings;
        setMnemonic(KeyEvent.VK_B);
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
        add(testBalance);
        add(resultBalance);
        add(yearBalance);
        add(relationsBalance);
        menuBar.addRefreshableMenuItem(testBalance);
        menuBar.addRefreshableMenuItem(resultBalance);
        menuBar.addRefreshableMenuItem(yearBalance);
        menuBar.addRefreshableMenuItem(relationsBalance);
    }

    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        if (e.getSource() == testBalance) {
            GUIActions.showTestBalance(accounting.getJournals(), accounting.getAccounts(), accounting.getAccountTypes());
        } else{
            Balances balances = accounting.getBalances();
            Balance balance = null;
            if (e.getSource() == yearBalance) {
                balance = balances.getBusinessObject(Balances.YEAR_BALANCE);
            } else if (e.getSource() == resultBalance) {
                balance = balances.getBusinessObject(Balances.RESULT_BALANCE);
            } else if (e.getSource() == relationsBalance) {
                balance = balances.getBusinessObject(Balances.RELATIONS_BALANCE);
            }
            GUIActions.showBalance(accounting.getJournals(), balance);
        }
    }
}
