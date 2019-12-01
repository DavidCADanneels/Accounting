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
    private final MealsEditDataViewTableModel mealsDataTableModel
    private final SelectableTable<Meal> overviewTable
    private final IngredientsViewPanel ingredientsViewPanel

    MealIngredientsViewPanel(Accounting accounting) {
        mealsDataTableModel = new MealsEditDataViewTableModel(this, accounting)
        overviewTable = new SelectableTable<>(mealsDataTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200))

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel()
        selectionModel.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        overviewTable.setSelectionModel(selectionModel)

        ingredientsViewPanel = new IngredientsViewPanel(accounting, true)

        JPanel overviewPanel = new JPanel()
        JScrollPane overviewScroll = new JScrollPane(overviewTable)
        overviewPanel.setLayout(new BorderLayout())
        overviewPanel.add(overviewScroll, BorderLayout.CENTER)

//        JPanel detailPanel = new JPanel()
//        JScrollPane detailScroll = new JScrollPane(ingredientsViewPanel)
//        detailPanel.setLayout(new BorderLayout())
//        detailPanel.add(detailScroll, BorderLayout.CENTER)

        JSplitPane splitPane = Main.createSplitPane(overviewScroll, ingredientsViewPanel, JSplitPane.VERTICAL_SPLIT)

        setLayout(new BorderLayout())
        add(splitPane, BorderLayout.CENTER)
    }

    private void updateSelection() {
        boolean multiSelection = true
        if(multiSelection){
            Ingredients ingredients = new Ingredients()
            ArrayList<Meal> selectedObjects = overviewTable.getSelectedObjects()
            selectedObjects.forEach({ meal ->
                Recipe recipe = meal.getRecipe()
                recipe.getIngredients().getBusinessObjects().forEach({ ingredient ->
                    try {
                        ingredients.addBusinessObject(ingredient)
                    } catch (EmptyNameException | DuplicateNameException e) {
                        e.printStackTrace()
                    }
                })
            })
            ingredientsViewPanel.setIngredients(ingredients)
        } else {
            Meal meal = overviewTable.getSelectedObject()
            Recipe recipe = meal == null ? null : meal.getRecipe()
            Ingredients ingredients = recipe == null ? null : recipe.getIngredients()
            ingredientsViewPanel.setIngredients(ingredients)
        }
    }


    void fireTableUpdate() {
        mealsDataTableModel.fireTableDataChanged()
    }
}