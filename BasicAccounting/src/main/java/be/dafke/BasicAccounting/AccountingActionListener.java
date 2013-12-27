package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Dao.AccountingsSAXParser;
import be.dafke.BasicAccounting.GUI.AccountManagement.NewAccountGUI;
import be.dafke.BasicAccounting.GUI.ComponentMap;
import be.dafke.BasicAccounting.GUI.Details.AccountDetails;
import be.dafke.BasicAccounting.GUI.Details.JournalDetails;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.Mortgage.GUI.MortgageCalculatorGUI;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

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
            new NewAccountGUI(accountings.getCurrentObject()).setVisible(true);
        } else if (actionCommand.equals(ComponentMap.NEW_ACCOUNTING)) {
            String name = JOptionPane.showInputDialog(null, "Enter a name");
            try {
                Accounting accounting = new Accounting(name);
//                accounting.setName(name);
                accountings.addBusinessObject(accounting);
                accountings.setCurrentObject(name);
                accounting.getBalances().addDefaultBalances(accounting);
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
            accountings.setCurrentObject(accountingName);
        } else if(actionCommand.equals(ComponentMap.JOURNAL_DETAILS)){
            Accounting accounting = accountings.getCurrentObject();
            Journal journal = accounting.getJournals().getCurrentObject();
            String key = ComponentMap.JOURNAL_DETAILS + accounting.toString() + journal.toString();
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if(gui == null){
                gui = new JournalDetails(journal, accounting);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        } else if(actionCommand.equals(ComponentMap.ACCOUNT_DETAILS)){
            Accounting accounting = accountings.getCurrentObject();
            Account account = accounting.getAccounts().getCurrentObject();
            String key = accounting.toString() + ComponentMap.ACCOUNT_DETAILS + account.getName();
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if(gui == null){
                gui = new AccountDetails(account, accounting);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        } else if(actionCommand.equals(ComponentMap.MORTGAGE_CALCULATOR)){
            MortgageCalculatorGUI gui = new MortgageCalculatorGUI(accountings.getCurrentObject());
            ComponentMap.addDisposableComponent(ComponentMap.MORTGAGE_CALCULATOR +gui.getNr(), gui);
            gui.setVisible(true);
        } else {
            String key = accountings.getCurrentObject().toString() + actionCommand;
            ComponentMap.getDisposableComponent(key).setVisible(true);
        }
        ComponentMap.refreshAllFrames();
    }
}
