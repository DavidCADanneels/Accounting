package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.GUI.AccountingMultiPanel;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.RefreshableFrame;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends RefreshableFrame {
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

    @Override
    public void refresh() {
        Accounting accounting = accountings.getCurrentObject();
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("ACCOUNTING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("ACCOUNTING"));
        }
        menuBar.setAccounting(accounting);
        contentPanel.setAccounting(accounting);
        pack();
    }
}
