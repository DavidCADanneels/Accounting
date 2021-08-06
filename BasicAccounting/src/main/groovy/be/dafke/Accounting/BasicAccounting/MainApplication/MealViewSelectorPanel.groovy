package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.Color

class MealViewSelectorPanel extends JPanel {

    JToggleButton ingredients, allergenes

    ButtonGroup buttonGroup

    String selection = Main.INGREDIENTS_CENTER_VIEW

    MealViewSelectorPanel() {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Meals"))
        ingredients = new JToggleButton('Ingredients', true)
        allergenes = new JToggleButton('Allergenes')

        buttonGroup = new ButtonGroup()

        buttonGroup.add ingredients
        buttonGroup.add allergenes

        add ingredients
        add allergenes

        ingredients.addActionListener( {
            selection = Main.INGREDIENTS_CENTER_VIEW
            refresh()
        })
        allergenes.addActionListener( {
            selection = Main.ALLERGENES_CENTER_VIEW
            refresh()
        })
    }

    void refresh(){
        Main.switchView(selection)
    }
}
