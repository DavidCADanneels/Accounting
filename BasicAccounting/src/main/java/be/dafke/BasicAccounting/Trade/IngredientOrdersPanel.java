package be.dafke.BasicAccounting.Trade;


import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.IngredientOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class IngredientOrdersPanel extends JPanel {

    private JButton addIngredient;
    private final IngredientOrderOverviewDataTableModel dataTableModel;
    private final SelectableTable<IngredientOrder> table;

    public IngredientOrdersPanel() {
        setLayout(new BorderLayout());

        dataTableModel = new IngredientOrderOverviewDataTableModel();
        table = new SelectableTable<>(dataTableModel);

        JScrollPane scrollPane = new JScrollPane(table);

//        JPanel center = new JPanel(new BorderLayout());
//        center.add(scrollPane, BorderLayout.CENTER);
//        add(center, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);


//        add(promoOrderDetailPanel, BorderLayout.EAST);


        addIngredient = new JButton("add Ingredient Order");
        add(addIngredient, BorderLayout.NORTH);
        addIngredient.addActionListener(e -> {

        });
    }

    public void setAccounting(Accounting accounting) {
        dataTableModel.setAccounting(accounting);
//        dataTableModel.fireTableDataChanged();
    }


}