package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BasicAccounting.SaveAllActionListener;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModelDao.XMLReader;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BasicAccountingMain {

    protected static Accountings accountings;
    private static File xmlFolder;
    private static File xslFolder;
    private static File htmlFolder;

	public static void main(String[] args) {
        readXmlData();
        AccountingGUIFrame mainFrame = launchFrame("Accounting-all", createContentPanel(), createMenu());
        mainFrame.addWindowListener(new SaveAllActionListener(accountings));
    }

    protected static AccountingGUIFrame launchFrame(String title, AccountingPanel contentPanel, AccountingMenuBar menuBar){
        AccountingGUIFrame frame = new AccountingGUIFrame(title,contentPanel);

        ComponentMap.addDisposableComponent(title, frame); // MAIN

        if(menuBar!=null) {
            menuBar.setParent(frame);
            frame.setMenuBar(menuBar);
        }

        frame.setVisible(true);
        frame.pack();
        frame.setAccounting(accountings.getCurrentObject());
        frame.refresh();
        return frame;
    }

    protected static AccountingMenuBar createMenu() {
        AccountingMenuBar menuBar = new AccountingMenuBar(accountings);
        menuBar.add(new BalancesMenu(accountings, menuBar));
        menuBar.add(new MorgagesMenu(accountings, menuBar));
        menuBar.add(new ProjectsMenu(accountings, menuBar));
        menuBar.add(new CodaMenu(accountings, menuBar));
        ComponentMap.addRefreshableComponent(menuBar);
        return menuBar;
    }

    public static AccountingMultiPanel createContentPanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(new AccountsGUI());
        links.add(new JournalsGUI());
        links.add(createSaveButton());

        AccountingMultiPanel accountingMultiPanel = new AccountingMultiPanel();
        accountingMultiPanel.setLayout(new BorderLayout());
        accountingMultiPanel.add(new JournalInputGUIListener(), BorderLayout.CENTER);
        accountingMultiPanel.add(links, BorderLayout.WEST);
        return accountingMultiPanel;
    }

    protected static JButton createSaveButton(){
        JButton saveButton = new JButton("Save all");
        saveButton.addActionListener(new SaveAllActionListener(accountings));
        return saveButton;
    }

    protected static void readXmlData() {
        setXmlFolder();
        accountings = new Accountings(xmlFolder, xslFolder, htmlFolder);
        if(!xmlFolder.exists()){
            xmlFolder.mkdirs();
        }
        File subFolder = new File(xmlFolder, Accountings.ACCOUNTINGS);
        if(!subFolder.exists()){
            subFolder.mkdir();
        }
        File file = new File(xmlFolder, "Accountings.xml");
        if(file.exists()){
            XMLReader.readCollection(accountings, xmlFolder);
        }
    }

    private static void setXmlFolder(){
//            File userHome = new File(System.getProperty("user.home"));
        File parentFolder = new File("data/Accounting");
        xmlFolder = new File(parentFolder, "xml");
        xslFolder = new File(parentFolder, "xsl");
        htmlFolder = new File(parentFolder, "html");
    }
}
