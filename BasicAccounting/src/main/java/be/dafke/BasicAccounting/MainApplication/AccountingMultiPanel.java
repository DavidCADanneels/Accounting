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
    private List<AccountingPanel> components;

    public AccountingMultiPanel(){
        components = new ArrayList<AccountingPanel>();
    }

    public Component add(AccountingPanel accountingPanel){
        super.add(accountingPanel);
        components.add(accountingPanel);
        return accountingPanel;
    }

    public void add(AccountingPanel accountingPanel, Object constraints){
        super.add(accountingPanel, constraints);
        components.add(accountingPanel);
    }


    public void refresh(){
        for(AccountingPanel component: components){
            component.refresh();
        }
    }

    public void setAccounting(Accounting accounting){
        for(AccountingPanel component: components){
            component.setAccounting(accounting);
        }
        refresh();
    }
}
