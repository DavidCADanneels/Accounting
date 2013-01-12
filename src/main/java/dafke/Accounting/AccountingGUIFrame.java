package be.dafke.Accounting;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.WindowConstants;

import be.dafke.ParentFrame;
import be.dafke.Accounting.Objects.Accountings;

/**
 * @author David Danneels
 */

public class AccountingGUIFrame extends ParentFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final AccountingMenuBar menuBar;

//	private Accounting accounting = null;
//	private final HashMap<String, Accounting> accountings;

	public AccountingGUIFrame(String title) {
		super(title);
//		accountings = new HashMap<String, Accounting>();
		Accountings.fromXML();
		addWindowListener(this);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setContentPane(new AccountingGUIPanel(this));
		menuBar = new AccountingMenuBar(this);
		setJMenuBar(menuBar);
		pack();
		setVisible(true);
	}

//	public void addAccounting(Accounting accounting) {
//		accountings.put(accounting.toString(), accounting);
//	}
//
//	public boolean contains(String name) {
//		return accountings.containsKey(name);
//	}
//
//	public Collection<Accounting> getAccountings() {
//		return accountings.values();
//	}

	@Override
	public void repaintAllFrames() {
		super.repaintAllFrames();
		refresh();
	}

	public void refresh() {
		AccountingGUIPanel panel = (AccountingGUIPanel) getContentPane();
		panel.activateButtons(/*accounting != null*/);
	}

//	public Accounting getAccounting() {
//		return accounting;
//	}

	public static void main(String[] args) {
		new AccountingGUIFrame(
				java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("BOEKHOUDING"));
	}

	@Override
	public void windowOpened(WindowEvent we) {
	}

	@Override
	public void windowClosing(WindowEvent we) {
		Accountings.close();
		closeAllFrames();
		dispose();
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
}
