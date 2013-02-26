package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.GUI.Details.JournalDetails;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
import be.dafke.DisposableComponent;

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
	private Accounting accounting;

	public JournalsGUI(Accounting accounting) {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("DAGBOEKEN")));
		this.accounting = accounting;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<Journal>();
		combo.setEnabled(false);
		add(combo);
		JPanel paneel = new JPanel();
		maak = new JButton(getBundle("Accounting").getString("NIEUW_DAGBOEK"));
		maak.addActionListener(this);
		maak.setEnabled(false);
		paneel.add(maak);
		details = new JButton(getBundle("Accounting").getString(
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
            String key = accounting.toString() + ComponentMap.JOURNAL_DETAILS + journal.toString();
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if(gui == null){
                gui = new JournalDetails(journal, accounting);
                ComponentMap.addDisposableComponent(key, gui);
            }
            gui.setVisible(true);
		} else if (e.getSource() == maak) {
            String key = accounting.toString()+ComponentMap.JOURNAL_MANAGEMENT;
            ComponentMap.getDisposableComponent(key).setVisible(true);
		} else if (e.getSource() == combo) {
			Journal journal = (Journal) combo.getSelectedItem();
			accounting.setCurrentJournal(journal);
		}
	}

    public void setAccounting(Accounting accounting){
        this.accounting = accounting;
        refresh();
    }

	public void refresh() {
        combo.removeAllItems();
		if (accounting!=null) {
			Journals journals = accounting.getJournals();
            for(Journal journal: journals.values()){
                combo.addItem(journal);
            }
			combo.setSelectedItem(accounting.getCurrentJournal());
		} else {
			combo.setSelectedItem(null);
		}
		combo.setEnabled(accounting!=null && accounting.getCurrentJournal()!=null);
		maak.setEnabled(accounting!=null);
		details.setEnabled(accounting!=null && accounting.getCurrentJournal()!=null);
	}
}