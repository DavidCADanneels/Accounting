package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Mortgage

import javax.swing.*

class MortgageGUI extends JFrame  {
    static MortgageGUI gui = null
    final MortgagePanel mortgagePanel

    MortgageGUI() {
        super("Mortgages")
        mortgagePanel = new MortgagePanel()
        setContentPane(mortgagePanel)
        pack()
        refresh()
    }

    static MortgageGUI showMortgages() {
        if(gui == null){
            gui = new MortgageGUI()
            Main.addFrame(gui)
        }
        gui
    }

    static void selectMortgage(Mortgage mortgage) {
        gui.reselect(mortgage)
    }

    void refresh() {
        mortgagePanel.refresh()
    }

    void reselect(Mortgage mortgage) {
        mortgagePanel.reselect(mortgage)
    }
}