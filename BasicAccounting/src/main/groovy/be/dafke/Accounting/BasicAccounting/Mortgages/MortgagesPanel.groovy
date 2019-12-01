package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BasicAccounting.Journals.JournalEditPanel
import be.dafke.Accounting.BusinessModel.Mortgage
import be.dafke.Accounting.BusinessModel.Mortgages

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.awt.event.KeyEvent
import java.util.List

class MortgagesPanel extends JPanel {
    final JList<Mortgage> list
    final JButton pay// , newMortgage, details
    final JCheckBox hidePayedOff
    Mortgages mortgages
    final DefaultListModel<Mortgage> listModel
    Mortgage selectedMortgage
    JournalEditPanel journalEditPanel

    MortgagesPanel(JournalEditPanel journalEditPanel) {
        this.journalEditPanel = journalEditPanel
        setLayout(new BorderLayout())
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Mortgages"))
        list = new JList<>()
        listModel = new DefaultListModel<>()
        list.setModel(listModel)
        list.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting() && list.selectedIndex != -1) {
                selectedMortgage = list.getSelectedValue()
            } else {
                selectedMortgage = null
            }
            enablePayButton(selectedMortgage)
        })
        pay = new JButton("Pay")
        pay.setMnemonic(KeyEvent.VK_P)
        pay.addActionListener({ e -> book() })
        pay.enabled = false

        hidePayedOff = new JCheckBox("Hide Payed Off")
        hidePayedOff.selected = true
        hidePayedOff.addActionListener({ e -> refreshList() })

        add(hidePayedOff, BorderLayout.NORTH)
        add(list, BorderLayout.CENTER)
        add(pay, BorderLayout.SOUTH)
    }

    void book() {
        Mortgage mortgage = list.getSelectedValue()
        if (mortgage != null) {
            journalEditPanel.addMortgageTransaction(mortgage)
        }
    }

    void enablePayButton(Mortgage mortgage) {
        if(mortgage == selectedMortgage) {
            pay.enabled = selectedMortgage != null && selectedMortgage.bookable
        }
    }

    void setMortgages(Mortgages mortgages) {
        this.mortgages = mortgages
        refreshList()
    }

    void refreshList(){
        listModel.clear()
        if (mortgages != null) {
            List<Mortgage> businessObjects
            if(hidePayedOff.selected) {
                businessObjects = mortgages.getBusinessObjects({ mortgage -> !mortgage.isPayedOff() })
            } else {
                businessObjects = mortgages.businessObjects
            }
            for (Mortgage mortgage : businessObjects) {
                if (!listModel.contains(mortgage)) {
                    listModel.addElement(mortgage)
                }
            }
        }
        list.revalidate()
    }
}
