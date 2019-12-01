package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Mortgage
import be.dafke.Accounting.BusinessModel.Mortgages

import javax.swing.*

class MortgageGUI extends JFrame  {
    static final HashMap<Mortgages, MortgageGUI> mortgageGuis = new HashMap<>()
    final MortgagePanel mortgagePanel

    MortgageGUI(Mortgages mortgages, Accounts accounts) {
        super("Mortgages")
        mortgagePanel = new MortgagePanel(mortgages, accounts)
        setContentPane(mortgagePanel)
        pack()
        refresh()
    }

    static MortgageGUI showMortgages(Mortgages mortgages, Accounts accounts) {
        MortgageGUI gui = mortgageGuis.get(mortgages)
        if(gui == null){
            gui = new MortgageGUI(mortgages, accounts)
            mortgageGuis.put(mortgages,gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void refreshAllFrames(){
        for (MortgageGUI mortgageGUI:mortgageGuis.values()) {
            mortgageGUI.refresh()
        }
    }

    static void selectMortgage(Mortgage mortgage){
        for (MortgageGUI mortgageGUI:mortgageGuis.values()) {
            mortgageGUI.reselect(mortgage)
        }
    }

    void refresh() {
        mortgagePanel.refresh()
    }

    void reselect(Mortgage mortgage) {
        mortgagePanel.reselect(mortgage)
    }
}