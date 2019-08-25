package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class MealIngredientsEditPanel extends JPanel {
    private final MealsEditDataTableModel mealsEditDataTableModel;
    private final SelectableTable<Meal> overviewTable;
    private final MealIngredientsEditDataTableModel mealRecipeDataTableModel;
    private final SelectableTable<RecipeLine> recipeTable;
    private final JButton addRecipeLine;

    public MealIngredientsEditPanel(Accounting accounting) {
        mealsEditDataTableModel = new MealsEditDataTableModel(this, accounting);
        overviewTable = new SelectableTable<>(mealsEditDataTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        mealRecipeDataTableModel = new MealIngredientsEditDataTableModel();
        recipeTable = new SelectableTable<>(mealRecipeDataTableModel);
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

        addRecipeLine = new JButton("Add Ingredient");
        addRecipeLine.setEnabled(false);
        addRecipeLine.addActionListener(e -> {
            Meal meal = overviewTable.getSelectedObject();
            if(meal!=null) {
                Ingredients ingredients = accounting.getIngredients();
                IngredientSelectorDialog ingredientSelector = IngredientSelectorDialog.getIngredientSelector(ingredients);
                ingredientSelector.setVisible(true);
                Ingredient ingredient = ingredientSelector.getSelection();
                BigDecimal amount = BigDecimal.ZERO;
                RecipeLine recipeLine = new RecipeLine(ingredient);
                recipeLine.setAmount(amount);
                recipeLine.setIngredient(ingredient);
                Recipe recipe = meal.getRecipe();
                try {
                    recipe.addBusinessObject(recipeLine);
                    Main.fireRecipeDataUpdated(accounting);
                } catch (EmptyNameException | DuplicateNameException e1) {
                    e1.printStackTrace();
                }
                mealRecipeDataTableModel.fireTableDataChanged();
            }
        });
        add(addRecipeLine, BorderLayout.SOUTH);
    }

    private void updateSelection() {
        Meal meal = overviewTable.getSelectedObject();
        addRecipeLine.setEnabled(meal!=null);
        Recipe recipe = meal==null?null:meal.getRecipe();
        mealRecipeDataTableModel.setRecipe(recipe);
        mealRecipeDataTableModel.fireTableDataChanged();
//        int rowCount = recipeTable.getRowCount();
//        if(rowCount >0){
//            recipeTable.setRowSelectionInterval(0, rowCount - 1);
//        }
    }


    public void fireTableUpdate() {
        mealsEditDataTableModel.fireTableDataChanged();
    }
}