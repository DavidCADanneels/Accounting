package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.RefreshableFrame;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends RefreshableFrame implements AccountingPanelInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Accountings accountings;
    private AccountingMenuBar menuBar;
    private AccountingMultiPanel contentPanel;

	public AccountingGUIFrame(Accountings accountings) {
		super(getBundle("Accounting").getString("ACCOUNTING"));
		this.accountings = accountings;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void setContentPanel(AccountingMultiPanel contentPanel){
        this.contentPanel = contentPanel;
        setContentPane(contentPanel);
    }

    public void setMenuBar(AccountingMenuBar menuBar){
        this.menuBar = menuBar;
        setJMenuBar(menuBar);
    }

    public void setAccounting(Accounting accounting){
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("ACCOUNTING"));
        }
        menuBar.setAccounting(accounting);
        contentPanel.setAccounting(accounting);
    }

    public void refresh() {
        Accounting accounting = accountings.getCurrentObject();
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("ACCOUNTING"));
        }
        menuBar.refresh();
        contentPanel.refresh();
    }
}
