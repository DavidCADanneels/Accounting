package be.dafke.Mortgage.Action;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.Dao.AccountingsSAXParser;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Mortgage.Dao.MortgagesSAXParser;
import be.dafke.Mortgage.GUI.MortgageCalculatorGUI;
import be.dafke.Mortgage.GUI.MortgageComponentMap;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * User: david
 * Date: 28-12-13
 * Time: 1:13
 */
public class MortgageActionListener extends AccountingActionListener{

    private final Accountings accountings;

    public MortgageActionListener(Accountings accountings){
        super(accountings);
        this.accountings=accountings;
    }

    @Override
    public void windowClosing(WindowEvent we) {
        AccountingsSAXParser.writeAccountings(accountings);
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
        AccountingComponentMap.closeAllFrames();
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        String actionCommand = ae.getActionCommand();
        if(actionCommand.equals(MortgageComponentMap.MORTGAGE_CALCULATOR)){
            MortgageCalculatorGUI gui = new MortgageCalculatorGUI(accountings.getCurrentObject());
            AccountingComponentMap.addDisposableComponent(MortgageComponentMap.MORTGAGE_CALCULATOR + gui.getNr(), gui);
            gui.setVisible(true);
        } else {
            String key = accountings.getCurrentObject().toString() + actionCommand;
            AccountingComponentMap.getDisposableComponent(key).setVisible(true);
        }
        AccountingComponentMap.refreshAllFrames();
    }
}
