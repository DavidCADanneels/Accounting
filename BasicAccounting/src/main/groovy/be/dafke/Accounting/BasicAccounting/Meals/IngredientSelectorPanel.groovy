package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.Ingredients
import be.dafke.Accounting.BusinessModel.Unit
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class IngredientSelectorPanel extends JPanel {
    final JButton addIngredient
    Ingredients ingredients
    Ingredient ingredient
    JComboBox<Ingredient> combo
    DefaultComboBoxModel<Ingredient> model
    ArrayList<AccountType> accountTypes

    IngredientSelectorPanel(Ingredients ingredients) {
        this.ingredients = ingredients
        this.accountTypes = accountTypes

        model = new DefaultComboBoxModel<>()
        combo = new JComboBox<>(model)
        combo.addActionListener({ e -> ingredient = (Ingredient) combo.selectedItem })
        ingredients.businessObjects.forEach({ ingredient -> model.addElement(ingredient) })

        addIngredient = new JButton("Add Ingredient")
        addIngredient.addActionListener({ e ->
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            if (name != null) {
                try {
                    ingredients.addBusinessObject(new Ingredient(name, Unit.PIECE))
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY)
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim())
                }
            }
        })

        add(combo)
        add(addIngredient)
    }

    Ingredient getSelection() {
        ingredient
    }
}
