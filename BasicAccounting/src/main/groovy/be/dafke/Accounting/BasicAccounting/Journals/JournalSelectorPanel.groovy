package be.dafke.Accounting.BasicAccounting.Journals

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
    static final String VIEW1 = "View1"
    static final String VIEW2 = "View2"
    JComboBox<Journal> combo
    JCheckBox showInput, mergeTransactions
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

        JRadioButton view1 = new JRadioButton(VIEW1)
        JRadioButton view2 = new JRadioButton(VIEW2)
        ButtonGroup group = new ButtonGroup()
        group.add(view1)
        group.add(view2)

        view1.selected = true
        view1.addActionListener({ e ->
            if (view1.selected) {
                Main.switchView(VIEW1)
            } else {
                Main.switchView(VIEW2)
            }
        })
        view2.addActionListener({ e ->
            if (view1.selected) {
                Main.switchView(VIEW1)
            } else {
                Main.switchView(VIEW2)
            }
        })
        add(view1)
        add(view2)

        showInput = new JCheckBox("Show Input Panel")
        showInput.addActionListener({ e ->
            Main.fireShowInputChanged(showInput.selected)
        })
        showInput.selected = true
        add(showInput)

        mergeTransactions = new JCheckBox("Merge Transactions")
        mergeTransactions.addActionListener({ e ->
            Main.fireMultiTransactionChanged(mergeTransactions.selected)
        })
        mergeTransactions.selected = false
        add(mergeTransactions)
    }

    void actionPerformed(ActionEvent ae) {
        Journal newJournal = (Journal) combo.selectedItem
        Journal journal = journalEditPanel.switchJournal(newJournal)
        Main.journal = journal
    }

    void setAccounting(Accounting accounting) {
        setJournals(accounting?accounting.journals:null)
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accountingSession==null?null:accountingSession.activeJournal)
    }

    void setJournals(Journals journals){
        Journal selectedJournal = (Journal)combo.selectedItem
        combo.removeActionListener(this)
        combo.removeAllItems()
        if (journals!=null) {
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