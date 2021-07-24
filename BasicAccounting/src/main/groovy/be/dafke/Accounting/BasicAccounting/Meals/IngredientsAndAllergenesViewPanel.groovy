package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Ingredients

import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import java.awt.BorderLayout

class IngredientsAndAllergenesViewPanel extends JPanel {
    final IngredientsViewPanel ingredientsViewPanel
    final AllergenesViewPanel allergenesViewPanel

    Ingredients ingredients

    IngredientsAndAllergenesViewPanel(Accounting accounting) {
        ingredientsViewPanel = new IngredientsViewPanel(accounting)
        allergenesViewPanel = new AllergenesViewPanel(false)
        allergenesViewPanel.allergenes = accounting.allergenes

        setLayout(new BorderLayout())

        JScrollPane leftScroll = new JScrollPane(ingredientsViewPanel)
        JScrollPane rightScroll = new JScrollPane(allergenesViewPanel)
        JSplitPane splitPane = Main.createSplitPane(leftScroll, rightScroll, JSplitPane.HORIZONTAL_SPLIT)

        add(splitPane, BorderLayout.CENTER)
    }
}
