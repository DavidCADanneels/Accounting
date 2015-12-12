package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Balance;
import be.dafke.BasicAccounting.Objects.Balances;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

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
            BalanceActions.showTestBalance(accounting.getJournals(), accounting.getAccounts(), accounting.getAccountTypes());
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
            BalanceActions.showBalance(accounting.getJournals(), balance);
        }
    }
}
