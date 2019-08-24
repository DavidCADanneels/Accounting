package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

public class MealsPanel extends JPanel {
    private final MealsDataTableModel mealsDataTableModel;
    private final SelectableTable<Meal> overviewTable;
    private final RecipeDataTableModel recipeDataTableModel;
    private final SelectableTable<RecipeLine> recipeTable;

    public MealsPanel(Accounting accounting) {
        Meals meals = accounting.getMeals();
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

        JButton addRecipeLine = new JButton("Add Ingredient (+ amount)");
        addRecipeLine.addActionListener(e -> {

        });
        add(addRecipeLine, BorderLayout.SOUTH);
    }

    private void updateSelection() {
        Meal meal = overviewTable.getSelectedObject();
        Recipe recipe = meal.getRecipe();
        recipeDataTableModel.setRecipe(recipe);
    }


    public void fireMealUsageUpdated() {
        mealsDataTableModel.fireTableDataChanged();
    }
}