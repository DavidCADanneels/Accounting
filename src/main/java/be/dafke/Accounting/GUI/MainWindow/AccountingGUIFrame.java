package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.RefreshableFrame;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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

	public AccountingGUIFrame(String title, Accountings accountings/*, JMenuBar menuBar*/) {
		super(title);
		this.accountings = accountings;
		addWindowListener(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//DO_NOTHING_ON_CLOSE);
        contentPanel = new AccountingGUIPanel(accountings);
		setContentPane(contentPanel);
        menuBar = new AccountingMenuBar(accountings, this);
		setJMenuBar(menuBar);
//		setJMenuBar(new AccountingMenuBar(accountings));
		pack();
		//setVisible(true);
	}

    @Override
	public void windowOpened(WindowEvent we) {
	}

    @Override
	public void windowClosing(WindowEvent we) {
		accountings.close();
        AccountingMenuBar.closeAllFrames();
//        dispose();
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
        contentPanel.refresh();
    }
}
