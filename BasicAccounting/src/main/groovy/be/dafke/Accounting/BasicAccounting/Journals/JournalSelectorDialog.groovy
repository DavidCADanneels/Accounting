package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.ComponentModel.RefreshableDialog

import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JPanel
import java.awt.BorderLayout

class JournalSelectorDialog extends RefreshableDialog {
    private JButton ok
    private Journal journal
    private JComboBox<Journal> combo

    JournalSelectorDialog(Journals journals){
        super("Select Journal:")
        combo = new JComboBox<>()
        for (Journal journal : journals.getBusinessObjects()) {
            combo.addItem(journal)
        }
        combo.addActionListener({ e -> journal = (Journal) combo.getSelectedItem() })
        ok = new JButton("Ok (Close popup)")
        ok.addActionListener({ e -> dispose() })

        JPanel innerPanel = new JPanel(new BorderLayout())
        innerPanel.setLayout(new BorderLayout())
        innerPanel.add(combo, BorderLayout.CENTER)
        innerPanel.add(ok, BorderLayout.SOUTH)
        setContentPane(innerPanel)
        pack()
    }

    void setSelectedJournal(Journal journal){
        combo.setSelectedItem(journal)
    }

    Journal getSelection(){
        journal
    }
}