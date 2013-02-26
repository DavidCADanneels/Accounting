package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Actions.AccountingActionListener;
import be.dafke.Accounting.Dao.XML.AccountingSAXParser;
import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.RefreshableFrame;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends RefreshableFrame implements WindowListener {
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
		addWindowListener(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        AccountingActionListener actionListener = new AccountingActionListener(accountings);
        contentPanel = new AccountingGUIPanel(accountings.getCurrentAccounting(), actionListener);
		setContentPane(contentPanel);
        menuBar = new AccountingMenuBar(accountings, actionListener);
        setJMenuBar(menuBar);
        ComponentMap.addDisposableComponent(ComponentMap.MAIN, this); // MAIN
        ComponentMap.addRefreshableComponent(ComponentMap.MENU, menuBar);
        for(Accounting accounting : accountings.getAccountings()){
            ComponentMap.addAccountingComponents(accounting);
        }
		pack();
        ComponentMap.refreshAllFrames();
	}

    @Override
	public void windowOpened(WindowEvent we) {
	}

    @Override
	public void windowClosing(WindowEvent we) {
        AccountingSAXParser.toXML(accountings);
        ComponentMap.closeAllFrames();
	}

    @Override
	public void windowClosed(WindowEvent we) {
	}

    @Override
	public void windowIconified(WindowEvent we) {
	}

    @Override
	public void windowDeiconified(WindowEvent we) {
	}

    @Override
	public void windowActivated(WindowEvent we) {
	}

    @Override
	public void windowDeactivated(WindowEvent we) {
	}

    @Override
    public void refresh() {
        Accounting accounting = accountings.getCurrentAccounting();
        if(accounting!=null){
            setTitle(getBundle("Accounting").getString("BOEKHOUDING") + ": " + accounting.toString());
        } else {
            setTitle(getBundle("Accounting").getString("BOEKHOUDING"));
        }
        contentPanel.setAccounting(accounting);
    }
}
