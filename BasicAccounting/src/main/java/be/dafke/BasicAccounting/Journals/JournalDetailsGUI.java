package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDetailsGUI extends JFrame implements WindowListener {
	private static HashMap<Journal,JournalDetailsGUI> journalDetailsMap = new HashMap<>();
	private final JournalDetailsPanel journalDetailsPanel;

	private JournalDetailsGUI(Journal journal, Journals journals) {
		super(getBundle("Accounting").getString("JOURNAL_DETAILS") + journal.toString());
		journalDetailsPanel = new JournalDetailsPanel(journal, journals);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(journalDetailsPanel);
		pack();

	}

	public static JournalDetailsGUI getJournalDetails(Journal journal, Journals journals){
		JournalDetailsGUI journalDetailsGUI = journalDetailsMap.get(journal);
		if(journalDetailsGUI ==null){
			journalDetailsGUI = new JournalDetailsGUI(journal, journals);
			journalDetailsMap.put(journal, journalDetailsGUI);
			Main.addFrame(journalDetailsGUI);
		}
		journalDetailsGUI.setVisible(true);
		return journalDetailsGUI;
	}

	public void selectObject(Booking object){
		journalDetailsPanel.selectObject(object);
	}

	public void windowClosing(WindowEvent we) {
        journalDetailsPanel.closePopups();
    }
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

	public static void fireJournalDataChangedForAll(Journal journal) {
		JournalDetailsGUI journalDetailsGUI = journalDetailsMap.get(journal);
		if(journalDetailsGUI !=null) {
			journalDetailsGUI.fireJournalDataChanged();
		}
	}

	public void fireJournalDataChanged() {
		journalDetailsPanel.fireJournalDataChanged();
	}
}
