package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
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
    private AccountingMenuBar menuBar;
    private AccountingPanel contentPanel;

	public AccountingGUIFrame(String title, AccountingPanel contentPanel) {
	    this(title);
        this.contentPanel=contentPanel;
        setContentPane(contentPanel);
    }
	public AccountingGUIFrame(String title) {
		super(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
        if(menuBar!=null) menuBar.setAccounting(accounting);
        if(contentPanel!=null) contentPanel.setAccounting(accounting);
    }

    public void refresh() {
        if(menuBar!=null) menuBar.refresh();
        if(contentPanel!=null) contentPanel.refresh();
    }
}
