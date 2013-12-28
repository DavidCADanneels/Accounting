package be.dafke.Mortgage.GUI;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.GUI.BasicAccountingMain;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Mortgage.Action.MortgageActionListener;
import be.dafke.Mortgage.Objects.MortgageExtension;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 1:02
 */
public class MortgageExtensionMain extends BasicAccountingMain {
    protected static MortgagesGUI mortgagesGUI;

    public static void main(String[] args) {
        File xmlFolder = getXmlFolder();
        Accountings accountings = new Accountings(xmlFolder);
        accountings.addExtension(new MortgageExtension());
        getAccountings(accountings, xmlFolder);

        AccountingGUIFrame frame = getFrame(accountings);

        AccountingActionListener actionListener = new AccountingActionListener(accountings);
        AccountingMenuBar menuBar = createMenuBar(actionListener);

        MortgageActionListener mortgageActionListener = new MortgageActionListener(accountings);
        createMenu(menuBar, mortgageActionListener);

        createComponents(actionListener);
        mortgagesGUI = new MortgagesGUI();

        AccountingMultiPanel contentPanel = composePanel();

        completeFrame(accountings, frame, menuBar, contentPanel, actionListener);

        for(Accounting accounting : accountings.getBusinessObjects()){
            AccountingComponentMap.addDisposableComponent(accounting.toString() + MortgageActionListener.MORTGAGES, new MortgageGUI(accounting, mortgageActionListener));
        }

        frame.setVisible(true);
        frame.refresh();
    }

    private static void createMenu(AccountingMenuBar menuBar, MortgageActionListener actionListener) {
        JMenu banking = new JMenu("Mortgage");
        JMenuItem mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(actionListener);
        mortgage.setEnabled(false);
        mortgage.setActionCommand(MortgageActionListener.MORTGAGES);
        banking.add(mortgage);
        menuBar.addRefreshableMenuItem(mortgage);
        menuBar.add(banking);
    }

    public static AccountingMultiPanel composePanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUI);
        links.add(mortgagesGUI);
        links.add(journalsGUI);
        AccountingMultiPanel main = new AccountingMultiPanel();
        main.setLayout(new BorderLayout());
        main.add(journalGUI, BorderLayout.CENTER);
        main.add(links, BorderLayout.WEST);
        return main;
    }



}
