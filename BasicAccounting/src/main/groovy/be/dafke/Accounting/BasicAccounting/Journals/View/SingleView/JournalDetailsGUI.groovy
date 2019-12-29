package be.dafke.Accounting.BasicAccounting.Journals.View.SingleView

import be.dafke.Accounting.BasicAccounting.Journals.View.JournalSwitchViewPanel
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal

import javax.swing.*
import java.awt.*
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

import static java.util.ResourceBundle.getBundle

class JournalDetailsGUI extends JFrame implements WindowListener {
    static HashMap<Journal,JournalDetailsGUI> journalDetailsMap = new HashMap<>()
    final JournalSwitchViewPanel journalSwitchViewPanel

    JournalDetailsGUI(Point location, Journal journal) {
        super(getBundle("Accounting").getString("JOURNAL_DETAILS") + journal.toString())
        journalSwitchViewPanel = new JournalSwitchViewPanel()
        journalSwitchViewPanel.journal = journal
        setLocation(location)
        setContentPane(journalSwitchViewPanel)
        pack()

    }

    static JournalDetailsGUI getJournalDetails(Point location, Journal journal){
        JournalDetailsGUI journalDetailsGUI = journalDetailsMap.get(journal)
        if(journalDetailsGUI==null){
            journalDetailsGUI = new JournalDetailsGUI(location, journal)
            journalDetailsMap.put journal, journalDetailsGUI
            Main.addFrame(journalDetailsGUI)
        }
        journalDetailsGUI.visible = true
        journalDetailsGUI
    }

    void selectObject(Booking booking){
        journalSwitchViewPanel.selectBooking(booking)
    }

    void windowClosing(WindowEvent we) {
        journalSwitchViewPanel.closePopups()
    }
    void windowOpened(WindowEvent e) {}
    void windowClosed(WindowEvent e) {}
    void windowIconified(WindowEvent e) {}
    void windowDeiconified(WindowEvent e) {}
    void windowActivated(WindowEvent e) {}
    void windowDeactivated(WindowEvent e) {}

    static void fireJournalDataChangedForAll(Journal journal) {
        JournalDetailsGUI journalDetailsGUI = journalDetailsMap.get(journal)
        if(journalDetailsGUI) {
            journalDetailsGUI.fireJournalDataChanged()
        }
    }

    void fireJournalDataChanged() {
        journalSwitchViewPanel.fireJournalDataChanged()
    }
}
