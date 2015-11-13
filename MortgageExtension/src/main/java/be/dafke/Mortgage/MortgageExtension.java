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

    private static JMenu banking = null;

    public MortgageExtension(Accountings accountings, AccountingMenuBar menuBar){
        if(banking == null) createMenu(menuBar, accountings);
        for(Accounting accounting: accountings.getBusinessObjects()) {
            mortgages = new Mortgages(accounting);
            accounting.addExtension(this);
        }
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

    public void extendReadCollection(Accounting accounting, File xmlFolder){
        File rootFolder = new File(xmlFolder, accounting.getName());
        for(BusinessObject businessObject : mortgages.getBusinessObjects()){
            Mortgage mortgage = (Mortgage) businessObject;
            File mortgagesFolder = new File(rootFolder, "Mortgages");
            MortgagesSAXParser.readMortgage(mortgage, new File(mortgagesFolder, mortgage.getName() + ".xml"));
        }
    }

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
