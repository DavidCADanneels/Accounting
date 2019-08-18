package be.dafke.BasicAccounting.Trade;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class BuyIngredientsPanel extends JPanel {

    private final Ingredients ingredients;
    private JButton addIngredient;
    private final BuyIngredientsDataTableModel dataTableModel;
    private final SelectableTable<IngredientOrderItem> table;
    private IngredientOrder ingredientOrder;

    public BuyIngredientsPanel(Accounting accounting) {
        setLayout(new BorderLayout());

        ingredients = accounting.getIngredients();

        ingredientOrder = new IngredientOrder();
        ingredientOrder.setIngredients(ingredients);
        dataTableModel = new BuyIngredientsDataTableModel(ingredientOrder, ingredients);
        table = new SelectableTable<>(dataTableModel);

        JScrollPane scrollPane = new JScrollPane(table);

//        JPanel center = new JPanel(new BorderLayout());
//        center.add(scrollPane, BorderLayout.CENTER);
//        add(center, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);


//        add(promoOrderDetailPanel, BorderLayout.EAST);


        addIngredient = new JButton("add Ingredient");
        add(addIngredient, BorderLayout.NORTH);
        addIngredient.addActionListener(e -> {

        });
    }

    public void fireIngredientAddedOrRemoved() {
        dataTableModel.fireTableDataChanged();
    }

    public void setAccounting(Accounting accounting) {
        dataTableModel.setAccounting(accounting);
//        dataTableModel.fireTableDataChanged();
    }


    public void setIngredientsOrder(IngredientOrder ingredientsOrder){
        dataTableModel.setOrder(ingredientsOrder);
    }

}