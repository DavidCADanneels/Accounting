package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Mortgages

import javax.swing.*

class MortgageCalculatorGUI extends JFrame {

    static final HashMap<Mortgages, MortgageCalculatorGUI> mortgageCalculatorGuis = new HashMap<>()
    MortgageCalculatorPanel mortgageCalculatorPanel

    MortgageCalculatorGUI(Mortgages mortgages) {
        super("Mortgage Calculator")
        mortgageCalculatorPanel = new MortgageCalculatorPanel(mortgages)
        setContentPane(mortgageCalculatorPanel)
        pack()
    }

    static MortgageCalculatorGUI showCalculator(Mortgages mortgages) {
        MortgageCalculatorGUI gui = mortgageCalculatorGuis.get(mortgages)
        if (gui == null) {
            gui = new MortgageCalculatorGUI(mortgages)
            mortgageCalculatorGuis.put(mortgages, gui)
            Main.addFrame(gui)
        }
        gui
    }
}
