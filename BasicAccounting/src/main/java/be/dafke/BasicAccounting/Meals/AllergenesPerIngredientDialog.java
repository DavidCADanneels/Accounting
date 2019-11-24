package be.dafke.BasicAccounting.Meals;

import be.dafke.Accounting.BusinessModel.Allergenes;
import be.dafke.Accounting.BusinessModel.Ingredient;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.*;
import java.awt.*;

public class AllergenesPerIngredientDialog extends RefreshableDialog {
    private JButton ok;
    private final AllergenesPerIngredientSelectorPanel selectorPanel;

    public AllergenesPerIngredientDialog(Ingredient ingredient, Allergenes allergenes) {
        super("Allergenes Selector");
        selectorPanel = new AllergenesPerIngredientSelectorPanel(ingredient, allergenes);
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.add(selectorPanel, BorderLayout.CENTER);

        ok = new JButton("Ok (Close popup)");
        ok.addActionListener(e -> dispose());
        innerPanel.add(ok, BorderLayout.SOUTH);

        setContentPane(innerPanel);
//        setIngredient(ingredient);
        pack();
    }

//    private void setIngredient(Ingredient ingredient) {
//        selectorPanel.setIngredient(ingredient);
//    }
}
