package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.SaveAllActionListener;

import javax.swing.*;
import java.awt.*;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Main extends BasicAccountingMain {

    public static void main(String[] args) {
        readXmlData();
        AccountingGUIFrame mainFrame = launchFrame("Accounting-all", createContentPanel(), createMenu());
        mainFrame.addWindowListener(new SaveAllActionListener(accountings));

        /*
        launchFrame("input",new JournalInputGUI(),null);
        launchFrame("input",new JournalGUI(accountings.getCurrentObject()),null);
        launchFrame("journals",new JournalsGUI(),null);
        launchFrame("acc1",new AccountsGUI(),null);
        launchFrame("acc2",new AccountsGUI(),null);
        */
    }

    public static AccountingMultiPanel createContentPanel(){

        JournalGUI journalReadGUI = new JournalGUI();
        JournalInputGUI journalInputGUI = new JournalInputGUI();

        JournalsGUI journalsGUI = new JournalsGUI();
        journalsGUI.addSetJournalListener(journalReadGUI);
        journalsGUI.addSetJournalListener(journalInputGUI);

        AccountsGUI accountsPanel = new AccountsGUI();
        accountsPanel.addAddBookingLister(journalInputGUI);

        AccountsTableGUI accountsGUIPanel = new AccountsTableGUI();
        accountsGUIPanel.addAddBookingLister(journalInputGUI);

        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
//        links.add(new AccountsGUI());
        links.add(accountsPanel);
        links.add(accountsGUIPanel);
        links.add(new MortgagesGUI());
        links.add(journalsGUI);
        links.add(createSaveButton());

        AccountingMultiPanel accountingMultiPanel = new AccountingMultiPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        JSplitPane splitPane = new AccountingSplitPanel(journalReadGUI, journalInputGUI, JSplitPane.VERTICAL_SPLIT);
//        splitPane.add(new JournalGUI(accounting), JSplitPane.TOP);
//        splitPane.add(new JournalInputGUI(), JSplitPane.BOTTOM);
        accountingMultiPanel.add(splitPane, BorderLayout.CENTER);
        accountingMultiPanel.add(links, BorderLayout.WEST);
        return accountingMultiPanel;
    }
}