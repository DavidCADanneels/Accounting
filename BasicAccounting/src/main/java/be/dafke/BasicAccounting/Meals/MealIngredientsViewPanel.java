package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MealIngredientsViewPanel extends JPanel {
    private final MealsEditDataViewTableModel mealsDataTableModel;
    private final SelectableTable<Meal> overviewTable;
    private final IngredientsViewPanel ingredientsViewPanel;

    public MealIngredientsViewPanel(Accounting accounting) {
        mealsDataTableModel = new MealsEditDataViewTableModel(this, accounting);
        overviewTable = new SelectableTable<>(mealsDataTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        overviewTable.setSelectionModel(selectionModel);

        ingredientsViewPanel = new IngredientsViewPanel(accounting, true);

        JPanel overviewPanel = new JPanel();
        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        overviewPanel.setLayout(new BorderLayout());
        overviewPanel.add(overviewScroll, BorderLayout.CENTER);

//        JPanel detailPanel = new JPanel();
//        JScrollPane detailScroll = new JScrollPane(ingredientsViewPanel);
//        detailPanel.setLayout(new BorderLayout());
//        detailPanel.add(detailScroll, BorderLayout.CENTER);

        JSplitPane splitPane = Main.createSplitPane(overviewScroll, ingredientsViewPanel, JSplitPane.VERTICAL_SPLIT);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    private void updateSelection() {
        boolean multiSelection = true;
        if(multiSelection){
            Ingredients ingredients = new Ingredients();
            ArrayList<Meal> selectedObjects = overviewTable.getSelectedObjects();
            selectedObjects.forEach(meal -> {
                Recipe recipe = meal.getRecipe();
                recipe.getIngredients().getBusinessObjects().forEach(ingredient -> {
                    try {
                        ingredients.addBusinessObject(ingredient);
                    } catch (EmptyNameException | DuplicateNameException e) {
                        e.printStackTrace();
                    }
                });
            });
            ingredientsViewPanel.setIngredients(ingredients);
        } else {
            Meal meal = overviewTable.getSelectedObject();
            Recipe recipe = meal == null ? null : meal.getRecipe();
            Ingredients ingredients = recipe == null ? null : recipe.getIngredients();
            ingredientsViewPanel.setIngredients(ingredients);
        }
    }


    public void fireTableUpdate() {
        mealsDataTableModel.fireTableDataChanged();
    }
}