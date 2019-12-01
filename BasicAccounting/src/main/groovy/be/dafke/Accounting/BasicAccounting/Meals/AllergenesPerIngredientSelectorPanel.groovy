package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Allergene
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.ComponentModel.SelectableTable

import javax.swing.JPanel
import javax.swing.JScrollPane

class AllergenesPerIngredientSelectorPanel extends JPanel {

    final AllergenesPerIngredientSelectionDataTableModel allergenesPerIngredientSelectionDataTableModel
    final SelectableTable<Allergene> table

    AllergenesPerIngredientSelectorPanel(Ingredient ingredient, Allergenes allergenes) {
        allergenesPerIngredientSelectionDataTableModel = new AllergenesPerIngredientSelectionDataTableModel(ingredient, allergenes)
        table = new SelectableTable<>(allergenesPerIngredientSelectionDataTableModel)
        JScrollPane scrollPane = new JScrollPane(table)
        add(scrollPane)
    }
}