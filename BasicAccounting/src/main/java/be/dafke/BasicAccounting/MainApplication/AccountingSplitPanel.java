package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

/**
 * User: david
 * Date: 28-12-13
 * Time: 12:05
 */
public class AccountingSplitPanel extends JSplitPane implements AccountingPanelInterface {
    private AccountingPanel panel1, panel2;

    public AccountingSplitPanel(AccountingPanel panel1, AccountingPanel panel2,int orientation){
        super(orientation);
        this.panel1 = panel1;
        this.panel2 = panel2;
        if(orientation == JSplitPane.VERTICAL_SPLIT){
            super.add(panel1,TOP);
            super.add(panel2,BOTTOM);
        } else {
            super.add(panel1,LEFT);
            super.add(panel2,RIGHT);
        }
    }

    public void refresh(){
        panel1.refresh();
        panel2.refresh();
    }

    public void setAccounting(Accounting accounting){
        panel1.setAccounting(accounting);
        panel2.setAccounting(accounting);
        refresh();
    }
}
