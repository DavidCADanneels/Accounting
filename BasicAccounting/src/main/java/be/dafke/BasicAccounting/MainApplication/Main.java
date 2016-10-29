package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.SaveAllActionListener;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Main extends BasicAccountingMain{

    public static void main(String[] args) {
        readXmlData();
        frame = new AccountingGUIFrame(getBundle("Accounting").getString("ACCOUNTING"));
        createMenu(frame);
        
        createContentPanel();

        frame.setMenuBar(menuBar);
        frame.setContentPanel(contentPanel);
        frame.addWindowListener(new SaveAllActionListener(accountings));
        ComponentMap.addDisposableComponent(MAIN, frame); // MAIN
        ComponentMap.addRefreshableComponent(menuBar);

        frame.setVisible(true);
        frame.setAccounting(accountings.getCurrentObject());
        frame.pack();
        frame.refresh();
    }

    protected static AccountingMultiPanel createContentPanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(new AccountsGUI());
        links.add(new AccountsGUI());
        links.add(new MortgagesGUI());
        links.add(new JournalsGUI());
        JButton saveButton = new JButton("Save all");
        saveButton.addActionListener(new SaveAllActionListener(accountings));
        links.add(saveButton);

        AccountingMultiPanel accountingMultiPanel = new AccountingMultiPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        JSplitPane splitPane = new AccountingSplitPanel(new JournalGUI(accountings.getCurrentObject()), new JournalInputGUI(), JSplitPane.VERTICAL_SPLIT);
//        splitPane.add(new JournalGUI(accounting), JSplitPane.TOP);
//        splitPane.add(new JournalInputGUI(), JSplitPane.BOTTOM);
        accountingMultiPanel.add(splitPane, BorderLayout.CENTER);
        accountingMultiPanel.add(links, BorderLayout.WEST);
        return accountingMultiPanel;
    }
}