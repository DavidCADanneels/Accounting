package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;

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
public class JournalSelectorPanel extends JPanel implements ActionListener{
	private JComboBox<Journal> combo;
	private JournalEditPanel journalEditPanel;

	public JournalSelectorPanel(JournalEditPanel journalEditPanel){
		this.journalEditPanel = journalEditPanel;
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		// this will strech the component
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<>();
        combo.addActionListener(this);
		combo.setEnabled(false);
		add(combo);
	}

	public void actionPerformed(ActionEvent ae) {
		Journal newJournal = (Journal) combo.getSelectedItem();
		Journal journal = journalEditPanel.switchJournal(newJournal);
		Main.setJournal(journal);
	}

	public void setAccounting(Accounting accounting) {
		setJournals(accounting==null?null:accounting.getJournals());
		setJournal(accounting==null?null:accounting.getActiveJournal());
	}

	public void setJournals(Journals journals){
		Journal selectedJournal = (Journal)combo.getSelectedItem();
		combo.removeActionListener(this);
		combo.removeAllItems();
		if (journals!=null) {
			for (Journal journal : journals.getBusinessObjects()) {
				combo.addItem(journal);
			}
		}
		combo.addActionListener(this);
		combo.setSelectedItem(selectedJournal);
		combo.setEnabled(journals!=null);
	}

	public void setJournal(Journal journal) {
		combo.setSelectedItem(journal);
	}
}