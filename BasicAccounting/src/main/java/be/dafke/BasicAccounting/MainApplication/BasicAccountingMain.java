package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BasicAccounting.SaveAllActionListener;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.BusinessModelDao.MortgagesSAXParser;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModelDao.XMLReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.io.File;

public class BasicAccountingMain {

    private static final String MAIN = "MainPanel";
    public static final String MORTGAGES = "Mortgages";
    protected static Accountings accountings;
    protected static File xmlFolder;
    protected static File xslFolder;
    protected static File htmlFolder;
    protected static AccountingMenuBar menuBar;
    protected static AccountingMultiPanel contentPanel;
    protected static AccountingGUIFrame frame;

    protected enum Mode{ PROD, TEST}

    protected static JButton saveButton;
    protected static JournalGUI journalGUI;
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
        menuBar = new AccountingMenuBar(accountings);
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
        Accounting accounting = accountings.getCurrentObject();
        journalGUI = new JournalGUI(accounting);
        accountsGUI = new AccountsGUI(accounting);
        journalsGUI = new JournalsGUI(accounting);
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
        frame = new AccountingGUIFrame(accountings);
        frame.setMenuBar(menuBar);
        frame.setContentPanel(contentPanel);
        frame.addWindowListener(new SaveAllActionListener(accountings));
        ComponentMap.addDisposableComponent(MAIN, frame); // MAIN
        ComponentMap.addRefreshableComponent(menuBar);
    }
    protected static void launch() {
        frame.setVisible(true);
        frame.refresh();
    }

    private static void setXmlFolder(){
        Mode mode = Mode.PROD;

        if(mode == Mode.TEST){
            int nr = JOptionPane.showOptionDialog(null,"TEST or PROD", "Which environment?",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null, Mode.values(),Mode.TEST);
            if(nr == 0){
                mode = Mode.PROD;
            } else {
                mode = Mode.TEST;
            }
        }

        if(mode == Mode.TEST) {
            xmlFolder = new File("BasicAccounting/src/test/resources/xml");
            xslFolder = new File("BasicAccounting/src/test/resources/xsl");
            htmlFolder = new File("BasicAccounting/src/test/resources/html");
        } else {// if (mode == Mode.PROD) {
//            File userHome = new File(System.getProperty("user.home"));
            File parentFolder = new File("data/Accounting");
            xmlFolder = new File(parentFolder, "xml");
            xslFolder = new File(parentFolder, "xsl");
            htmlFolder = new File(parentFolder, "html");
        }
        System.out.println(mode.toString());
        System.out.println(xmlFolder);
        System.out.println(htmlFolder);
    }
}
