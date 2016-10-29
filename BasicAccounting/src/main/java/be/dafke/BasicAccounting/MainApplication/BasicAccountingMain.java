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

    private static final String MAIN = "MainPanel";
    protected static Accountings accountings;
    protected static File xmlFolder;
    protected static File xslFolder;
    protected static File htmlFolder;
    protected static AccountingMenuBar menuBar;
    protected static AccountingMultiPanel contentPanel;
    protected static AccountingGUIFrame frame;

    protected static JButton saveButton;
    private static JournalInputGUI journalGUI;
    protected static AccountsGUI accountsGUI;
    protected static JournalsGUI journalsGUI;


	public static void main(String[] args) {
        readXmlData();
        createBasicComponents();
        createMenu();
        composeContentPanel();
        composeFrames();
        launch();
    }

    protected static void createMenu() {
        frame = new AccountingGUIFrame(accountings);
        menuBar = new AccountingMenuBar(accountings,frame);
        menuBar.add(new BalancesMenu(accountings, menuBar));
        menuBar.add(new MorgagesMenu(accountings, menuBar));
        menuBar.add(new ProjectsMenu(accountings, menuBar));
        menuBar.add(new CodaMenu(accountings, menuBar));
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

    protected static void createBasicComponents(){
        journalGUI = new JournalInputGUI();
        accountsGUI = new AccountsGUI();
        journalsGUI = new JournalsGUI();
        saveButton = new JButton("Save all");
        saveButton.addActionListener(new SaveAllActionListener(accountings));
    }

    protected static void composeContentPanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUI);
        links.add(journalsGUI);
        links.add(saveButton);
        contentPanel = new AccountingMultiPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(journalGUI, BorderLayout.CENTER);
        contentPanel.add(links, BorderLayout.WEST);
    }

    protected static void composeFrames() {
        frame.setMenuBar(menuBar);
        frame.setContentPanel(contentPanel);
        frame.addWindowListener(new SaveAllActionListener(accountings));
        ComponentMap.addDisposableComponent(MAIN, frame); // MAIN
        ComponentMap.addRefreshableComponent(menuBar);
    }
    protected static void launch() {
        frame.setVisible(true);
        frame.setAccounting(accountings.getCurrentObject());
        frame.pack();
        frame.refresh();
    }

    private static void setXmlFolder(){
//            File userHome = new File(System.getProperty("user.home"));
        File parentFolder = new File("data/Accounting");
        xmlFolder = new File(parentFolder, "xml");
        xslFolder = new File(parentFolder, "xsl");
        htmlFolder = new File(parentFolder, "html");
    }
}
