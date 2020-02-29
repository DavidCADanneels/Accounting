package be.dafke.Accounting.BasicAccounting.Journals.Selector

import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import static java.util.ResourceBundle.getBundle

class JournalSelectorPanel extends JPanel implements ActionListener{

    JComboBox<Journal> combo
    JCheckBox showInput
    JournalEditPanel journalEditPanel

    JournalSelectorPanel(JournalEditPanel journalEditPanel){
        this.journalEditPanel = journalEditPanel
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")))
        // this will strech the component
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
        combo = new JComboBox<>()
        combo.addActionListener(this)
        combo.enabled = false
        add(combo)

        showInput = new JCheckBox("Show Input Panels")
        showInput.addActionListener({ e ->
            Main.fireShowInputChanged(showInput.selected)
        })
        showInput.selected = true
        add(showInput)
    }

    void actionPerformed(ActionEvent ae) {
        Journal newJournal = (Journal) combo.selectedItem
        Journal journal = journalEditPanel.switchJournal(newJournal)
        Main.journal = journal
    }

    void setAccounting(Accounting accounting) {
        setJournals(accounting?accounting.journals:null)
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accountingSession?accountingSession.activeJournal:null)
    }

    void setJournals(Journals journals){
        Journal selectedJournal = (Journal)combo.selectedItem
        combo.removeActionListener(this)
        combo.removeAllItems()
        if (journals) {
            for (Journal journal : journals.businessObjects) {
                combo.addItem(journal)
            }
        }
        combo.addActionListener(this)
        combo.setSelectedItem(selectedJournal)
        combo.enabled = journals!=null
    }

    void setJournal(Journal journal) {
        combo.setSelectedItem(journal)
    }
}