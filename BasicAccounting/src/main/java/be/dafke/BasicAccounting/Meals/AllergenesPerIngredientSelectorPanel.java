package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Allergene;
import be.dafke.BusinessModel.Allergenes;
import be.dafke.BusinessModel.Ingredient;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;

public class AllergenesPerIngredientSelectorPanel extends JPanel {

    private final AllergenesPerIngredientSelectionDataTableModel allergenesPerIngredientSelectionDataTableModel;
    private final SelectableTable<Allergene> table;

    public AllergenesPerIngredientSelectorPanel(Ingredient ingredient, Allergenes allergenes) {
        allergenesPerIngredientSelectionDataTableModel = new AllergenesPerIngredientSelectionDataTableModel(ingredient, allergenes);
        table = new SelectableTable<>(allergenesPerIngredientSelectionDataTableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
}
