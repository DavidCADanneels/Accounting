package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: david
 * Date: 28-12-13
 * Time: 12:05
 */
public class AccountingMultiPanel extends AccountingPanel {
    private List<AccountingPanelInterface> components;

    public AccountingMultiPanel(){
        components = new ArrayList<AccountingPanelInterface>();
    }

    public Component add(AccountingPanel accountingPanel){
        super.add(accountingPanel);
        components.add(accountingPanel);
        return accountingPanel;
    }

    public void add(Component accountingPanel, Object constraints){
        super.add(accountingPanel, constraints);
        components.add((AccountingPanelInterface)accountingPanel);
    }


    public void refresh(){
        for(AccountingPanelInterface component: components){
            component.refresh();
        }
    }

    public void setAccounting(Accounting accounting){
        for(AccountingPanelInterface component: components){
            component.setAccounting(accounting);
        }
        refresh();
    }
}