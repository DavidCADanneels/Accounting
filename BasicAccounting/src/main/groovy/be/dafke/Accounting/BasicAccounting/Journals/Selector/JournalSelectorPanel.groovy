package be.dafke.Accounting.BasicAccounting.Journals.Selector

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.BusinessModel.Transaction
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

    JournalSelectorPanel(){
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
        if(newJournal) {
            Journal finalSelection = Main.switchJournal(newJournal)
            combo.removeActionListener(this)
            combo.setSelectedItem(finalSelection)
            combo.addActionListener(this)
        }
    }

//    void switchJournal(Journal newJournal){
//        Journal currentJournal = Main.currentJournal
//        Transaction currentTransaction = Main.currentTransaction
//        if (!currentTransaction.businessObjects.empty && currentJournal != newJournal) {
//            newJournal = askInput(newJournal)
//        }
//        if (currentJournal != newJournal) {
//            Main.journal = newJournal
//        }
//    }

//    Journal askInput(Journal newJournal) {
//        Journal journal = Main.getCurrentJournal()
////        Transaction transaction = Main.getCurrentTransaction()
//        Transaction newTransaction = newJournal.currentTransaction
//        String text = """\
//Do you want to transfer the current transaction from ${journal} to ${newJournal}?
//${newTransaction && !newTransaction.businessObjects.isEmpty()?"""\
//WARNING: ${newJournal} also has an open transactions, which will be lost if you select transfer
//""":''}\
//"""
//        int answer = JOptionPane.showConfirmDialog(null, text)
//        if (answer == JOptionPane.YES_OPTION) {
//            Main.moveTransactionToNewJournal(newJournal)
//            newJournal
//        } else if (answer == JOptionPane.NO_OPTION) {
//            Main.saveCurrentTransaction()
//            newJournal
//        } else {
//            journal
//        }
//
//    }

//    void moveTransactionToNewJournal(Journal newJournal){
//        Journal journal = Main.getCurrentJournal()
//        Transaction transaction = Main.getCurrentTransaction()
//        newJournal.currentTransaction = transaction
//        journal.currentTransaction = new Transaction(Calendar.getInstance(), "")
//    }

//    void saveCurrentTransaction(){
//        Journal journal = Main.getCurrentJournal()
//        Transaction transaction = Main.getCurrentTransaction()
//        journal.currentTransaction = transaction
//    }

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