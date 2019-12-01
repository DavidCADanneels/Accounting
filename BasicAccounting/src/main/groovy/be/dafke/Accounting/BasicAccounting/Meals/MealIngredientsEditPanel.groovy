package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class MealIngredientsEditPanel extends JPanel {
    final MealsEditDataTableModel mealsEditDataTableModel
    final SelectableTable<Meal> overviewTable
    final MealIngredientsEditDataTableModel mealRecipeDataTableModel
    final SelectableTable<RecipeLine> recipeTable
    final JButton addRecipeLine

    MealIngredientsEditPanel(Accounting accounting) {
        mealsEditDataTableModel = new MealsEditDataTableModel(this, accounting)
        overviewTable = new SelectableTable<>(mealsEditDataTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200))
//        overviewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

        mealRecipeDataTableModel = new MealIngredientsEditDataTableModel()
        recipeTable = new SelectableTable<>(mealRecipeDataTableModel)
        recipeTable.setPreferredScrollableViewportSize(new Dimension(500, 200))
        recipeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel()
        selectionModel.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        overviewTable.setSelectionModel(selectionModel)
        overviewTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

        JPanel overviewPanel = new JPanel()
        JScrollPane overviewScroll = new JScrollPane(overviewTable)
        overviewPanel.setLayout(new BorderLayout())
        overviewPanel.add(overviewScroll, BorderLayout.CENTER)

        JPanel detailPanel = new JPanel()
        JScrollPane detailScroll = new JScrollPane(recipeTable)
        detailPanel.setLayout(new BorderLayout())
        detailPanel.add(detailScroll, BorderLayout.CENTER)

        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT)

        setLayout(new BorderLayout())
        add(splitPane, BorderLayout.CENTER)

        addRecipeLine = new JButton("Add Ingredient")
        addRecipeLine.enabled = false
        addRecipeLine.addActionListener({ e ->
            Meal meal = overviewTable.selectedObject
            if (meal != null) {
                Ingredients ingredients = accounting.ingredients
                IngredientSelectorDialog ingredientSelector = IngredientSelectorDialog.getIngredientSelector(ingredients)
                ingredientSelector.visible = true
                Ingredient ingredient = ingredientSelector.getSelection()
                BigDecimal amount = BigDecimal.ZERO
                RecipeLine recipeLine = new RecipeLine(ingredient)
                recipeLine.setAmount(amount)
                recipeLine.setIngredient(ingredient)
                Recipe recipe = meal.getRecipe()
                try {
                    recipe.addBusinessObject(recipeLine)
                    Main.fireRecipeDataUpdated(accounting)
                } catch (EmptyNameException | DuplicateNameException e1) {
                    e1.printStackTrace()
                }
                mealRecipeDataTableModel.fireTableDataChanged()
            }
        })
        add(addRecipeLine, BorderLayout.SOUTH)
    }

    void updateSelection() {
        Meal meal = overviewTable.selectedObject
        addRecipeLine.enabled = meal!=null
        Recipe recipe = meal==null?null:meal.getRecipe()
        mealRecipeDataTableModel.setRecipe(recipe)
        mealRecipeDataTableModel.fireTableDataChanged()
//        int rowCount = recipeTable.getRowCount()
//        if(rowCount >0){
//            recipeTable.setRowSelectionInterval(0, rowCount - 1)
//        }
    }


    void fireTableUpdate() {
        mealsEditDataTableModel.fireTableDataChanged()
    }
}