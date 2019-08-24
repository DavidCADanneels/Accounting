package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class IngredientSelectorPanel extends JPanel {
    private final JButton addIngredient;
    private Ingredients ingredients;
    private Ingredient ingredient;
    private JComboBox<Ingredient> combo;
    private DefaultComboBoxModel<Ingredient> model;
    private ArrayList<AccountType> accountTypes;

    IngredientSelectorPanel(Ingredients ingredients) {
        this.ingredients = ingredients;
        this.accountTypes = accountTypes;
        model = new DefaultComboBoxModel<>();
        combo = new JComboBox<>(model);
        combo.addActionListener(e -> ingredient = (Ingredient) combo.getSelectedItem());
        addIngredient = new JButton("Add Ingredient");
        addIngredient.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            if (name != null) {
                try {
                    ingredients.addBusinessObject(new Ingredient(name, Unit.PIECE));
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY);
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim());
                }
            }
        });
        add(combo);
        add(addIngredient);
        setIngredients(ingredients);
    }

    public Ingredient getSelection() {
        return ingredient;
    }

    public void fireIngredientsDataChanged() {
        model.removeAllElements();
        ingredients.getBusinessObjects().forEach(ingredient -> model.addElement(ingredient));
        invalidate();
        combo.invalidate();
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
        fireIngredientsDataChanged();
    }
}
