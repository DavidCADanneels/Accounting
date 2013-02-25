package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.Details.JournalDetails;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
import be.dafke.RefreshableComponent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class JournalsGUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Journal> combo;
	private final JButton maak, details;
	private final Accountings accountings;

	public JournalsGUI(Accountings accountings) {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
				"Accounting").getString("DAGBOEKEN")));
		this.accountings = accountings;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<Journal>();
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
            Accounting accounting = accountings.getCurrentAccounting();
            String title = accounting.toString() + "/" +
                    getBundle("Accounting").getString("DAGBOEK_DETAILS") + "/"
                    + journal.toString();
            RefreshableComponent gui = AccountingMenuBar.getFrame(title);
            if(gui == null){
                gui = new JournalDetails(title, journal, accounting);
                AccountingMenuBar.addRefreshableComponent(title, gui);
            }
            gui.setVisible(true);
		} else if (e.getSource() == maak) {
            AccountingMenuBar.getFrame(AccountingMenuBar.NEW_JOURNAL).setVisible(true);
		} else if (e.getSource() == combo) {
			Journal journal = (Journal) combo.getSelectedItem();
			Accounting accounting = accountings.getCurrentAccounting();
			accounting.setCurrentJournal(journal);
		}
	}

	public void refresh() {
        combo.removeAllItems();
        boolean active = accountings.isActive();
		if (active) {
			Accounting accounting = accountings.getCurrentAccounting();
			Journals journals = accounting.getJournals();
            for(Journal journal: journals.values()){
                combo.addItem(journal);
            }
			combo.setSelectedItem(accounting.getCurrentJournal());
		} else {
			combo.setSelectedItem(null);
		}
		combo.setEnabled(active);
		maak.setEnabled(active);
		details.setEnabled(active);
	}
}