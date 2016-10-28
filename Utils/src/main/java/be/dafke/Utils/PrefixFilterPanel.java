package be.dafke.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * Een paneel met list en tekstveld<br>
 * Van zodra je begint te tikken in het tekstveld, verdwijnen de personen/objecten uit de list (de view ervan) zodat de
 * list steeds korter wordt
 * @version 1
 * @author David Danneels
 * @since 28/07/2005
 * @see AlphabeticListModel AlphabeticListModel
 */
public class PrefixFilterPanel<K> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrol;
	protected JTextField zoekveld;
    private PrefixFilter<K> filter;
    private DefaultListModel<K> model;

	/**
	 * Constructor with model, panel and map
	 * @param model het model van de list
	 * @param list the list
	 * @param map map met de gegevens uit de list
	 */
	public PrefixFilterPanel(DefaultListModel<K> model, JList<K> list, List<K> map) {
        this.model = model;
        filter = new PrefixFilter<>(model, map);
        scrol = new JScrollPane(list);
		zoekveld = new JTextField(20);
		zoekveld.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				repaint();
			}
			public void insertUpdate(DocumentEvent e) {
				repaint();
			}
			public void removeUpdate(DocumentEvent e) {
				repaint();
			}
		});

		JPanel zoekpaneel = new JPanel();
		zoekpaneel.add(new JLabel(getBundle("Utils").getString("SEARCH")));
		zoekpaneel.add(zoekveld);
		setLayout(new BorderLayout());
		add(scrol, BorderLayout.CENTER);
		add(zoekpaneel, BorderLayout.NORTH);
	}

	/**
	 * Vervangt de datamap en hertekent het JPanel
	 * @param newMap de nieuwe map
	 * @since 01/10/2010
	 */
	public void resetMap(List<K> newMap) {
		filter = new PrefixFilter<>(model, newMap);
		repaint();
	}

	/**
	 * verwijdert alle objecten uit de view en voegt enkel de objecten toe, waarvan de String-voorstelling begint met de
	 * letters in het tekstveld
	 */
	@Override
	public void paintComponent(Graphics g) {
        filter.filter(zoekveld.getText());
		super.paintComponent(g);
	}
}