package be.dafke.Coda.GUI;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.GUI.BasicAccountingMain;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Coda.Action.CodaActionListener;
import be.dafke.Coda.Objects.CodaExtension;

import javax.swing.*;
import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 14:41
 */
public class CodaExtensionMain extends BasicAccountingMain {

    public static void main(String[] args) {
        File xmlFolder = getXmlFolder();
        Accountings accountings = new Accountings(xmlFolder);
        accountings.addExtension(new CodaExtension());
        getAccountings(accountings, xmlFolder);

        AccountingGUIFrame frame = getFrame(accountings);

        AccountingActionListener actionListener = new AccountingActionListener(accountings);
        AccountingMenuBar menuBar = createMenuBar(actionListener);

        createMenu(menuBar, actionListener);

        createComponents(actionListener);

        AccountingMultiPanel contentPanel = composePanel();

        completeFrame(accountings, frame, menuBar, contentPanel, actionListener);

        for(Accounting accounting : accountings.getBusinessObjects()){
            AccountingComponentMap.addDisposableComponent(accounting.toString() + CodaActionListener.MOVEMENTS, new StatementTable(accounting, actionListener));
            AccountingComponentMap.addDisposableComponent(accounting.toString() + CodaActionListener.COUNTERPARTIES, new CounterPartyTable(accounting, actionListener));
        }

        frame.setVisible(true);
        frame.refresh();
    }

    private static void createMenu(AccountingMenuBar menuBar, AccountingActionListener actionListener) {
        JMenu banking = new JMenu("Banking");
        JMenuItem movements = new JMenuItem("Show movements");
        movements.addActionListener(actionListener);
        movements.setActionCommand(CodaActionListener.MOVEMENTS);
        movements.setEnabled(false);
        JMenuItem counterParties = new JMenuItem("Show Counterparties");
        counterParties.addActionListener(actionListener);
        counterParties.setActionCommand(CodaActionListener.COUNTERPARTIES);
        counterParties.setEnabled(false);

        banking.add(movements);
        banking.add(counterParties);
        menuBar.addRefreshableMenuItem(movements);
        menuBar.addRefreshableMenuItem(counterParties);
        menuBar.add(banking);
    }
}
