package be.dafke.Accounting.BasicAccounting.MainApplication

import javax.swing.*

class MealViewSelectorPanel extends JPanel {

    JToggleButton ingredients, allergenes

    ButtonGroup buttonGroup

    MealViewSelectorPanel() {
        ingredients = new JToggleButton('Ingredients', true)
        allergenes = new JToggleButton('Allergenes')

        buttonGroup = new ButtonGroup()

        buttonGroup.add ingredients
        buttonGroup.add allergenes

        add ingredients
        add allergenes

        ingredients.addActionListener( {
            Main.switchView(Main.INGREDIENTS_CENTER_VIEW)
        })
        allergenes.addActionListener( {
            Main.switchView(Main.ALLERGENES_CENTER_VIEW)
        })
    }
}
