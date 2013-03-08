package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.RefreshableComponent;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class AccountingMenuBar extends JMenuBar implements RefreshableComponent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JMenu balances, projecten, file, banking;
    private final JMenuItem testBalance, yearBalance, resultBalance, relationsBalance;
    private final JMenuItem projects;
    private final JMenuItem startNew;
    private final JMenuItem movements, counterParties, mortgage;
    private final ActionListener actionListener;
    private boolean active = false;

    public AccountingMenuBar(ActionListener actionListener) {
        this.actionListener = actionListener;

        // Menu1
        file = new JMenu("Bestand");
        startNew = new JMenuItem("New");
        startNew.addActionListener(actionListener);
        startNew.setActionCommand(ComponentMap.NEW_ACCOUNTING);
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

        banking = new JMenu("Banking");
        movements = new JMenuItem("Show movements");
        movements.addActionListener(actionListener);
        movements.setEnabled(false);
        counterParties = new JMenuItem("Show Counterparties");
        counterParties.addActionListener(actionListener);
        counterParties.setEnabled(false);
        mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(actionListener);
        mortgage.setEnabled(false);
        banking.add(movements);
        banking.add(counterParties);
        banking.add(mortgage);
        add(banking);
        setActionCommands();
    }

    @Override
    public void refresh(){
        projects.setEnabled(active);
        relationsBalance.setEnabled(active);
        resultBalance.setEnabled(active);
        testBalance.setEnabled(active);
        yearBalance.setEnabled(active);
        movements.setEnabled(active);
        counterParties.setEnabled(active);
        mortgage.setEnabled(active);
    }

    private void setActionCommands(){
        testBalance.setActionCommand(ComponentMap.TEST_BALANCE);
        yearBalance.setActionCommand(ComponentMap.YEAR_BALANCE);
        resultBalance.setActionCommand(ComponentMap.RESULT_BALANCE);
        relationsBalance.setActionCommand(ComponentMap.RELATIONS_BALANCE);
        projects.setActionCommand(ComponentMap.PROJECTS);
        movements.setActionCommand(ComponentMap.MOVEMENTS);
        counterParties.setActionCommand(ComponentMap.COUNTERPARTIES);
        mortgage.setActionCommand(ComponentMap.MORTGAGES);
    }

    public void setAccounting(Accounting accounting, Accountings accountings) {
        active = accounting!=null;
        file.removeAll();
        file.add(startNew);
        for(Accounting acc : accountings.getBusinessObjects()) {
            if(acc!=accounting){
                JMenuItem item = new JMenuItem(acc.toString());
                item.addActionListener(actionListener);
                item.setActionCommand(ComponentMap.OPEN_ACCOUNTING+acc.toString());
                file.add(item);
            }
        }
        refresh();
    }
}
