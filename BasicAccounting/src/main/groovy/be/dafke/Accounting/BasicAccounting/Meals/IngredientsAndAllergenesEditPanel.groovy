package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import java.awt.BorderLayout

class IngredientsAndAllergenesEditPanel extends JPanel {

    final IngredientsEditPanel ingredientsEditPanel
    final AllergenesEditPanel allergenesEditPanel

    IngredientsAndAllergenesEditPanel(Accounting accounting) {
        ingredientsEditPanel = new IngredientsEditPanel(accounting)
        allergenesEditPanel = new AllergenesEditPanel(accounting.allergenes)

        setLayout(new BorderLayout())

        JScrollPane leftScroll = new JScrollPane(ingredientsEditPanel)
        JScrollPane rightScroll = new JScrollPane(allergenesEditPanel)
        JSplitPane splitPane = Main.createSplitPane(leftScroll, rightScroll, JSplitPane.HORIZONTAL_SPLIT)

        add(splitPane, BorderLayout.CENTER)
    }
}
