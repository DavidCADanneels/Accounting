package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.GUI.JournalManagement.NewJournalGUI;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.RefreshableComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author David Danneels
 */
public class AccountingMenuBar extends JMenuBar implements ActionListener, RefreshableComponent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JMenu balances, projecten, file, banking;
    private final JMenuItem testBalance, yearBalance, resultBalance, relationsBalance;
    private final JMenuItem projects;
    private final JMenuItem startNew;
    private final JMenuItem movements, counterParties, mortgage;
    private final Accountings accountings;

    public AccountingMenuBar(Accountings accountings) {
        this.accountings = accountings;

        // Menu1
        file = new JMenu("Bestand");
        startNew = new JMenuItem("New");
        startNew.addActionListener(this);
        file.add(startNew);

        for(Accounting acc : accountings.getAccountings()) {
            JMenuItem item = new JMenuItem(acc.toString());
            item.addActionListener(this);
            item.setActionCommand(acc.toString());
            file.add(item);
        }
        add(file);


        // Menu2
        balances = new JMenu(java.util.ResourceBundle.getBundle("Accounting").getString("BALANSEN"));
        balances.setMnemonic(KeyEvent.VK_B);
        testBalance = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
                "PROEF_EN_SALDI-BALANS"));
        yearBalance = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString("EINDBALANS"));
        resultBalance = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
                "RESULTATENBALANS"));
        relationsBalance = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
                "RELATIES-BALANS"));
        testBalance.addActionListener(this);
        yearBalance.addActionListener(this);
        resultBalance.addActionListener(this);
        relationsBalance.addActionListener(this);
        relationsBalance.setEnabled(false);
        resultBalance.setEnabled(false);
        testBalance.setEnabled(false);
        yearBalance.setEnabled(false);
        balances.add(testBalance);
        balances.add(resultBalance);
        balances.add(yearBalance);
        balances.add(relationsBalance);
        add(balances);

        projecten = new JMenu(java.util.ResourceBundle.getBundle("Accounting").getString("PROJECTEN"));
        projecten.setMnemonic(KeyEvent.VK_P);
        projects = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
                "PROJECTMANAGER"));
        projects.addActionListener(this);
        projects.setEnabled(false);
        projecten.add(projects);
        add(projecten);

        banking = new JMenu("Banking");
        movements = new JMenuItem("Show movements");
        movements.addActionListener(this);
        movements.setEnabled(false);
        counterParties = new JMenuItem("Show Counterparties");
        counterParties.addActionListener(this);
        counterParties.setEnabled(false);
        mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(this);
        mortgage.setEnabled(false);
        banking.add(movements);
        banking.add(counterParties);
        banking.add(mortgage);
        add(banking);
    }

    public void actionPerformed(ActionEvent ae) {
        JMenuItem item = (JMenuItem) ae.getSource();
        if (item == startNew) {
            String name = JOptionPane.showInputDialog(null, "Enter a name");
            try {
                Accounting accounting = accountings.addAccounting(name);
                accountings.setCurrentAccounting(name);
                ComponentMap.addAccountingComponents(accounting);
                JOptionPane.showMessageDialog(this, "Please create a Journal.");
                String title = "Create and modify journals for " + accounting.toString();
                NewJournalGUI gui = new NewJournalGUI(title, accounting);
                ComponentMap.addDisposableComponent(title, gui);
                gui.setVisible(true);
            } catch (DuplicateNameException e) {
                JOptionPane.showMessageDialog(this, "There is already an accounting with the name \""+name+"\".\r\n"+
                        "Please provide a new name.");
            } catch (EmptyNameException e) {
                JOptionPane.showMessageDialog(this, "The name cannot be empty.\r\nPlease provide a new name.");
            }
            ComponentMap.refreshAllFrames();
        } else if(accountings.contains(ae.getActionCommand())){
            // TODO: save currentAccounting ? --> make toXML(accounting) public
//            Accounting currentAccounting = accountings.getCurrentAccounting();
//            if(currentAccounting != null){
//                // TODO: ask user: save or not ?
//                AccountingSAXParser.toXML(currentAccounting);
//            }
            accountings.setCurrentAccounting(ae.getActionCommand());
        } else if(ae.getActionCommand().startsWith(accountings.getCurrentAccounting().toString())){
            RefreshableComponent gui = ComponentMap.getDisposableComponent(item.getActionCommand());
            if(gui != null){
                gui.setVisible(true);
            } else {
                System.err.println(ae.getActionCommand()+ " not supported");
            }
        }
        ComponentMap.refreshAllFrames();
    }

    @Override
    public void refresh(){
        activateButtons();
        setActionCommands();
    }


    private void activateButtons() {
        boolean active = accountings.isActive();
//		startNew.setEnabled(!active);
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
        boolean active = accountings.isActive();
        if(active){
            testBalance.setActionCommand(accountings.getCurrentAccounting().toString()+ComponentMap.OPEN_TEST_BALANCE);
            yearBalance.setActionCommand(accountings.getCurrentAccounting().toString() + ComponentMap.OPEN_YEAR_BALANCE);
            resultBalance.setActionCommand(accountings.getCurrentAccounting().toString() + ComponentMap.OPEN_RESULT_BALANCE);
            relationsBalance.setActionCommand(accountings.getCurrentAccounting().toString() + ComponentMap.OPEN_RELATIONS_BALANCE);
            projects.setActionCommand(accountings.getCurrentAccounting().toString()+ComponentMap.OPEN_PROJECTS);
            movements.setActionCommand(accountings.getCurrentAccounting().toString()+ComponentMap.OPEN_MOVEMENTS);
            counterParties.setActionCommand(accountings.getCurrentAccounting().toString()+ComponentMap.OPEN_COUNTERPARTIES);
            mortgage.setActionCommand(accountings.getCurrentAccounting().toString()+ComponentMap.OPEN_MORTGAGES);
        }
    }

}
