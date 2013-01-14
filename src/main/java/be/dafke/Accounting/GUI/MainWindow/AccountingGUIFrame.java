package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.RefreshEvent;
import org.springframework.context.ApplicationListener;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends JFrame implements WindowListener, ApplicationListener<RefreshEvent> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Accountings accountings;

	public AccountingGUIFrame(String title, Accountings accountings/*, JMenuBar menuBar*/) {
		super(title);
		this.accountings = accountings;
		addWindowListener(this);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setContentPane(new AccountingGUIPanel(accountings));
//		setJMenuBar(menuBar);
		setJMenuBar(new AccountingMenuBar(accountings));
		pack();
		setVisible(true);
	}

//	@Override
	public void refresh() {
		AccountingGUIPanel panel = (AccountingGUIPanel) getContentPane();
		panel.activateButtons(/*accounting != null*/);
	}

	public void windowOpened(WindowEvent we) {
	}

	public void windowClosing(WindowEvent we) {
		accountings.close();
		dispose();
	}

	public void windowClosed(WindowEvent we) {
	}

	public void windowIconified(WindowEvent we) {
	}

	public void windowDeiconified(WindowEvent we) {
	}

	public void windowActivated(WindowEvent we) {
	}

	public void windowDeactivated(WindowEvent we) {
	}

	public void onApplicationEvent(RefreshEvent arg0) {
		refresh();
	}
}
