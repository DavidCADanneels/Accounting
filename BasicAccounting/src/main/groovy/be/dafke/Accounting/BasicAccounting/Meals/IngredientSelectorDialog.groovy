package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.Ingredients
import be.dafke.ComponentModel.RefreshableDialog

import javax.swing.JButton
import javax.swing.JPanel
import java.awt.BorderLayout

class IngredientSelectorDialog extends RefreshableDialog {
    JButton ok
    IngredientSelectorPanel selectorPanel
    static IngredientSelectorDialog selectorDialog = null

    IngredientSelectorDialog(Ingredients ingredients) {
        this(ingredients, "Select Account")
    }
    IngredientSelectorDialog(Ingredients ingredients, String title) {
        super(title)
        selectorPanel = new IngredientSelectorPanel(ingredients)
        JPanel innerPanel = new JPanel(new BorderLayout())
        innerPanel.add(selectorPanel, BorderLayout.CENTER)

        ok = new JButton("Ok (Close popup)")
        ok.addActionListener({ e -> dispose() })
        innerPanel.add(ok, BorderLayout.SOUTH)

        setContentPane(innerPanel)
        setIngredients(ingredients)
        pack()
    }

    static IngredientSelectorDialog getIngredientSelector(Ingredients ingredients, String title){
        if(selectorDialog ==null){
            selectorDialog = new IngredientSelectorDialog(ingredients, title)
        } else selectorDialog.setTitle(title)
        selectorDialog
    }

    static IngredientSelectorDialog getIngredientSelector(Ingredients ingredients){
        if(selectorDialog ==null){
            selectorDialog = new IngredientSelectorDialog(ingredients)
        }
        selectorDialog
    }

    Ingredient getSelection() {
        selectorPanel.getSelection()
    }

    void setIngredients(Ingredients ingredients) {
        selectorPanel.setIngredients(ingredients)
    }

    static void fireAccountDataChangedForAll() {
        if(selectorDialog){
            selectorDialog.fireIngredientsDataChanged()
        }
    }

    void fireIngredientsDataChanged() {
        selectorPanel.fireIngredientsDataChanged()
    }
}
