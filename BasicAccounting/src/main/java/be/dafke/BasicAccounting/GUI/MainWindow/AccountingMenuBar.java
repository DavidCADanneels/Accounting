package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.RefreshableComponent;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class AccountingMenuBar extends JMenuBar implements RefreshableComponent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JMenu balances, projecten, file;
//    private final JMenu banking;
    private final JMenuItem testBalance, yearBalance, resultBalance, relationsBalance;
    private final JMenuItem projects;
    private final JMenuItem startNew;
    private List<JMenuItem> itemsToRefresh;
//    private final JMenuItem movements, counterParties;
//    private final JMenuItem mortgage;
    private final ActionListener actionListener;
    private boolean active = false;

    public AccountingMenuBar(ActionListener actionListener) {
        this.actionListener = actionListener;

        // Menu1
        file = new JMenu("Bestand");
        startNew = new JMenuItem("New");
        startNew.addActionListener(actionListener);
        startNew.setActionCommand(AccountingComponentMap.NEW_ACCOUNTING);
        add(file);


        // Menu2
        balances = new JMenu(getBundle("Accounting").getString("BALANSEN"));
        balances.setMnemonic(KeyEvent.VK_B);
        testBalance = new JMenuItem(getBundle("Accounting").getString(
                "PROEF_EN_SALDI-BALANS"));
        yearBalance = new JMenuItem(getBundle("Accounting").getString("EINDBALANS"));
        resultBalance = new JMenuItem(getBundle("Accounting").getString(
                "RESULTATENBALANS"));
        relationsBalance = new JMenuItem(getBundle("Accounting").getString(
                "RELATIES-BALANS"));
        testBalance.addActionListener(actionListener);
        yearBalance.addActionListener(actionListener);
        resultBalance.addActionListener(actionListener);
        relationsBalance.addActionListener(actionListener);
        relationsBalance.setEnabled(false);
        resultBalance.setEnabled(false);
        testBalance.setEnabled(false);
        yearBalance.setEnabled(false);
        balances.add(testBalance);
        balances.add(resultBalance);
        balances.add(yearBalance);
        balances.add(relationsBalance);
        add(balances);

        projecten = new JMenu(getBundle("Accounting").getString("PROJECTEN"));
        projecten.setMnemonic(KeyEvent.VK_P);
        projects = new JMenuItem(getBundle("Accounting").getString(
                "PROJECTMANAGER"));
        projects.addActionListener(actionListener);
        projects.setEnabled(false);
        projecten.add(projects);
        add(projecten);

//        banking = new JMenu("Banking");
//        movements = new JMenuItem("Show movements");
//        movements.addActionListener(actionListener);
//        movements.setEnabled(false);
//        counterParties = new JMenuItem("Show Counterparties");
//        counterParties.addActionListener(actionListener);
//        counterParties.setEnabled(false);
//        mortgage = new JMenuItem("Mortgages");
//        mortgage.addActionListener(actionListener);
//        mortgage.setEnabled(false);
//        banking.add(movements);
//        banking.add(counterParties);
//        banking.add(mortgage);
//        add(banking);
        itemsToRefresh = new ArrayList<JMenuItem>();
        itemsToRefresh.add(testBalance);
        itemsToRefresh.add(resultBalance);
        itemsToRefresh.add(yearBalance);
        itemsToRefresh.add(relationsBalance);
        itemsToRefresh.add(projects);
        setActionCommands();
    }

    public void addRefreshableMenuItem(JMenuItem item){
        itemsToRefresh.add(item);
    }

    @Override
    public void refresh(){
        for(JMenuItem item:itemsToRefresh){
            item.setEnabled(active);
        }
//        movements.setEnabled(active);
//        counterParties.setEnabled(active);
//        mortgage.setEnabled(active);
    }

    private void setActionCommands(){
        testBalance.setActionCommand(AccountingComponentMap.TEST_BALANCE);
        yearBalance.setActionCommand(AccountingComponentMap.YEAR_BALANCE);
        resultBalance.setActionCommand(AccountingComponentMap.RESULT_BALANCE);
        relationsBalance.setActionCommand(AccountingComponentMap.RELATIONS_BALANCE);
        projects.setActionCommand(AccountingComponentMap.PROJECTS);
//        movements.setActionCommand(AccountingComponentMap.MOVEMENTS);
//        counterParties.setActionCommand(AccountingComponentMap.COUNTERPARTIES);
//        mortgage.setActionCommand(AccountingComponentMap.MORTGAGES);
    }

    public void setAccounting(Accounting accounting, Accountings accountings) {
        active = accounting!=null;
        file.removeAll();
        file.add(startNew);
        for(Accounting acc : accountings.getBusinessObjects()) {
            if(acc!=accounting){
                JMenuItem item = new JMenuItem(acc.toString());
                item.addActionListener(actionListener);
                item.setActionCommand(AccountingComponentMap.OPEN_ACCOUNTING+acc.toString());
                file.add(item);
            }
        }
        refresh();
    }
}
