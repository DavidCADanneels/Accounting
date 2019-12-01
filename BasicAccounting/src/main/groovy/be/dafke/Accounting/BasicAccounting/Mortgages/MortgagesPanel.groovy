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
    private final JList<Mortgage> list
    private final JButton pay// , newMortgage, details
    private final JCheckBox hidePayedOff
    private Mortgages mortgages
    private final DefaultListModel<Mortgage> listModel
    private Mortgage selectedMortgage
    private JournalEditPanel journalEditPanel

    MortgagesPanel(JournalEditPanel journalEditPanel) {
        this.journalEditPanel = journalEditPanel
        setLayout(new BorderLayout())
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Mortgages"))
        list = new JList<>()
        listModel = new DefaultListModel<>()
        list.setModel(listModel)
        list.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
                selectedMortgage = list.getSelectedValue()
            } else {
                selectedMortgage = null
            }
            enablePayButton(selectedMortgage)
        })
        pay = new JButton("Pay")
        pay.setMnemonic(KeyEvent.VK_P)
        pay.addActionListener({ e -> book() })
        pay.setEnabled(false)

        hidePayedOff = new JCheckBox("Hide Payed Off")
        hidePayedOff.setSelected(true)
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
            pay.setEnabled(selectedMortgage != null && selectedMortgage.isBookable())
        }
    }

    void setMortgages(Mortgages mortgages) {
        this.mortgages = mortgages
        refreshList()
    }

    private void refreshList(){
        listModel.clear()
        if (mortgages != null) {
            List<Mortgage> businessObjects
            if(hidePayedOff.isSelected()) {
                businessObjects = mortgages.getBusinessObjects({ mortgage -> !mortgage.isPayedOff() })
            } else {
                businessObjects = mortgages.getBusinessObjects()
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
