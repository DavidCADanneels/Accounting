package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Mortgages

import javax.swing.*

class MortgageCalculatorGUI extends JFrame {

    static MortgageCalculatorGUI gui = null
    MortgageCalculatorPanel mortgageCalculatorPanel

    MortgageCalculatorGUI() {
        super("Mortgage Calculator")
        mortgageCalculatorPanel = new MortgageCalculatorPanel()
        setContentPane(mortgageCalculatorPanel)
        pack()
    }

    static MortgageCalculatorGUI showCalculator() {
        if (gui == null) {
            gui = new MortgageCalculatorGUI()
            Main.addFrame(gui)
        }
        gui
    }
}
