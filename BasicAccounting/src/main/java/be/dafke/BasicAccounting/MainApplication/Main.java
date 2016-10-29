package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.SaveAllActionListener;
import be.dafke.ComponentModel.ComponentMap;

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
        launchMainFrame("Accounting-all", createContentPanel());

//        launchInputFrame();
    }

    public static void launchInputFrame(){
        AccountingGUIFrame frame = new AccountingGUIFrame("input", new JournalInputGUI());
        ComponentMap.addDisposableComponent("Input-Only",frame); // INPUT

        frame.setVisible(true);
        frame.setAccounting(accountings.getCurrentObject());
        frame.pack();
        frame.refresh();
    }

    public static AccountingMultiPanel createContentPanel(){
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