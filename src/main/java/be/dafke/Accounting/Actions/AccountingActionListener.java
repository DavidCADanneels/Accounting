package be.dafke.Accounting.Actions;

import be.dafke.Accounting.Dao.XML.AccountingsSAXParser;
import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.GUI.AccountManagement.NewAccountGUI;
import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.GUI.Details.AccountDetails;
import be.dafke.Accounting.GUI.Details.JournalDetails;
import be.dafke.Accounting.GUI.MortgageManagement.MortgageCalculatorGUI;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.DisposableComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: Dafke
 * Date: 26/02/13
 * Time: 6:36
 */
public class AccountingActionListener extends WindowAdapter implements ActionListener {

    private final Accountings accountings;

    public AccountingActionListener(Accountings accountings){
        this.accountings = accountings;
    }

    @Override
    public void windowClosing(WindowEvent we) {
        AccountingsSAXParser.writeAccountings(accountings);
        ComponentMap.closeAllFrames();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if (actionCommand.equals(ComponentMap.NEW_ACCOUNT)){
            new NewAccountGUI(accountings.getCurrentAccounting()).setVisible(true);
        } else if (actionCommand.equals(ComponentMap.NEW_ACCOUNTING)) {
            String name = JOptionPane.showInputDialog(null, "Enter a name");
            try {
                Accounting accounting = accountings.addAccounting(name);
                accountings.setCurrentAccounting(name);
                ComponentMap.addAccountingComponents(accounting, this);
                JOptionPane.showMessageDialog(null, "Please create a Journal.");
                String key = accounting.toString()+ComponentMap.JOURNAL_MANAGEMENT;
                ComponentMap.getDisposableComponent(key).setVisible(true);
            } catch (DuplicateNameException e) {
                JOptionPane.showMessageDialog(null, "There is already an accounting with the name \""+name+"\".\r\n"+
                        "Please provide a new name.");
            } catch (EmptyNameException e) {
                JOptionPane.showMessageDialog(null, "The name cannot be empty.\r\nPlease provide a new name.");
            }
            ComponentMap.refreshAllFrames();
        } else if(actionCommand.startsWith(ComponentMap.OPEN_ACCOUNTING)){
            String accountingName = actionCommand.replaceAll(ComponentMap.OPEN_ACCOUNTING, "");
            accountings.setCurrentAccounting(accountingName);
        } else if(actionCommand.equals(ComponentMap.JOURNAL_DETAILS)){
            Accounting accounting = accountings.getCurrentAccounting();
            Journal journal = accounting.getJournals().getCurrentJournal();
            String key = ComponentMap.JOURNAL_DETAILS + accounting.toString() + journal.toString();
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if(gui == null){
                gui = new JournalDetails(journal, accounting);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        } else if(actionCommand.equals(ComponentMap.ACCOUNT_DETAILS)){
            Accounting accounting = accountings.getCurrentAccounting();
            Account account = accounting.getJournals().getCurrentJournal().getCurrentAccount();
            String key = accounting.toString() + ComponentMap.ACCOUNT_DETAILS + account.getName();
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if(gui == null){
                gui = new AccountDetails(account, accounting);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        } else if(actionCommand.equals(ComponentMap.MORTGAGE_CALCULATOR)){
            MortgageCalculatorGUI gui = new MortgageCalculatorGUI(accountings.getCurrentAccounting());
            ComponentMap.addDisposableComponent(ComponentMap.MORTGAGE_CALCULATOR +gui.getNr(), gui);
            gui.setVisible(true);
        } else {
            String key = accountings.getCurrentAccounting().toString() + actionCommand;
            ComponentMap.getDisposableComponent(key).setVisible(true);
        }
        ComponentMap.refreshAllFrames();
    }
}
