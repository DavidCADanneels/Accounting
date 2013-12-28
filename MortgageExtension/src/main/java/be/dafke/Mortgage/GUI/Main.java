package be.dafke.Mortgage.GUI;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.GUI.BasicAccounting;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.Mortgage.Action.MortgageActionListener;
import be.dafke.Mortgage.Dao.MortgagesSAXParser;
import be.dafke.Mortgage.Objects.AccountingsWithMortgage;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 1:02
 */
public class Main extends BasicAccounting {
    protected static MortgagesGUI mortgagesGUI;

    public static void main(String[] args) {
        File xmlFolder = getXmlFolder();
        AccountingsWithMortgage accountings = new AccountingsWithMortgage(xmlFolder);
        getAccountings(accountings, xmlFolder);

        for(Accounting accounting : accountings.getBusinessObjects()){
            File rootFolder = new File(xmlFolder, accounting.getName());
            BusinessCollection<BusinessObject> mortgages = accounting.getBusinessObject("Mortgages");
            for(BusinessObject businessObject : mortgages.getBusinessObjects()){
                Mortgage mortgage = (Mortgage) businessObject;
                File mortgagesFolder = new File(rootFolder, "Mortgages");
                MortgagesSAXParser.readMortgage(mortgage, new File(mortgagesFolder, mortgage.getName() + ".xml"));
            }

        }


//        for(Accounting accounting : accountings.getBusinessObjects()){
//            File rootFolder = new File(xmlFolder, accounting.getName());
//            Mortgages mortgages = ((AccountingWithMortgage)accounting).getMortgages();
//            for(Mortgage mortgage : mortgages.getBusinessObjects()){
////                Mortgage mortgage = (Mortgage) businessObject;
//                File mortgagesFolder = new File(rootFolder, "Mortgages");
//                MortgagesSAXParser.readMortgage(mortgage, new File(mortgagesFolder, mortgage.getName() + ".xml"));
//            }
//        }

        AccountingGUIFrame frame = getFrame(accountings);

        AccountingActionListener actionListener = new AccountingActionListener(accountings);
        AccountingMenuBar menuBar = createMenuBar(actionListener);

        MortgageActionListener mortgageActionListener = new MortgageActionListener(accountings);
        createMenu(menuBar, mortgageActionListener);

        createComponents(actionListener);
        mortgagesGUI = new MortgagesGUI();

        AccountingMultiPanel contentPanel = composePanel();

        completeFrame(accountings, frame, menuBar, contentPanel, actionListener);

        for(Accounting accounting : accountings.getBusinessObjects()){
            AccountingComponentMap.addDisposableComponent(accounting.toString() + MortgageActionListener.MORTGAGES, new MortgageGUI(accounting, mortgageActionListener));
        }

        frame.setVisible(true);
        frame.refresh();
    }

    private static void createMenu(AccountingMenuBar menuBar, MortgageActionListener actionListener) {
        JMenu banking = new JMenu("Banking");
//        movements = new JMenuItem("Show movements");
//        movements.addActionListener(actionListener);
//        movements.setEnabled(false);
//        counterParties = new JMenuItem("Show Counterparties");
//        counterParties.addActionListener(actionListener);
//        counterParties.setEnabled(false);
        JMenuItem mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(actionListener);
        mortgage.setEnabled(false);
        mortgage.setActionCommand(MortgageActionListener.MORTGAGES);
//        banking.add(movements);
//        banking.add(counterParties);
        banking.add(mortgage);
        menuBar.addRefreshableMenuItem(mortgage);
        menuBar.add(banking);
    }

    public static AccountingMultiPanel composePanel(){
        AccountingMultiPanel links = new AccountingMultiPanel();
        links.setLayout(new BoxLayout(links,BoxLayout.Y_AXIS));
        links.add(accountsGUI);
        links.add(mortgagesGUI);
        links.add(journalsGUI);
        AccountingMultiPanel main = new AccountingMultiPanel();
        main.setLayout(new BorderLayout());
        main.add(journalGUI, BorderLayout.CENTER);
        main.add(links, BorderLayout.WEST);
        return main;
    }



}
