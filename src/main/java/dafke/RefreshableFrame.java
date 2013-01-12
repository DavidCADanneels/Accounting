package be.dafke;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Abstracte klasse, bevat een abstracte functie refresh() die alle andere Refreshable frame kan laten hertekenen
 * @author David Danneels
 * @since 01/10/2010
 * @see RefreshableFrame#windowClosing(java.awt.event.WindowEvent) windowClosing()
 * @see RefreshableFrame#refresh() refresh()
 */
public abstract class RefreshableFrame extends JFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ParentFrame parent;

	/**
	 * @param title de titel van het JFrame
	 */
	public RefreshableFrame(String title, ParentFrame parent) {
		super(title);
		this.parent = parent;
		addWindowListener(this);
	}

	/**
	 * doet niets
	 * @param we het event
	 */
	@Override
	public void windowOpened(WindowEvent we) {
	}

	/**
	 * doet het JFrame verdwijnen en verwijdert zichzelf uit de lijst met refreshable frames
	 * @param we het event
	 * @see be.dafke.Accounting.AccountingGUIFrame#frames AccountingGUIWindow.frames
	 */
	@Override
	public void windowClosing(WindowEvent we) {
		dispose();
		parent.closeChildFrame(this);
	}

	/**
	 * doet niets
	 * @param we het event
	 */
	@Override
	public void windowClosed(WindowEvent we) {
	}

	/**
	 * doet niets
	 * @param we het event
	 */
	@Override
	public void windowIconified(WindowEvent we) {
	}

	/**
	 * doet niets
	 * @param we het event
	 */
	@Override
	public void windowDeiconified(WindowEvent we) {
	}

	/**
	 * doet niets
	 * @param we het event
	 */
	@Override
	public void windowActivated(WindowEvent we) {
	}

	/**
	 * doet niets
	 * @param we het event
	 */
	@Override
	public void windowDeactivated(WindowEvent we) {
	}

	/** abstracte functie */
	public abstract void refresh();
}
