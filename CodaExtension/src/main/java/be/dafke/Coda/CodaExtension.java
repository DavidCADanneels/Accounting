package be.dafke.Coda;

import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.AccountingExtension;
import be.dafke.Coda.GUI.CounterPartyTable;
import be.dafke.Coda.GUI.StatementTable;
import be.dafke.Coda.Objects.CounterParties;
import be.dafke.Coda.Objects.Statements;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:22
 */
public class CodaExtension implements AccountingExtension{
    private static JMenu banking = null;
    private final ActionListener actionListener;
    private CounterParties counterParties;
    private Statements statements;
    public static final String MOVEMENTS = "Statements";
    public static final String COUNTERPARTIES = "Counterparties";

    public CodaExtension(ActionListener actionListener, AccountingMenuBar menuBar){
        this.actionListener = actionListener;
        if(banking == null){
            createMenu(menuBar, actionListener);
        }
    }

    private static void createMenu(AccountingMenuBar menuBar, ActionListener actionListener) {
        banking = new JMenu("Banking");
        JMenuItem movements = new JMenuItem("Show movements");
        movements.addActionListener(actionListener);
        movements.setActionCommand(MOVEMENTS);
        movements.setEnabled(false);
        JMenuItem counterParties = new JMenuItem("Show Counterparties");
        counterParties.addActionListener(actionListener);
        counterParties.setActionCommand(COUNTERPARTIES);
        counterParties.setEnabled(false);

        banking.add(movements);
        banking.add(counterParties);
        menuBar.addRefreshableMenuItem(movements);
        menuBar.addRefreshableMenuItem(counterParties);
        menuBar.add(banking);
    }

    @Override
    public void extendConstructor(Accounting accounting){
        counterParties = new CounterParties();
        counterParties.setName(counterParties.getBusinessObjectType());

        statements = new Statements();
        statements.setBusinessCollection(counterParties);
        statements.setName(statements.getBusinessObjectType());
        try {
            accounting.addBusinessObject((BusinessCollection)statements);
            accounting.addBusinessObject((BusinessCollection)counterParties);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(counterParties.getBusinessObjectType());
        accounting.addKey(statements.getBusinessObjectType());
    }

    @Override
    public void extendReadCollection(Accounting accounting, File xmlFolder){

    }

    @Override
    public void extendAccountingComponentMap(Accounting accounting){
        AccountingComponentMap.addDisposableComponent(accounting.toString() + MOVEMENTS, new StatementTable(accounting, statements, counterParties, actionListener));
        AccountingComponentMap.addDisposableComponent(accounting.toString() + COUNTERPARTIES, new CounterPartyTable(accounting, counterParties, statements, actionListener));
    }

    @Override
    public void extendWriteCollection(Accounting accounting, File xmlFolder){

    }
}
