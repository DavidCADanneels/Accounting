package be.dafke.BasicAccounting.GUI;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.Dao.AccountingsSAXParser;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountsGUI;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalGUI;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalsGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.AccountingExtension;
import be.dafke.BasicAccounting.Objects.Accountings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BasicAccountingMain {

    protected static Accountings accountings;
    protected static File xmlFolder;
    protected static AccountingMenuBar menuBar;
    protected static AccountingActionListener actionListener;
    protected static AccountingMultiPanel contentPanel;
    protected static AccountingGUIFrame frame;

    protected enum Mode{ PROD, TEST}

    protected static JournalGUI journalGUI;
    protected static AccountsGUI accountsGUI;
    protected static JournalsGUI journalsGUI;


	public static void main(String[] args) {
//        doIt();
//    }
//
//    public static void doIt(){
        createAccountings();
        createComponents();
        extensions();
        getAccountings();
        getFrame();
        composePanel();
        completeFrame();
        launch();
    }

    protected static void launch() {
        frame.setVisible(true);
        frame.refresh();
    }

    protected static void extensions() {
        // Nothing here
    }

    protected static void createAccountings() {
        xmlFolder = getXmlFolder();
        accountings = new Accountings(xmlFolder);
    }


    protected static void createComponents() {
        actionListener = new AccountingActionListener(accountings);
        journalGUI = new JournalGUI();
        accountsGUI = new AccountsGUI(actionListener);
        journalsGUI = new JournalsGUI(actionListener);
        menuBar = createMenuBar(actionListener);
    }

    protected static void composePanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUI);
        links.add(journalsGUI);
        contentPanel = new AccountingMultiPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(journalGUI, BorderLayout.CENTER);
        contentPanel.add(links, BorderLayout.WEST);
    }

    protected static void completeFrame(){
        frame.setMenuBar(menuBar);
        frame.setContentPanel(contentPanel);
        frame.addWindowListener(actionListener);
        for(Accounting accounting : accountings.getBusinessObjects()){
            AccountingComponentMap.addAccountingComponents(accounting, actionListener);
        }
        AccountingComponentMap.addDisposableComponent(AccountingActionListener.MAIN, frame); // MAIN
        AccountingComponentMap.addRefreshableComponent(AccountingActionListener.MENU, menuBar);
        for(AccountingExtension extension : accountings.getExtensions()){
            extension.extendAccountingComponentMap(accountings);
        }
    }

    protected static AccountingMenuBar createMenuBar(AccountingActionListener actionListener){
        return new AccountingMenuBar(actionListener);
    }

    protected static File getXmlFolder(){
        Mode mode = Mode.TEST;

        File userHome = new File(System.getProperty("user.home"));
        File xmlFolder;
        if(mode == Mode.TEST){
            int nr = JOptionPane.showOptionDialog(null,"TEST or PROD", "Which environment?",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null, Mode.values(),Mode.TEST);
            if(nr == 0){
                mode = Mode.PROD;
            } else {
                mode = Mode.TEST;
            }
        }

        if(mode == Mode.TEST) {
            xmlFolder = new File(userHome, "workspace/Accounting/BasicAccounting/src/main/resources/xml");
        } else {// if (mode == Mode.PROD) {
            xmlFolder = new File(userHome, "Accounting");
        }
        System.out.println(mode.toString());
        System.out.println(xmlFolder);
        return xmlFolder;
    }

    protected static void getAccountings(){
        AccountingsSAXParser.readCollection(accountings, xmlFolder);
    }

    protected static void getFrame(){
        frame = new AccountingGUIFrame(accountings);
    }

}
