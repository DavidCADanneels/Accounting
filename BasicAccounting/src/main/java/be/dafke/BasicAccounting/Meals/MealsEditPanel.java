package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

public class MealsEditPanel extends JPanel {
    private final MealsDataTableModel mealsDataTableModel;
    private final SelectableTable<Meal> overviewTable;
    private final RecipeDataTableModel recipeDataTableModel;
    private final SelectableTable<RecipeLine> recipeTable;
    private final JButton addRecipeLine;

    public MealsEditPanel(Accounting accounting) {
        mealsDataTableModel = new MealsDataTableModel(this, accounting);
        overviewTable = new SelectableTable<>(mealsDataTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        recipeDataTableModel = new RecipeDataTableModel();
        recipeTable = new SelectableTable<>(recipeDataTableModel);
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

        addRecipeLine = new JButton("Add Ingredient (+ amount)");
        addRecipeLine.setEnabled(false);
        addRecipeLine.addActionListener(e -> {
            Meal meal = overviewTable.getSelectedObject();
            if(meal!=null) {
                Ingredients ingredients = accounting.getIngredients();
                IngredientSelectorDialog ingredientSelector = IngredientSelectorDialog.getIngredientSelector(ingredients);
                ingredientSelector.setVisible(true);
                Ingredient ingredient = ingredientSelector.getSelection();
                String amountString = JOptionPane.showInputDialog(this, "get amount");
                BigDecimal amount = amountString==null?BigDecimal.ZERO:new BigDecimal(amountString);
                RecipeLine recipeLine = new RecipeLine(ingredient);
                recipeLine.setAmount(amount);
                recipeLine.setIngredient(ingredient);
                Recipe recipe = meal.getRecipe();
                try {
                    recipe.addBusinessObject(recipeLine);
                } catch (EmptyNameException | DuplicateNameException e1) {
                    e1.printStackTrace();
                }
                recipeDataTableModel.fireTableDataChanged();
            }
        });
        add(addRecipeLine, BorderLayout.SOUTH);
    }

    private void updateSelection() {
        Meal meal = overviewTable.getSelectedObject();
        addRecipeLine.setEnabled(meal!=null);
        Recipe recipe = meal==null?null:meal.getRecipe();
        recipeDataTableModel.setRecipe(recipe);
        recipeDataTableModel.fireTableDataChanged();
//        int rowCount = recipeTable.getRowCount();
//        if(rowCount >0){
//            recipeTable.setRowSelectionInterval(0, rowCount - 1);
//        }
    }


    public void fireMealUsageUpdated() {
        mealsDataTableModel.fireTableDataChanged();
    }
}