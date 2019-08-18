package be.dafke.BasicAccounting.Trade;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;

public class IngredientOrderCreatePanel extends JPanel {
    private IngredientOrder ingredientOrder;
    private Ingredients ingredients;
    private final IngredientOrderCreateDataTableModel ingredientOrderCreateDataTableModel;

    IngredientOrderCreatePanel(Accounting accounting) {
        this.ingredients = accounting.getIngredients();
        ingredientOrder = new IngredientOrder();
        ingredientOrder.setIngredients(ingredients);

        ingredientOrderCreateDataTableModel = new IngredientOrderCreateDataTableModel();
        ingredientOrderCreateDataTableModel.setOrder(ingredientOrder);
        ingredientOrderCreateDataTableModel.setIngredients(ingredients);
        SelectableTable<IngredientOrderItem> table = new SelectableTable<>(ingredientOrderCreateDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        JButton orderButton = new JButton("Add Ingredient Order");
        orderButton.addActionListener(e -> {
            IngredientOrders ingredientOrders = accounting.getIngredientOrders();
            try {
                ingredientOrder.removeEmptyOrderItems();
                IngredientOrder existing = ingredientOrders.getBusinessObject(ingredientOrder.getName());
                if(existing==null) {
                    ingredientOrders.addBusinessObject(ingredientOrder);
                }
                ingredientOrder = new IngredientOrder();
                ingredientOrder.setIngredients(ingredients);
                ingredientOrderCreateDataTableModel.setOrder(ingredientOrder);
                SalesOrdersOverviewGUI.fireSalesOrderAddedOrRemovedForAll();
            } catch (EmptyNameException | DuplicateNameException e1) {
                e1.printStackTrace();
            }
        });
        JPanel south = new JPanel(new BorderLayout());
        south.add(orderButton, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    public void setIngredientOrder(IngredientOrder ingredientOrder) {
        this.ingredientOrder = ingredientOrder;
        ingredientOrderCreateDataTableModel.setOrder(ingredientOrder);
//        ingredientOrderCreateDataTableModel.fireTableDataChanged();
    }

}