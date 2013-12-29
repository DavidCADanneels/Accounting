package be.dafke.Mortgage.Objects;

import be.dafke.BasicAccounting.Dao.AccountingsSAXParser;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.AccountingExtension;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Mortgage.Action.MortgageActionListener;
import be.dafke.Mortgage.Dao.MortgagesSAXParser;
import be.dafke.Mortgage.GUI.MortgageCalculatorGUI;
import be.dafke.Mortgage.GUI.MortgageGUI;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:09
 */
public class MortgageExtension implements AccountingExtension{
    private final ActionListener actionListener;
    private Mortgages mortgages;

    public MortgageExtension(ActionListener actionListener, AccountingMenuBar menuBar){
        this.actionListener = actionListener;
        createMenu(menuBar, actionListener);
    }

    private static void createMenu(AccountingMenuBar menuBar, ActionListener actionListener) {
        JMenu banking = new JMenu("Mortgage");
        JMenuItem mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(actionListener);
        mortgage.setEnabled(false);
        mortgage.setActionCommand(MortgageActionListener.MORTGAGES);
        banking.add(mortgage);
        menuBar.addRefreshableMenuItem(mortgage);
        menuBar.add(banking);
    }
    @Override
    public void extendConstructor(Accounting accounting){
        mortgages = new Mortgages();
        mortgages.setBusinessTypeCollection(accounting.getAccountTypes());
        mortgages.setBusinessCollection(accounting.getAccounts());
        mortgages.setName(mortgages.getBusinessObjectType());
        try{
            accounting.addBusinessObject((BusinessCollection) mortgages);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(mortgages.getBusinessObjectType());
    }

    @Override
    public void extendReadCollection(Accountings accountings, File xmlFolder){
        for(Accounting accounting : accountings.getBusinessObjects()){
            File rootFolder = new File(xmlFolder, accounting.getName());
            for(BusinessObject businessObject : mortgages.getBusinessObjects()){
                Mortgage mortgage = (Mortgage) businessObject;
                File mortgagesFolder = new File(rootFolder, "Mortgages");
                MortgagesSAXParser.readMortgage(mortgage, new File(mortgagesFolder, mortgage.getName() + ".xml"));
            }
        }
    }

    @Override
    public void extendAccountingComponentMap(Accountings accountings){
        for(Accounting accounting : accountings.getBusinessObjects()){
            AccountingComponentMap.addDisposableComponent(accounting.toString() + MortgageActionListener.MORTGAGES, new MortgageGUI(accounting, mortgages, actionListener));
            AccountingComponentMap.addDisposableComponent(accounting.toString() + MortgageActionListener.MORTGAGE_CALCULATOR, new MortgageCalculatorGUI(accounting, mortgages));
        }
    }

    @Override
    public void extendClosing(Accountings accountings){
        File xmlFolder = accountings.getXmlFolder();
        for(Accounting accounting : accountings.getBusinessObjects()) {
            File rootFolder = new File(xmlFolder, accounting.getName());
            File mortgagesFolder = new File(rootFolder, "Mortgages");
            BusinessCollection<BusinessObject> mortgages = accounting.getBusinessObject("Mortgages");
            for(BusinessObject businessObject : mortgages.getBusinessObjects()){
                Mortgage mortgage = (Mortgage) businessObject;
                MortgagesSAXParser.writeMortgage(mortgage, mortgagesFolder, AccountingsSAXParser.getXmlHeader(mortgage, 2));
            }
        }

    }
}
