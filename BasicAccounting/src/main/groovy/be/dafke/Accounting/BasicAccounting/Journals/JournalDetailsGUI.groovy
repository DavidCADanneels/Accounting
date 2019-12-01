package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals

import javax.swing.*
import java.awt.*
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

import static java.util.ResourceBundle.getBundle

class JournalDetailsGUI extends JFrame implements WindowListener {
    static HashMap<Journal,JournalDetailsGUI> journalDetailsMap = new HashMap<>()
    final JournalDetailsPanel journalDetailsPanel

    JournalDetailsGUI(Point location, Journal journal, Journals journals) {
        super(getBundle("Accounting").getString("JOURNAL_DETAILS") + journal.toString())
        journalDetailsPanel = new JournalDetailsPanel(journal, journals)
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
        setLocation(location)
        setContentPane(journalDetailsPanel)
        pack()

    }

    static JournalDetailsGUI getJournalDetails(Point location, Journal journal, Journals journals){
        JournalDetailsGUI journalDetailsGUI = journalDetailsMap.get(journal)
        if(journalDetailsGUI ==null){
            journalDetailsGUI = new JournalDetailsGUI(location, journal, journals)
            journalDetailsMap.put(journal, journalDetailsGUI)
            Main.addFrame(journalDetailsGUI)
        }
        journalDetailsGUI.visible = true
        journalDetailsGUI
    }

    void selectObject(Booking object){
        journalDetailsPanel.selectObject(object)
    }

    void windowClosing(WindowEvent we) {
        journalDetailsPanel.closePopups()
    }
    void windowOpened(WindowEvent e) {}
    void windowClosed(WindowEvent e) {}
    void windowIconified(WindowEvent e) {}
    void windowDeiconified(WindowEvent e) {}
    void windowActivated(WindowEvent e) {}
    void windowDeactivated(WindowEvent e) {}

    static void fireJournalDataChangedForAll(Journal journal) {
        JournalDetailsGUI journalDetailsGUI = journalDetailsMap.get(journal)
        if(journalDetailsGUI !=null) {
            journalDetailsGUI.fireJournalDataChanged()
        }
    }

    void fireJournalDataChanged() {
        journalDetailsPanel.fireJournalDataChanged()
    }
}
