package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.IngredientOrder
import be.dafke.Accounting.BusinessModel.IngredientOrderItem
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class IngredientOrdersOverviewPanel extends JPanel {

    Accounting accounting
    JButton addIngredient

    final IngredientOrderViewDataTableModel detailsTableModel
    final SelectableTable<IngredientOrderItem> detailsTable

    final IngredientOrdersOverviewDataTableModel overviewTableModel
    final SelectableTable<IngredientOrder> overviewTable

    IngredientOrdersOverviewPanel() {
        setLayout(new BorderLayout())

        overviewTableModel = new IngredientOrdersOverviewDataTableModel()
        overviewTable = new SelectableTable<>(overviewTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        detailsTableModel = new IngredientOrderViewDataTableModel()
        detailsTable = new SelectableTable<>(detailsTableModel)
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200))

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel()
        selectionModel.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        overviewTable.setSelectionModel(selectionModel)

        JScrollPane overviewScroll = new JScrollPane(overviewTable)
        JScrollPane detailScroll = new JScrollPane(detailsTable)
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT)
        add(splitPane, BorderLayout.CENTER)


        addIngredient = new JButton("Create Ingredient Order")
        add(addIngredient, BorderLayout.NORTH)
        addIngredient.addActionListener({ e ->
            IngredientOrderCreateGUI ingredientOrderCreateGUI = IngredientOrderCreateGUI.showIngredientsOrderCreateGUI(accounting)
            ingredientOrderCreateGUI.visible = true
        })
    }

    void updateSelection() {
//        if(multiSelection) {
//            ArrayList<IngredientOrder> selectedObjects = overviewTable.selectedObjects
//            IngredientOrder combinedOrder = IngredientOrders.mergeOrders(selectedObjects)
//            detailsTableModel.setOrder(combinedOrder)
//        } else {
        IngredientOrder ingredientOrder = overviewTable.selectedObject
        detailsTableModel.setOrder(ingredientOrder)
//        }
    }
}
