package be.dafke.launcher;

import be.dafke.Balances.BalancesExtension;
import be.dafke.BasicAccounting.BasicAccountingMain;
import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.Coda.CodaExtension;
import be.dafke.Mortgage.GUI.MortgagesGUI;
import be.dafke.Mortgage.MortgageExtension;

import javax.swing.*;
import java.awt.*;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Main extends BasicAccountingMain{
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

    protected static void composeContentPanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUI);
        links.add(new MortgagesGUI());
        links.add(journalsGUI);
        links.add(saveButton);
        contentPanel = new AccountingMultiPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(journalGUI, BorderLayout.CENTER);
        contentPanel.add(links, BorderLayout.WEST);
    }
}