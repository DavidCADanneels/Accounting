package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.IngredientOrder;
import be.dafke.BusinessModel.IngredientOrderItem;
import be.dafke.BusinessModel.IngredientOrders;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;

public class IngredientOrdersOverviewPanel extends JPanel {

    private Accounting accounting;
    private JButton addIngredient;

    private final IngredientOrderViewDataTableModel detailsTableModel;
    private final SelectableTable<IngredientOrderItem> detailsTable;

    private final IngredientOrdersOverviewDataTableModel overviewTableModel;
    private final SelectableTable<IngredientOrder> overviewTable;

    public IngredientOrdersOverviewPanel() {
        setLayout(new BorderLayout());

        overviewTableModel = new IngredientOrdersOverviewDataTableModel();
        overviewTable = new SelectableTable<>(overviewTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        detailsTableModel = new IngredientOrderViewDataTableModel();
        detailsTable = new SelectableTable<>(detailsTableModel);
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200));

        fireIngredientAddedOrRemoved();

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        overviewTable.setSelectionModel(selectionModel);

        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        JScrollPane detailScroll = new JScrollPane(detailsTable);
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT);
        add(splitPane, BorderLayout.CENTER);


        addIngredient = new JButton("Create Ingredient Order");
        add(addIngredient, BorderLayout.NORTH);
        addIngredient.addActionListener(e -> {
            IngredientOrderCreateGUI ingredientOrderCreateGUI = IngredientOrderCreateGUI.showIngredientsOrderCreateGUI(accounting);
            ingredientOrderCreateGUI.setVisible(true);
        });
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        overviewTableModel.setAccounting(accounting);
//        detailsTableModel.setAccounting(accounting);
//        overviewTableModel.fireTableDataChanged();
    }


    private void updateSelection() {
//        if(multiSelection) {
//            ArrayList<IngredientOrder> selectedObjects = overviewTable.getSelectedObjects();
//            IngredientOrder combinedOrder = IngredientOrders.mergeOrders(selectedObjects);
//            detailsTableModel.setOrder(combinedOrder);
//        } else {
        IngredientOrder ingredientOrder = overviewTable.getSelectedObject();
        detailsTableModel.setOrder(ingredientOrder);
//        }
    }


    public void fireIngredientAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged();
    }


}