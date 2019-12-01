package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class MealRecipeEditPanel extends JPanel {
    final MealsEditDataTableModel mealsEditDataTableModel
    final SelectableTable<Meal> overviewTable
    final MealRecipeEditDataTableModel mealRecipeEditDataTableModel
    final SelectableTable<RecipeLine> recipeTable
    final JButton addRecipeLine

    MealRecipeEditPanel(Accounting accounting) {
        mealsEditDataTableModel = new MealsEditDataTableModel(this, accounting)
        overviewTable = new SelectableTable<>(mealsEditDataTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200))

        mealRecipeEditDataTableModel = new MealRecipeEditDataTableModel()
        recipeTable = new SelectableTable<>(mealRecipeEditDataTableModel)
        recipeTable.setPreferredScrollableViewportSize(new Dimension(500, 200))

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel()
        selectionModel.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        overviewTable.setSelectionModel(selectionModel)

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

        addRecipeLine = new JButton("Add Ingredient (+ amount)")
        addRecipeLine.enabled = false
        addRecipeLine.addActionListener({ e ->
            Meal meal = overviewTable.selectedObject
            if (meal != null) {
                Ingredients ingredients = accounting.ingredients
                IngredientSelectorDialog ingredientSelector = IngredientSelectorDialog.getIngredientSelector(ingredients)
                ingredientSelector.visible = true
                Ingredient ingredient = ingredientSelector.getSelection()
                String amountString = JOptionPane.showInputDialog(this, "get amount")
                BigDecimal amount = amountString == null ? BigDecimal.ZERO : new BigDecimal(amountString)
                RecipeLine recipeLine = new RecipeLine(ingredient)
                recipeLine.setAmount(amount)
                recipeLine.setIngredient(ingredient)
                Recipe recipe = meal.getRecipe()
                try {
                    recipe.addBusinessObject(recipeLine)
                } catch (EmptyNameException | DuplicateNameException e1) {
                    e1.printStackTrace()
                }
                mealRecipeEditDataTableModel.fireTableDataChanged()
            }
        })
        add(addRecipeLine, BorderLayout.SOUTH)
    }

    void updateSelection() {
        Meal meal = overviewTable.selectedObject
        addRecipeLine.enabled = meal!=null
        Recipe recipe = meal==null?null:meal.getRecipe()
        mealRecipeEditDataTableModel.setRecipe(recipe)
        mealRecipeEditDataTableModel.fireTableDataChanged()
//        int rowCount = recipeTable.getRowCount()
//        if(rowCount >0){
//            recipeTable.setRowSelectionInterval(0, rowCount - 1)
//        }
    }


    void fireTableUpdate() {
        mealsEditDataTableModel.fireTableDataChanged()
    }
}