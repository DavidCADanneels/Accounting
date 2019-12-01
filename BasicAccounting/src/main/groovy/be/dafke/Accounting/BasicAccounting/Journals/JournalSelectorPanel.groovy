package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.BusinessModelDao.AccountingSession
import be.dafke.BusinessModelDao.Session

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
    private JComboBox<Journal> combo
    private JCheckBox showInput, mergeTransactions
    private JournalEditPanel journalEditPanel

    JournalSelectorPanel(JournalEditPanel journalEditPanel){
        this.journalEditPanel = journalEditPanel
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("JOURNALS")))
        // this will strech the component
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
        combo = new JComboBox<>()
        combo.addActionListener(this)
        combo.setEnabled(false)
        add(combo)

        JRadioButton view1 = new JRadioButton(VIEW1)
        JRadioButton view2 = new JRadioButton(VIEW2)
        ButtonGroup group = new ButtonGroup()
        group.add(view1)
        group.add(view2)

        view1.setSelected(true)
        view1.addActionListener({ e ->
            if (view1.isSelected()) {
                Main.switchView(VIEW1)
            } else {
                Main.switchView(VIEW2)
            }
        })
        view2.addActionListener({ e ->
            if (view1.isSelected()) {
                Main.switchView(VIEW1)
            } else {
                Main.switchView(VIEW2)
            }
        })
        add(view1)
        add(view2)

        showInput = new JCheckBox("Show Input Panel")
        showInput.addActionListener({ e ->
            Main.fireShowInputChanged(showInput.isSelected())
        })
        showInput.setSelected(true)
        add(showInput)

        mergeTransactions = new JCheckBox("Merge Transactions")
        mergeTransactions.addActionListener({ e ->
            Main.fireMultiTransactionChanged(mergeTransactions.isSelected())
        })
        mergeTransactions.setSelected(false)
        add(mergeTransactions)
    }

    void actionPerformed(ActionEvent ae) {
        Journal newJournal = (Journal) combo.getSelectedItem()
        Journal journal = journalEditPanel.switchJournal(newJournal)
        Main.setJournal(journal)
    }

    void setAccounting(Accounting accounting) {
        setJournals(accounting==null?null:accounting.getJournals())
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accountingSession==null?null:accountingSession.getActiveJournal())
    }

    void setJournals(Journals journals){
        Journal selectedJournal = (Journal)combo.getSelectedItem()
        combo.removeActionListener(this)
        combo.removeAllItems()
        if (journals!=null) {
            for (Journal journal : journals.getBusinessObjects()) {
                combo.addItem(journal)
            }
        }
        combo.addActionListener(this)
        combo.setSelectedItem(selectedJournal)
        combo.setEnabled(journals!=null)
    }

    void setJournal(Journal journal) {
        combo.setSelectedItem(journal)
    }
}