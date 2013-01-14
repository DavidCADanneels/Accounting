package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.Details.JournalDetails;
import be.dafke.Accounting.GUI.JournalManagement.NewJournalGUI;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;

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
	private final JournalGUI journalGUI;
	private JComboBox combo;
	private final JButton maak, details;
	private final Accountings accountings;

//	private final NewJournalGUI newJournalGui = null;

	public JournalsGUI(Accountings accountings, JournalGUI journalGUI) {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
				"Accounting").getString("DAGBOEKEN")));
		this.journalGUI = journalGUI;
		this.accountings = accountings;
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
			JournalDetails gui = new JournalDetails(journal, accountings);
			gui.setVisible(true);
		} else if (e.getSource() == maak) {
			NewJournalGUI.getInstance(accountings).setVisible(true);
		} else if (e.getSource() == combo) {
			Journal journal = (Journal) combo.getSelectedItem();
			Accounting accounting = accountings.getCurrentAccounting();
			accounting.setCurrentJournal(journal);
			journalGUI.init();
		}
	}

	public void activateButtons(/*boolean active*/) {
		boolean active = accountings.isActive();
		if (active) {
			remove(combo);
			Accounting accounting = accountings.getCurrentAccounting();
			Journals journals = accounting.getJournals();
			combo = new JComboBox(journals.values().toArray());
			combo.addActionListener(this);
			combo.setSelectedItem(accounting.getCurrentJournal());
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