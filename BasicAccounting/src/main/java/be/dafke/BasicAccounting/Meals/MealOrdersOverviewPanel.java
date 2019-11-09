package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class MealOrdersOverviewPanel extends JPanel {

    private final JButton createButton;
    private SelectableTable<MealOrder> overviewTable;
    private SelectableTable<MealOrderItem> detailsTable;
    private MealOrdersOverviewDataTableModel overviewTableModel;
    private MealOrderViewDataTableModel detailsTableModel;
    private DeliveryTotalsPanel totalsPanel;
    private boolean multiSelection;

    public MealOrdersOverviewPanel(Accounting accounting) {
        overviewTableModel = new MealOrdersOverviewDataTableModel(accounting.getMealOrders());
        overviewTable = new SelectableTable<>(overviewTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        detailsTableModel = new MealOrderViewDataTableModel();
        detailsTable = new SelectableTable<>(detailsTableModel);
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200));

        totalsPanel = new DeliveryTotalsPanel();

        createButton = new JButton("Create new Order");
        createButton.addActionListener(e -> {
            MealOrderCreateGUI mealOrdersGui = MealOrderCreateGUI.getInstance(accounting);
            mealOrdersGui.setLocation(getLocationOnScreen());
            mealOrdersGui.setVisible(true);
        });

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        overviewTable.setSelectionModel(selection);

        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        JScrollPane detailScroll = new JScrollPane(detailsTable);
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT);

        JPanel north = createFilterPane();
        north.add(createButton);

        JPanel center = new JPanel(new BorderLayout());
        center.add(north, BorderLayout.NORTH);
        center.add(splitPane, BorderLayout.CENTER);
        center.add(totalsPanel, BorderLayout.SOUTH);

        add(center);

        totalsPanel.clear();
    }

    private JPanel createFilterPane() {
        JPanel panel = new JPanel();
        JCheckBox showSummary = new JCheckBox("Combine selected orders");
        showSummary.setSelected(false);
        showSummary.addActionListener(e -> {
            multiSelection = showSummary.isSelected();
            updateSelection();
        });
        panel.add(showSummary);
        return panel;
    }

    private void updateSelection() {
        MealOrder mealOrder;
        if(multiSelection) {
            ArrayList<MealOrder> selectedObjects = overviewTable.getSelectedObjects();
            mealOrder = MealOrders.mergeOrders(selectedObjects);
        } else {
            mealOrder = overviewTable.getSelectedObject();
        }
        detailsTableModel.setMealOrder(mealOrder);
        BigDecimal totalPrice = BigDecimal.ZERO.setScale(2);
        if(mealOrder != null){
            totalPrice = mealOrder.getTotalPrice();
        }
        totalsPanel.setSalesAmountInclVat(totalPrice);
        totalsPanel.calculateTotals();
    }

    public void fireOrderAdded(Accounting accounting, MealOrder mealOrder) {
        MealOrders mealOrders = accounting.getMealOrders();
        overviewTableModel.setMealOrders(mealOrders);
        overviewTableModel.fireTableDataChanged();
        selectOrder(mealOrder);
    }

    private void selectOrder(MealOrder mealOrder) {
        int row = overviewTableModel.getRow(mealOrder);
        overviewTable.setRowSelectionInterval(row, row);
        Rectangle cellRect = overviewTable.getCellRect(row, 0, false);
        overviewTable.scrollRectToVisible(cellRect);
    }
}
