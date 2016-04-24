package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.SaveAllActionListener;
import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BusinessModel.Accounting;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Main extends BasicAccountingMain{

    protected static AccountsGUI accountsGUILeft;
    protected static AccountsGUI accountsGUIRight;

    public static void main(String[] args) {
        readXmlData();
        createBasicComponents();
        createMenu();
        composeContentPanel();
        composeFrames();
        launch();
    }

    protected static void createBasicComponents(){
        Accounting accounting = accountings.getCurrentObject();
        journalGUI = new JournalGUI(accounting);
        accountsGUILeft = new AccountsGUI(accounting);
        accountsGUIRight = new AccountsGUI(accounting);
        journalsGUI = new JournalsGUI(accounting);
        menuBar = new AccountingMenuBar(accountings);
        saveButton = new JButton("Save all");
        saveButton.addActionListener(new SaveAllActionListener(accountings));
    }

    protected static void composeContentPanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUILeft);
        links.add(accountsGUIRight);
        links.add(new MortgagesGUI());
        links.add(journalsGUI);
        links.add(saveButton);
        contentPanel = new AccountingMultiPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(journalGUI, BorderLayout.CENTER);
        contentPanel.add(links, BorderLayout.WEST);
    }
}