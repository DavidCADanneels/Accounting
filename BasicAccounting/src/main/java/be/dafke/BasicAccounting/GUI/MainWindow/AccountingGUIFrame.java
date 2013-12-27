package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.ComponentMap;
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
    private AccountingGUIPanel contentPanel;

	public AccountingGUIFrame(Accountings accountings) {
		super(getBundle("Accounting").getString("BOEKHOUDING"));
		this.accountings = accountings;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AccountingActionListener actionListener = new AccountingActionListener(accountings);
        addWindowListener(actionListener);
        contentPanel = new AccountingGUIPanel(actionListener);
		setContentPane(contentPanel);
        menuBar = new AccountingMenuBar(actionListener);
        setJMenuBar(menuBar);
        ComponentMap.addDisposableComponent(ComponentMap.MAIN, this); // MAIN
        ComponentMap.addRefreshableComponent(ComponentMap.MENU, menuBar);
        for(Accounting accounting : accountings.getBusinessObjects()){
            ComponentMap.addAccountingComponents(accounting, actionListener);
        }
        refresh();
    }

    @Override
    public void refresh() {
        Accounting accounting = accountings.getCurrentObject();
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("BOEKHOUDING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("BOEKHOUDING"));
        }
        menuBar.setAccounting(accounting, accountings);
        contentPanel.setAccounting(accounting);
        pack();
    }
}
