package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.ComponentMap;
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
    private final JMenuItem test, year, result, relate;
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
        test = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
                "PROEF_EN_SALDI-BALANS"));
        year = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString("EINDBALANS"));
        result = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
                "RESULTATENBALANS"));
        relate = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
                "RELATIES-BALANS"));
        test.addActionListener(this);
        year.addActionListener(this);
        result.addActionListener(this);
        relate.addActionListener(this);
        test.setActionCommand(ComponentMap.OPEN_TEST_BALANCE);
        year.setActionCommand(ComponentMap.OPEN_YEAR_BALANCE);
        result.setActionCommand(ComponentMap.OPEN_RESULT_BALANCE);
        relate.setActionCommand(ComponentMap.OPEN_RELATIONS_BALANCE);
        relate.setEnabled(false);
        result.setEnabled(false);
        test.setEnabled(false);
        year.setEnabled(false);
        balances.add(test);
        balances.add(result);
        balances.add(year);
        balances.add(relate);
        add(balances);

        projecten = new JMenu(java.util.ResourceBundle.getBundle("Accounting").getString("PROJECTEN"));
        projecten.setMnemonic(KeyEvent.VK_P);
        projects = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
                "PROJECTMANAGER"));
        projects.addActionListener(this);
        projects.setEnabled(false);
        projects.setActionCommand(ComponentMap.OPEN_PROJECTS);
        projecten.add(projects);
        add(projecten);

        banking = new JMenu("Banking");
        movements = new JMenuItem("Show movements");
        movements.setActionCommand(ComponentMap.OPEN_MOVEMENTS);
        movements.addActionListener(this);
        // movements.setEnabled(false);
        counterParties = new JMenuItem("Show Counterparties");
        counterParties.setActionCommand(ComponentMap.OPEN_COUNTERPARTIES);
        counterParties.addActionListener(this);

        mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(this);
        mortgage.setActionCommand(ComponentMap.OPEN_MORTGAGES);
        banking.add(movements);
        banking.add(counterParties);
        banking.add(mortgage);
        add(banking);
    }

    public void actionPerformed(ActionEvent ae) {
        JMenuItem item = (JMenuItem) ae.getSource();
        if (item == startNew) {
            if(accountings.getCurrentAccounting()!=null){
                JOptionPane.showConfirmDialog(null, "Do you want to save the current Accounting");
                // TODO: do something with the answer
            }
            String name = JOptionPane.showInputDialog(null, "Enter a name");
            // TODO: catch the Cancel button
            while (name == null || accountings.contains(name) || name.trim().isEmpty()) {
                // TODO: split up in empty/null response and name already exists
                name = JOptionPane.showInputDialog(null, "This name is empty or already exists. Enter another name");
            }
            accountings.addAccounting(name);
            ComponentMap.refreshAllFrames();
        } else if(accountings.contains(ae.getActionCommand())){
            // TODO: save currentAccounting ? --> make toXML(accounting) public
//            Accounting currentAccounting = accountings.getCurrentAccounting();
//            if(currentAccounting != null){
//                // TODO: ask user: save or not ?
//                AccountingSAXParser.toXML(currentAccounting);
//            }
            accountings.setCurrentAccounting(ae.getActionCommand());
        } else {
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
    }


    private void activateButtons() {
        boolean active = accountings.isActive();
//		startNew.setEnabled(!active);
        projects.setEnabled(active);
        relate.setEnabled(active);
        result.setEnabled(active);
        test.setEnabled(active);
        year.setEnabled(active);
        // movements.setEnabled(active);
    }

}
