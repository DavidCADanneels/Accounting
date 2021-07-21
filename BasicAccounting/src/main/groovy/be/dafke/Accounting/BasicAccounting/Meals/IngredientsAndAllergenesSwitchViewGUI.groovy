package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class IngredientsAndAllergenesSwitchViewGUI extends JFrame {
    final IngredientsAndAllergenesSwitchViewPanel panel

    static final HashMap<Accounting, IngredientsAndAllergenesSwitchViewGUI> guis = new HashMap<>()

    IngredientsAndAllergenesSwitchViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("INGREDIENTS_AND_ALLERGENES"))
        panel = new IngredientsAndAllergenesSwitchViewPanel(accounting)
        setContentPane(panel)
        pack()
    }

    static IngredientsAndAllergenesSwitchViewGUI show(Accounting accounting) {
        IngredientsAndAllergenesSwitchViewGUI gui = guis.get(accounting)
        if (gui == null) {
            gui = new IngredientsAndAllergenesSwitchViewGUI(accounting)
            guis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }
}