package be.dafke;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * Een paneel met lijst en tekstveld<br>
 * Van zodra je begint te tikken in het tekstveld, verdwijnen de personen/objecten uit de lijst (de view ervan) zodat de
 * lijst steeds korter wordt
 * @version 1
 * @author David Danneels
 * @since 28/07/2005
 * @see AlfabetischeLijstModel AlfabetischeLijstModel
 */
public class PrefixZoeker extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JList lijst;
	private final JScrollPane scrol;
	protected JTextField zoekveld;
	// protected HashMap map;
	protected ArrayList map;

	/*
	 * Constructor met lijst, paneel en gegevensmap als parameters
	 * @param lijst de lijst (dus impliciet ook een model)
	 * @param paneel <code>null</code> of een paneel dat onderaan wordt in beeld wordt toegevoegd
	 * @param map map met de gegevens uit de lijst l
	 * @see be.dafke.Accounting.AccountsGUI#AccountsGUI(be.dafke.Accounting.JournalGUI) AccountsGUI(<a
	 * href="Accounting/JournalGUI.html">JournalGUI</a>)
	 * @see be.dafke.Accounting.ProjectManagerFrame#ProjectManagerFrame()
	 */
	public PrefixZoeker(JList lijst, JPanel paneel, /* HashMap */ArrayList map) {
		this.map = map;
		this.lijst = lijst;
		scrol = new JScrollPane(lijst);
		zoekveld = new JTextField(20);
		zoekveld.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				repaint();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				repaint();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				repaint();
			}
		});

		JPanel zoekpaneel = new JPanel();
		zoekpaneel.add(new JLabel(java.util.ResourceBundle.getBundle("Dafke").getString("ZOEK")));
		zoekpaneel.add(zoekveld);
		setLayout(new BorderLayout());
		add(scrol, BorderLayout.CENTER);
		if (paneel != null) add(paneel, BorderLayout.SOUTH);
		add(zoekpaneel, BorderLayout.NORTH);
	}

	/*
	 * Vervangt de datamap en hertekent het JPanel
	 * @param map de nieuwe map
	 * @since 01/10/2010
	 * @see be.dafke.Accounting.ProjectManagerFrame#ProjectManagerFrame()
	 * @see be.dafke.Accounting.ProjectManagerFrame#refresh()
	 */
	public void resetMap(/* HashMap */ArrayList newMap) {
		map = newMap;
		repaint();
	}

	/**
	 * verwijdert alle objecten uit de view en voegt enkel de objecten toe, waarvan de String-voorstelling begint met de
	 * letters in het tekstveld
	 * <p>
	 * <i>(o.toString().startsWith(String s))</i>
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		DefaultListModel model = (DefaultListModel) lijst.getModel();
		model.removeAllElements();
		for(Object o : map) {
			if (o.toString().startsWith(zoekveld.getText())) model.addElement(o);
		}
	}
}