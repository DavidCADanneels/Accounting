package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Allergenes

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class AllergenesEditGUI extends JFrame {
    final AllergenesEditPanel allergenesEditPanel

    static final HashMap<Allergenes, AllergenesEditGUI> guis = new HashMap<>()

    AllergenesEditGUI(Allergenes allergenes) {
        super(getBundle("Accounting").getString("ALLERGENES"))
        allergenesEditPanel = new AllergenesEditPanel(allergenes)
        setContentPane(allergenesEditPanel)
        pack()
    }

    static AllergenesEditGUI showAllergenes(Allergenes allergenes) {
        AllergenesEditGUI gui = guis.get(allergenes)
        if (gui == null) {
            gui = new AllergenesEditGUI(allergenes)
            guis.put(allergenes, gui)
            Main.addFrame(gui)
        }
        gui
    }
}