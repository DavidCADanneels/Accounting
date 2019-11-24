package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class MealRecipeViewPanel extends JPanel {
    private final MealsEditDataViewTableModel mealsDataTableModel;
    private final SelectableTable<Meal> overviewTable;
    private final MealRecipeViewDataTableModel mealRecipeViewDataTableModel;
    private final SelectableTable<RecipeLine> recipeTable;

    public MealRecipeViewPanel(Accounting accounting) {
        mealsDataTableModel = new MealsEditDataViewTableModel(this, accounting);
        overviewTable = new SelectableTable<>(mealsDataTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        mealRecipeViewDataTableModel = new MealRecipeViewDataTableModel();
        recipeTable = new SelectableTable<>(mealRecipeViewDataTableModel);
        recipeTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        overviewTable.setSelectionModel(selectionModel);

        JPanel overviewPanel = new JPanel();
        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        overviewPanel.setLayout(new BorderLayout());
        overviewPanel.add(overviewScroll, BorderLayout.CENTER);

        JPanel detailPanel = new JPanel();
        JScrollPane detailScroll = new JScrollPane(recipeTable);
        detailPanel.setLayout(new BorderLayout());
        detailPanel.add(detailScroll, BorderLayout.CENTER);

        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    private void updateSelection() {
        Meal meal = overviewTable.getSelectedObject();
        Recipe recipe = meal==null?null:meal.getRecipe();
        mealRecipeViewDataTableModel.setRecipe(recipe);
        mealRecipeViewDataTableModel.fireTableDataChanged();
        int rowCount = recipeTable.getRowCount();
        if(rowCount >0){
            recipeTable.setRowSelectionInterval(0, rowCount - 1);
        }
    }


    public void fireTableUpdate() {
        mealsDataTableModel.fireTableDataChanged();
    }
}