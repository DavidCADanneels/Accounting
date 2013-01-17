package be.dafke;

import javax.swing.*;

/**
 * Abstracte klasse, bevat een abstracte functie refresh() die alle andere Refreshable frame kan laten hertekenen
 * @author David Danneels
 * @since 01/10/2010
 * @see RefreshableFrame#refresh() refresh()
 */
public abstract class RefreshableFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param title de titel van het JFrame
	 */
	public RefreshableFrame(String title) {
		super(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/** abstracte functie */
	public abstract void refresh();
}
