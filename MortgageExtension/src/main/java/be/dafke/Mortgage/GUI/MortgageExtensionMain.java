package be.dafke.Mortgage.GUI;

import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.GUI.BasicAccountingMain;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.Mortgage.Objects.MortgageExtension;

import javax.swing.*;
import java.awt.*;

/**
 * User: david
 * Date: 28-12-13
 * Time: 1:02
 */
public class MortgageExtensionMain extends BasicAccountingMain {
    protected static MortgagesGUI mortgagesGUI;

    public static void main(String[] args) {
//        doIt();
        createAccountings();
        createComponents();
        readBasicXmlFile();
        extensions();
        readXmlFile();
        getFrame();
        composePanel();
        completeFrame();
        launch();
    }

    protected static void extensions(){
//        MortgageActionListener mortgageActionListener = new MortgageActionListener(accountings);
        for(Accounting accounting: accountings.getBusinessObjects()){
            MortgageExtension mortgageExtension = new MortgageExtension(actionListener, menuBar);
            accounting.addExtension(mortgageExtension);
//            mortgageExtension.extendConstructor(accounting);
        }
        mortgagesGUI = new MortgagesGUI();
//        mortgagesGUI.setMortgages(accountings.);
    }

    protected static void extensions2(){

    }

    protected static void composePanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUI);
        links.add(mortgagesGUI);
        links.add(journalsGUI);
        contentPanel = new AccountingMultiPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(journalGUI, BorderLayout.CENTER);
        contentPanel.add(links, BorderLayout.WEST);
    }
}
