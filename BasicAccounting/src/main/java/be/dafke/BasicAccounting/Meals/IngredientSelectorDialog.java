package be.dafke.BasicAccounting.Meals;

import be.dafke.Accounting.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class IngredientSelectorDialog extends RefreshableDialog {
	private JButton ok;
	private IngredientSelectorPanel selectorPanel;
	private static IngredientSelectorDialog selectorDialog = null;

	private IngredientSelectorDialog(Ingredients ingredients) {
		this(ingredients, "Select Account");
	}
	public IngredientSelectorDialog(Ingredients ingredients, String title) {
		super(title);
		selectorPanel = new IngredientSelectorPanel(ingredients);
		JPanel innerPanel = new JPanel(new BorderLayout());
		innerPanel.add(selectorPanel, BorderLayout.CENTER);

		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(e -> dispose());
		innerPanel.add(ok, BorderLayout.SOUTH);

		setContentPane(innerPanel);
		setIngredients(ingredients);
		pack();
	}

	public static IngredientSelectorDialog getIngredientSelector(Ingredients ingredients, String title){
		if(selectorDialog ==null){
			selectorDialog = new IngredientSelectorDialog(ingredients, title);
		} else selectorDialog.setTitle(title);
		return selectorDialog;
	}

	public static IngredientSelectorDialog getIngredientSelector(Ingredients ingredients){
		if(selectorDialog ==null){
			selectorDialog = new IngredientSelectorDialog(ingredients);
		}
		return selectorDialog;
	}

	public Ingredient getSelection() {
		return selectorPanel.getSelection();
	}

    public void setIngredients(Ingredients ingredients) {
		selectorPanel.setIngredients(ingredients);
    }

	public static void fireAccountDataChangedForAll() {
		if(selectorDialog !=null){
			selectorDialog.fireIngredientsDataChanged();
		}
	}

	public void fireIngredientsDataChanged() {
		selectorPanel.fireIngredientsDataChanged();
	}
}
