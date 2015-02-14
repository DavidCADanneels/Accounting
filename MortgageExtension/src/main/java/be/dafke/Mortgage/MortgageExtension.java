package be.dafke.Mortgage;

import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Mortgage.Actions.ShowMortgagesActionListener;
import be.dafke.Mortgage.Dao.MortgagesSAXParser;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.Mortgage.Objects.Mortgages;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModelDao.ObjectModelSAXParser;

import javax.swing.*;
import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 16:09
 */
public class MortgageExtension implements AccountingExtension{
    private Mortgages mortgages;

    public static final String MORTGAGES = "Mortgages";
    public static final String MORTGAGE_CALCULATOR = "MortgageCalculator";
    public static final String MORTGAGE_TABLE = "MortgageTable";
    private static JMenu banking = null;

    public MortgageExtension(Accountings accountings, AccountingMenuBar menuBar){
        if(banking == null) createMenu(menuBar, accountings);
    }

    private void createMenu(AccountingMenuBar menuBar, Accountings accountings) {
        banking = new JMenu("Mortgage");
        JMenuItem mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(new ShowMortgagesActionListener(accountings));
        mortgage.setEnabled(false);
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
    public void extendReadCollection(Accounting accounting, File xmlFolder){
        File rootFolder = new File(xmlFolder, accounting.getName());
        for(BusinessObject businessObject : mortgages.getBusinessObjects()){
            Mortgage mortgage = (Mortgage) businessObject;
            File mortgagesFolder = new File(rootFolder, "Mortgages");
            MortgagesSAXParser.readMortgage(mortgage, new File(mortgagesFolder, mortgage.getName() + ".xml"));
        }
    }

    @Override
    public void extendWriteCollection(Accounting accounting, File xmlFolder){
        File mortgagesFolder = new File(xmlFolder, "Mortgages");
        File accountsFolder = new File(xmlFolder, "Accounts");
        BusinessCollection<BusinessObject> mortgages = accounting.getBusinessObject("Mortgages");
        for(BusinessObject businessObject : mortgages.getBusinessObjects()){
            Mortgage mortgage = (Mortgage) businessObject;
            MortgagesSAXParser.writeMortgage(mortgage, mortgagesFolder, accountsFolder, ObjectModelSAXParser.getXmlHeader(mortgage, 3));
        }
    }
}
