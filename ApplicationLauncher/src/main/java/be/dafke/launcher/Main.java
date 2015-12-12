package be.dafke.launcher;

import be.dafke.Balances.BalancesExtension;
import be.dafke.BasicAccounting.Actions.SaveAllActionListener;
import be.dafke.BasicAccounting.BasicAccountingMain;
import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountsGUI;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalGUI;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalsGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.Coda.CodaExtension;
import be.dafke.Mortgage.GUI.MortgagesGUI;
import be.dafke.Mortgage.MortgageExtension;

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
        startReadingXmlFile();
        createBasicComponents();

        applyExtensions();

        continueReadingXmlFile();
        composeContentPanel();
        composeFrames();
        launch();
    }

    protected static void applyExtensions(){
        new BalancesExtension(accountings, menuBar);
        new CodaExtension(accountings, menuBar);
        new MortgageExtension(accountings, menuBar);
    }

    protected static void createBasicComponents(){
        Accounting accounting = accountings.getCurrentObject();
        journalGUI = new JournalGUI(accounting);
        accountsGUILeft = new AccountsGUI(accounting.getAccounts(), accounting.getAccountTypes(),accounting.getJournals());
        accountsGUIRight = new AccountsGUI(accounting.getAccounts(), accounting.getAccountTypes(),accounting.getJournals());
        journalsGUI = new JournalsGUI(accountings);
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