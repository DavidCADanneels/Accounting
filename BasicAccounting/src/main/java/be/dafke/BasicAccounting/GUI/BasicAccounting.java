package be.dafke.BasicAccounting.GUI;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.Dao.AccountingsSAXParser;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountsGUI;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalGUI;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalsGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BasicAccounting {

    private enum Mode{ PROD, TEST}

    protected static JournalGUI journalGUI;
    protected static AccountsGUI accountsGUI;
    protected static JournalsGUI journalsGUI;

	public static void main(String[] args) {
        File xmlFolder = getXmlFolder();
        Accountings accountings = new Accountings(xmlFolder);
        getAccountings(accountings, xmlFolder);

        AccountingGUIFrame frame = getFrame(accountings);

        AccountingActionListener actionListener = new AccountingActionListener(accountings);
        AccountingMenuBar menuBar = createMenuBar(actionListener);

        createComponents(actionListener);

        AccountingMultiPanel contentPanel = composePanel();

        completeFrame(accountings, frame, menuBar, contentPanel, actionListener);

        frame.setVisible(true);
        frame.refresh();
    }

    public static void createComponents(AccountingActionListener actionListener) {
        journalGUI = new JournalGUI();
        accountsGUI = new AccountsGUI(actionListener);
        journalsGUI = new JournalsGUI(actionListener);
    }

    public static AccountingMultiPanel composePanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUI);
        links.add(journalsGUI);
        AccountingMultiPanel main = new AccountingMultiPanel();
        main.setLayout(new BorderLayout());
        main.add(journalGUI, BorderLayout.CENTER);
        main.add(links, BorderLayout.WEST);
        return main;
    }

    public static void completeFrame(Accountings accountings, AccountingGUIFrame frame, AccountingMenuBar menuBar, AccountingMultiPanel contentPanel, AccountingActionListener actionListener){
        frame.setMenuBar(menuBar);
        frame.setContentPanel(contentPanel);
        frame.addWindowListener(actionListener);
        for(Accounting accounting : accountings.getBusinessObjects()){
            AccountingComponentMap.addAccountingComponents(accounting, actionListener);
        }
        AccountingComponentMap.addDisposableComponent(AccountingActionListener.MAIN, frame); // MAIN
        AccountingComponentMap.addRefreshableComponent(AccountingActionListener.MENU, menuBar);
    }

    public static AccountingMenuBar createMenuBar(AccountingActionListener actionListener){
        return new AccountingMenuBar(actionListener);
    }

    public static File getXmlFolder(){
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

    public static Accountings getAccountings(Accountings accountings, File xmlFolder){
        AccountingsSAXParser.readCollection(accountings, xmlFolder);
        return accountings;
    }

    public static AccountingGUIFrame getFrame(Accountings accountings){
        return new AccountingGUIFrame(accountings);
    }

}
