package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Actions.BalancesMenu;
import be.dafke.BasicAccounting.Actions.SaveAllActionListener;
import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountsGUI;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalGUI;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalsGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModelDao.ObjectModelSAXParser;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

public class BasicAccountingMain {

    private static final String MAIN = "MainPanel";
    protected static Accountings accountings;
    protected static File xmlFolder;
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
        startReadingXmlFile();
        createBasicComponents();
        addBalances();
        continueReadingXmlFile();
        composeContentPanel();
        composeFrames();
        launch();
    }

    protected static void addBalances() {
        menuBar.add(new BalancesMenu(accountings, menuBar));
    }

    protected static void startReadingXmlFile() {
        setXmlFolder();
        accountings = new Accountings(xmlFolder, htmlFolder);
        if(!xmlFolder.exists()){
            xmlFolder.mkdirs();
        }
        File subFolder = new File(xmlFolder, Accountings.ACCOUNTINGS);
        if(!subFolder.exists()){
            subFolder.mkdir();
        }
        File file = new File(xmlFolder, "Accountings.xml");
        if(file.exists()){
            ObjectModelSAXParser.readCollection(accountings, false, xmlFolder);
        }
    }

    protected static void continueReadingXmlFile(){
        File subFolder = new File(xmlFolder, Accountings.ACCOUNTINGS);
        for(Accounting accounting : accountings.getBusinessObjects()){
            ObjectModelSAXParser.readCollection(accounting, true, subFolder);
        }

        for(Accounting accounting : accountings.getBusinessObjects()){
            List<AccountingExtension> extensions = accounting.getExtensions();
            for(AccountingExtension extension : extensions){
                extension.extendReadCollection(accounting,xmlFolder);
            }
        }
    }

    protected static void createBasicComponents(){
        Accounting accounting = accountings.getCurrentObject();
        journalGUI = new JournalGUI(accounting.getJournals(), accounting.getAccounts(), accounting.getAccountTypes());
        accountsGUI = new AccountsGUI(accounting.getAccounts(), accounting.getAccountTypes(),accounting.getJournals());
        journalsGUI = new JournalsGUI(accounting.getJournals(), accounting.getJournalTypes(), accounting.getAccountTypes());
        menuBar = new AccountingMenuBar(accountings);
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
            xmlFolder = new File("BasicAccounting/src/main/resources/xml");
            htmlFolder = new File("BasicAccounting/src/main/resources/html");
        } else {// if (mode == Mode.PROD) {
//            File userHome = new File(System.getProperty("user.home"));
            File userHome = new File("data");
            File parentFolder = new File(userHome, Accountings.ACCOUNTING);
            xmlFolder = new File(parentFolder, "xml");
            htmlFolder = new File(userHome, "AccountingHTML");
        }
        System.out.println(mode.toString());
        System.out.println(xmlFolder);
        System.out.println(htmlFolder);
    }
}
