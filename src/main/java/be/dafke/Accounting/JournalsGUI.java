package be.dafke.Accounting;

import be.dafke.Accounting.Details.JournalDetails;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Journal;
import be.dafke.Accounting.Objects.Journals;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author David Danneels
 */
public class JournalsGUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JournalGUI dagboekGUI;
	private JComboBox combo;
	private final JButton maak, details;
	private final AccountingGUIFrame parent;

//	private final NewJournalGUI newJournalGui = null;

	public JournalsGUI(JournalGUI journalGUI, AccountingGUIFrame parent) {
		this.parent = parent;
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
				"Accounting").getString("DAGBOEKEN")));
		dagboekGUI = journalGUI;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox();
		combo.setEnabled(false);
		add(combo);
		JPanel paneel = new JPanel();
		maak = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString("NIEUW_DAGBOEK"));
		maak.addActionListener(this);
		maak.setEnabled(false);
		paneel.add(maak);
		details = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString(
				"DETAILS_DAGBOEK"));
		details.addActionListener(this);
		details.setEnabled(false);
		paneel.add(details);
		add(paneel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == details) {
			Journal journal = (Journal) combo.getSelectedItem();
			parent.addChildFrame(new JournalDetails(journal, parent));
		} else if (e.getSource() == maak) {
			NewJournalGUI.getInstance(parent).setVisible(true);
		} else if (e.getSource() == combo) {
			Journal journal = (Journal) combo.getSelectedItem();
			Accountings.getCurrentAccounting().setCurrentJournal(journal);
			dagboekGUI.init();
		}
	}

	public void activateButtons(/*boolean active*/) {
		boolean active = Accountings.isActive();
		if (active) {
			remove(combo);
			Journals journals = Accountings.getCurrentAccounting().getJournals();
			combo = new JComboBox(journals.values().toArray());
			combo.addActionListener(this);
			combo.setSelectedItem(Accountings.getCurrentAccounting().getCurrentJournal());
			add(combo);
			revalidate();
		} else {
			remove(combo);
			combo = new JComboBox();
			combo.setSelectedItem(null);
			add(combo);
			revalidate();
		}
		combo.setEnabled(active);
		maak.setEnabled(active);
		details.setEnabled(active);
	}
}