package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Allergenes

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class AllergenesViewGUI extends JFrame {
    private final AllergenesViewPanel allergenesEditPanel

    private static final HashMap<Allergenes, AllergenesViewGUI> guis = new HashMap<>()

    private AllergenesViewGUI(Allergenes allergenes) {
        super(getBundle("Accounting").getString("ALLERGENES"))
        allergenesEditPanel = new AllergenesViewPanel(false)
        allergenesEditPanel.setAllergenes(allergenes)
        setContentPane(allergenesEditPanel)
        pack()
    }

    static AllergenesViewGUI showAllergenes(Allergenes allergenes) {
        AllergenesViewGUI gui = guis.get(allergenes)
        if (gui == null) {
            gui = new AllergenesViewGUI(allergenes)
            guis.put(allergenes, gui)
            Main.addFrame(gui)
        }
        gui
    }
}