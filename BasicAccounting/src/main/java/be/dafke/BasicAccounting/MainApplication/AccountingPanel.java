package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

/**
 * User: david
 * Date: 28-12-13
 * Time: 12:02
 */
public abstract class AccountingPanel extends JPanel implements AccountingPanelInterface{
    protected Accounting accounting;
    public void setAccounting(Accounting accounting){
        this.accounting=accounting;
    }
    public abstract void refresh();
}
