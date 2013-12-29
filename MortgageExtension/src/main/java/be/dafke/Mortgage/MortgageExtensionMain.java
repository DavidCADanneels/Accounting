package be.dafke.Mortgage;

import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.GUI.BasicAccountingMain;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.Mortgage.GUI.MortgagesGUI;

import javax.swing.*;
import java.awt.*;

/**
 * User: david
 * Date: 28-12-13
 * Time: 1:02
 */
public class MortgageExtensionMain extends BasicAccountingMain {
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
        for(Accounting accounting: accountings.getBusinessObjects()){
            MortgageExtension mortgageExtension = new MortgageExtension(actionListener, menuBar);
            accounting.addExtension(mortgageExtension);
        }
    }

    protected static void composeContentPanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUI);
        links.add(new MortgagesGUI());
        links.add(journalsGUI);
        contentPanel = new AccountingMultiPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(journalGUI, BorderLayout.CENTER);
        contentPanel.add(links, BorderLayout.WEST);
    }
}
