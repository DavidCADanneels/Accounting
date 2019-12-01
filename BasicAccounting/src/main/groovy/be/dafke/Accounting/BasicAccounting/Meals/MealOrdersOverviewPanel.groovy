package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.MealOrder
import be.dafke.Accounting.BusinessModel.MealOrderItem
import be.dafke.Accounting.BusinessModel.MealOrders
import be.dafke.ComponentModel.SelectableTable

import javax.swing.DefaultListSelectionModel
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Rectangle

class MealOrdersOverviewPanel extends JPanel {

    final JButton createButton
    SelectableTable<MealOrder> overviewTable
    SelectableTable<MealOrderItem> detailsTable
    MealOrdersOverviewDataTableModel overviewTableModel
    MealOrderViewDataTableModel detailsTableModel
    DeliveryTotalsPanel totalsPanel
    boolean multiSelection

    MealOrdersOverviewPanel(Accounting accounting) {
        overviewTableModel = new MealOrdersOverviewDataTableModel(accounting.mealOrders)
        overviewTable = new SelectableTable<>(overviewTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        detailsTableModel = new MealOrderViewDataTableModel()
        detailsTable = new SelectableTable<>(detailsTableModel)
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200))

        totalsPanel = new DeliveryTotalsPanel()

        createButton = new JButton("Create new Order")
        createButton.addActionListener({ e ->
            MealOrderCreateGUI mealOrdersGui = MealOrderCreateGUI.getInstance(accounting)
            mealOrdersGui.setLocation(getLocationOnScreen())
            mealOrdersGui.visible = true
        })

        DefaultListSelectionModel selection = new DefaultListSelectionModel()
        selection.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        overviewTable.setSelectionModel(selection)

        JScrollPane overviewScroll = new JScrollPane(overviewTable)
        JScrollPane detailScroll = new JScrollPane(detailsTable)
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT)

        JPanel north = createFilterPane()
        north.add(createButton)

        JPanel center = new JPanel(new BorderLayout())
        center.add(north, BorderLayout.NORTH)
        center.add(splitPane, BorderLayout.CENTER)
        center.add(totalsPanel, BorderLayout.SOUTH)

        add(center)

        totalsPanel.clear()
    }

    JPanel createFilterPane() {
        JPanel panel = new JPanel()
        JCheckBox showSummary = new JCheckBox("Combine selected orders")
        showSummary.selected = false
        showSummary.addActionListener({ e ->
            multiSelection = showSummary.selected
            updateSelection()
        })
        panel.add(showSummary)
        panel
    }

    void updateSelection() {
        MealOrder mealOrder
        if(multiSelection) {
            ArrayList<MealOrder> selectedObjects = overviewTable.selectedObjects
            mealOrder = MealOrders.mergeOrders(selectedObjects)
        } else {
            mealOrder = overviewTable.selectedObject
        }
        detailsTableModel.setMealOrder(mealOrder)
        BigDecimal totalPrice = BigDecimal.ZERO.setScale(2)
        if(mealOrder != null){
            totalPrice = mealOrder.getTotalPrice()
        }
        totalsPanel.setSalesAmountInclVat(totalPrice)
        totalsPanel.calculateTotals()
    }

    void fireOrderAdded(Accounting accounting, MealOrder mealOrder) {
        MealOrders mealOrders = accounting.mealOrders
        overviewTableModel.setMealOrders(mealOrders)
        overviewTableModel.fireTableDataChanged()
        selectOrder(mealOrder)
    }

    void selectOrder(MealOrder mealOrder) {
        int row = overviewTableModel.getRow(mealOrder)
        overviewTable.setRowSelectionInterval(row, row)
        Rectangle cellRect = overviewTable.getCellRect(row, 0, false)
        overviewTable.scrollRectToVisible(cellRect)
    }
}
