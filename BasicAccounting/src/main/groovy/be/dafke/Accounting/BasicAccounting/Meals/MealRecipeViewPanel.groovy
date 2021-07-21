package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Accounting.BusinessModel.RecipeLine
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class MealRecipeViewPanel extends JPanel {
    final MealsViewDataTableModel mealsDataTableModel
    final SelectableTable<Meal> overviewTable
    final MealRecipeViewDataTableModel mealRecipeViewDataTableModel
    final SelectableTable<RecipeLine> recipeTable

    MealRecipeViewPanel(Accounting accounting) {
        mealsDataTableModel = new MealsViewDataTableModel(this, accounting)
        overviewTable = new SelectableTable<>(mealsDataTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200))

        mealRecipeViewDataTableModel = new MealRecipeViewDataTableModel()
        recipeTable = new SelectableTable<>(mealRecipeViewDataTableModel)
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
    }

    void updateSelection() {
        Meal meal = overviewTable.selectedObject
        mealRecipeViewDataTableModel.recipe = meal?.recipe
        mealRecipeViewDataTableModel.fireTableDataChanged()
        int rowCount = recipeTable.getRowCount()
        if(rowCount >0){
            recipeTable.setRowSelectionInterval(0, rowCount - 1)
        }
    }


    void fireTableUpdate() {
        mealsDataTableModel.fireTableDataChanged()
    }
}