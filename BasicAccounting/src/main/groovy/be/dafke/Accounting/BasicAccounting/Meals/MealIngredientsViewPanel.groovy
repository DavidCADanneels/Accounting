package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Ingredients
import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Accounting.BusinessModel.Recipe
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class MealIngredientsViewPanel extends JPanel {
    final MealsViewDataTableModel mealsDataTableModel
    final SelectableTable<Meal> overviewTable
    final IngredientsViewPanel ingredientsViewPanel

    MealIngredientsViewPanel(Accounting accounting) {
        mealsDataTableModel = new MealsViewDataTableModel(this, accounting)
        overviewTable = new SelectableTable<>(mealsDataTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200))

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel()
        selectionModel.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        overviewTable.setSelectionModel(selectionModel)

        ingredientsViewPanel = new IngredientsViewPanel()
        ingredientsViewPanel.ingredients = accounting.ingredients

        JPanel overviewPanel = new JPanel()
        JScrollPane overviewScroll = new JScrollPane(overviewTable)
        overviewPanel.setLayout(new BorderLayout())
        overviewPanel.add(overviewScroll, BorderLayout.CENTER)

        JSplitPane splitPane = Main.createSplitPane(overviewScroll, ingredientsViewPanel, JSplitPane.VERTICAL_SPLIT)

        setLayout(new BorderLayout())
        add(splitPane, BorderLayout.CENTER)
    }

    void updateSelection() {
        boolean multiSelection = true
        if(multiSelection){
            Ingredients ingredients = new Ingredients()
            ArrayList<Meal> selectedObjects = overviewTable.selectedObjects
            selectedObjects.forEach({ meal ->
                Recipe recipe = meal.getRecipe()
                recipe.ingredients.businessObjects.forEach({ ingredient ->
                    try {
                        ingredients.addBusinessObject(ingredient)
                    } catch (EmptyNameException | DuplicateNameException e) {
                        e.printStackTrace()
                    }
                })
            })
            ingredientsViewPanel.setIngredients(ingredients)
            ingredientsViewPanel.selectAll()
        } else {
            Meal meal = overviewTable.selectedObject
            Recipe recipe = meal == null ? null : meal.getRecipe()
            Ingredients ingredients = recipe == null ? null : recipe.ingredients
            ingredientsViewPanel.setIngredients(ingredients)
            ingredientsViewPanel.selectAll()
        }
    }


    void fireTableUpdate() {
        mealsDataTableModel.fireTableDataChanged()
    }
}