package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.AccountManagement.NewAccountGUI;
import be.dafke.Accounting.GUI.Balances.RelationsBalance;
import be.dafke.Accounting.GUI.Balances.ResultBalance;
import be.dafke.Accounting.GUI.Balances.TestBalance;
import be.dafke.Accounting.GUI.Balances.YearBalance;
import be.dafke.Accounting.GUI.CodaManagement.CounterPartyTable;
import be.dafke.Accounting.GUI.CodaManagement.MovementTable;
import be.dafke.Accounting.GUI.JournalManagement.NewJournalGUI;
import be.dafke.Accounting.GUI.JournalManagement.NewJournalTypeGUI;
import be.dafke.Accounting.GUI.MortgageManagement.MortgageGUI;
import be.dafke.Accounting.GUI.Projects.ProjectManagerFrame;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.RefreshableFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author David Danneels
 */
public class AccountingMenuBar extends JMenuBar implements ActionListener {
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
    private static final HashMap<String, RefreshableFrame> frames = new HashMap<String, RefreshableFrame>();
    public static final String OPEN_TEST_BALANCE = "test";
    public static final String OPEN_YEAR_BALANCE = "year";
    public static final String OPEN_RELATIONS_BALANCE = "relations";
    public static final String OPEN_RESULT_BALANCE = "result";
    public static final String OPEN_MOVEMENTS = "movements";
    public static final String OPEN_COUNTERPARTIES = "counterparties";
    public static final String OPEN_MORTGAGES = "mortgages";
    public static final String OPEN_PROJECTS = "projects";
    public static final String NEW_ACCOUNT = "newAccounts";
    public static final String NEW_JOURNAL = "newJournal";
    public static final String NEW_JOURNAL_TYPE = "newJournalType";

    public AccountingMenuBar(Accountings accountings, AccountingGUIFrame mainPanel) {
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

//		frames = new HashMap<String, RefreshableFrame>();
        frames.put(OPEN_RELATIONS_BALANCE, new RelationsBalance(accountings));
        frames.put(OPEN_RESULT_BALANCE, new ResultBalance(accountings));
        frames.put(OPEN_TEST_BALANCE, new TestBalance(accountings));
        frames.put(OPEN_YEAR_BALANCE, new YearBalance(accountings));
        frames.put(OPEN_PROJECTS, new ProjectManagerFrame(accountings));
        frames.put(OPEN_MOVEMENTS, new MovementTable(accountings));
        frames.put(OPEN_COUNTERPARTIES, new CounterPartyTable(accountings));
        frames.put(OPEN_MORTGAGES, new MortgageGUI(accountings));
        frames.put(NEW_ACCOUNT, new NewAccountGUI(accountings));
        frames.put(NEW_JOURNAL, new NewJournalGUI(accountings));
        frames.put(NEW_JOURNAL_TYPE, new NewJournalTypeGUI(accountings));
        frames.put("MAIN",mainPanel);

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
        test.setActionCommand(OPEN_TEST_BALANCE);
        year.setActionCommand(OPEN_YEAR_BALANCE);
        result.setActionCommand(OPEN_RESULT_BALANCE);
        relate.setActionCommand(OPEN_RELATIONS_BALANCE);
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
        projects.setActionCommand(OPEN_PROJECTS);
        projecten.add(projects);
        add(projecten);

        banking = new JMenu("Banking");
        movements = new JMenuItem("Show movements");
        movements.setActionCommand(OPEN_MOVEMENTS);
        movements.addActionListener(this);
        // movements.setEnabled(false);
        counterParties = new JMenuItem("Show Counterparties");
        counterParties.setActionCommand(OPEN_COUNTERPARTIES);
        counterParties.addActionListener(this);

        mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(this);
        mortgage.setActionCommand(OPEN_MORTGAGES);
        banking.add(movements);
        banking.add(counterParties);
        banking.add(mortgage);
        add(banking);
        activateButtons();
        if(accountings.isActive()){
            refreshAllFrames();
        }
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
            refreshAllFrames();
        } else if (frames.containsKey(item.getActionCommand())) {
            frames.get(item.getActionCommand()).setVisible(true);
        } else if(accountings.contains(ae.getActionCommand())){
            // TODO: save currentAccounting ? --> make toXML(accounting) public
//            Accounting currentAccounting = accountings.getCurrentAccounting();
//            if(currentAccounting != null){
//                // TODO: ask user: save or not ?
//                AccountingSAXParser.toXML(currentAccounting);
//            }
            accountings.setCurrentAccounting(ae.getActionCommand());
        } else{
            System.err.println(ae.getActionCommand()+ " not supported");
        }
        activateButtons();
        if(accountings.isActive()){
            refreshAllFrames();
        }
    }

    public static void closeAllFrames(){
        Collection<RefreshableFrame> collection = frames.values();
        for(RefreshableFrame frame: collection){
            frame.dispose();
        }
    }

    public static RefreshableFrame getFrame(String name){
        return frames.get(name);
    }

    public static void refreshAllFrames(){
        Collection<RefreshableFrame> collection = frames.values();
        for(RefreshableFrame frame: collection){
            frame.refresh();
        }
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

    public static void addFrame(String key, RefreshableFrame frame) {
        frames.put(key,frame);
    }
}
