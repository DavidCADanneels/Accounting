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
public class JournalsGUI extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Journal> combo;
	private JournalInputGUI journalInputGUI;
	private JournalGUI journalGUI;

	public JournalsGUI(JournalGUI journalGUI, JournalInputGUI journalInputGUI){//Accounts accounts,  ) {
		this.journalInputGUI=journalInputGUI;
		this.journalGUI = journalGUI;
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<>();
        combo.addActionListener(this);
		combo.setEnabled(false);
		add(combo);
	}

	public void actionPerformed(ActionEvent ae) {
		Journal newJournal = (Journal) combo.getSelectedItem();
		Journal journal = journalInputGUI.switchJournal(newJournal);
		Main.setJournal(journal);
	}

	public void setAccounting(Accounting accounting) {
		setJournals(accounting==null?null:accounting.getJournals());
		setJournal(accounting==null?null:accounting.getActiveJournal());
	}

	public void setJournals(Journals journals){
		combo.removeActionListener(this);
		combo.removeAllItems();
		if (journals!=null) {
			for (Journal journal : journals.getBusinessObjects()) {
				combo.addItem(journal);
			}
		}
		combo.addActionListener(this);
		combo.setEnabled(journals!=null);
	}

	public void addJournal(Journal journal){
		int itemCount = combo.getItemCount();
		combo.addItem(journal);
		if(itemCount==0){
			setJournal(journal);
		}
	}

	public void setJournal(Journal journal) {
		combo.removeActionListener(this);
		combo.setSelectedItem(journal);
		combo.addActionListener(this);

		journalGUI.setJournal(journal);
		journalInputGUI.setJournal(journal);
	}
}