package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Dao.XML.AccountingSAXParser;
import be.dafke.Accounting.GUI.ComponentMap;
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
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//DO_NOTHING_ON_CLOSE);
        contentPanel = new AccountingGUIPanel(accountings);
		setContentPane(contentPanel);
        menuBar = new AccountingMenuBar(accountings);
        new ComponentMap(accountings, this, menuBar);
		setJMenuBar(menuBar);
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
        if(accountings!=null && accountings.getCurrentAccounting()!=null){
            setTitle(getBundle("Accounting").getString("BOEKHOUDING")+":"+accountings.getCurrentAccounting().toString());
        } else {
            setTitle(getBundle("Accounting").getString("BOEKHOUDING"));
        }
        contentPanel.refresh();
    }
}
