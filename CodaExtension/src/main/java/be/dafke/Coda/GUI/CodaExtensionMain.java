package be.dafke.Coda.GUI;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.GUI.BasicAccounting;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Coda.Objects.CodaExtension;

import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 14:41
 */
public class CodaExtensionMain extends BasicAccounting{

    public static void main(String[] args) {
        File xmlFolder = getXmlFolder();
        Accountings accountings = new Accountings(xmlFolder);
        accountings.addExtension(new CodaExtension());
        getAccountings(accountings, xmlFolder);

        AccountingGUIFrame frame = getFrame(accountings);

        AccountingActionListener actionListener = new AccountingActionListener(accountings);
        AccountingMenuBar menuBar = createMenuBar(actionListener);

        createComponents(actionListener);

        AccountingMultiPanel contentPanel = composePanel();

        completeFrame(accountings, frame, menuBar, contentPanel, actionListener);

        for(Accounting accounting : accountings.getBusinessObjects()){
            AccountingComponentMap.addDisposableComponent(accounting.toString() + AccountingActionListener.MOVEMENTS, new StatementTable(accounting, actionListener));
            AccountingComponentMap.addDisposableComponent(accounting.toString() + AccountingActionListener.COUNTERPARTIES, new CounterPartyTable(accounting, actionListener));
        }

        frame.setVisible(true);
        frame.refresh();
    }
}
