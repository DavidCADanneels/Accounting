package be.dafke.Accounting.BasicAccounting.MainApplication


import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.JournalSwitchViewPanel
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesPanel
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import java.awt.*

import static javax.swing.JSplitPane.VERTICAL_SPLIT

class NewLayoutPanel extends JPanel {

    MortgagesPanel mortgagesPanel
    JSplitPane journalViewAndEditSplitPane

    NewLayoutPanel(JournalEditPanel journalEditPanel, JournalSelectorPanel journalSelectorPanel, JournalSwitchViewPanel journalSwitchViewPanel) {

        mortgagesPanel = new MortgagesPanel(journalEditPanel)


        JPanel links = new JPanel()
        links.setLayout(new BorderLayout())
//        links.add(accountGuiLeft, BorderLayout.CENTER)
        links.add(mortgagesPanel, BorderLayout.SOUTH)

        setLayout(new BorderLayout())
        journalViewAndEditSplitPane = Main.createSplitPane(journalSwitchViewPanel, journalEditPanel, VERTICAL_SPLIT)

        JPanel centerPanel = new JPanel(new BorderLayout())
        centerPanel.add(journalViewAndEditSplitPane, BorderLayout.CENTER)
        centerPanel.add(journalSelectorPanel, BorderLayout.NORTH)

//        add(accountGuiRight, BorderLayout.EAST)
        add(centerPanel, BorderLayout.CENTER)
        add(links, BorderLayout.WEST)
    }

    void setAccounting(Accounting accounting){
        mortgagesPanel.setMortgages(accounting?accounting.mortgages:null)
    }

    void fireShowInputChanged(boolean enabled) {
        mortgagesPanel.visible = enabled
    }

    void setMortgages(Mortgages mortgages) {
        mortgagesPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage)
    }
}
