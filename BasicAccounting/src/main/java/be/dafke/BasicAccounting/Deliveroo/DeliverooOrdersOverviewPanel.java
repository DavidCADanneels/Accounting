package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class DeliverooOrdersOverviewPanel extends JPanel {

    private SelectableTable<MealOrder> overviewTable;
    private SelectableTable<MealOrderItem> detailsTable;
    private DeliverooOrdersOverviewDataTableModel overviewTableModel;
    private DeliverooOrderViewDataTableModel detailsTableModel;
    private DeliveryTotalsPanel totalsPanel;

    public DeliverooOrdersOverviewPanel(Accounting accounting) {
        overviewTableModel = new DeliverooOrdersOverviewDataTableModel(accounting.getMealOrders());
        overviewTable = new SelectableTable<>(overviewTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        detailsTableModel = new DeliverooOrderViewDataTableModel();
        detailsTable = new SelectableTable<>(detailsTableModel);
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200));

        totalsPanel = new DeliveryTotalsPanel();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                MealOrder mealOrder = overviewTable.getSelectedObject();
                detailsTableModel.setMealOrder(mealOrder);
                BigDecimal totalPrice = BigDecimal.ZERO.setScale(2);
                if(mealOrder != null){
                    totalPrice = mealOrder.getTotalPrice();
                }
                totalsPanel.setSalesAmountInclVat(totalPrice);
                totalsPanel.calculateTotals();
            }
        });
        overviewTable.setSelectionModel(selection);

        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        JScrollPane detailScroll = new JScrollPane(detailsTable);
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT);

        JPanel center = new JPanel(new BorderLayout());
        center.add(splitPane, BorderLayout.CENTER);
        center.add(totalsPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);
//        add(salesOrdersDetailPanel, BorderLayout.EAST);

        totalsPanel.clear();
    }

    public void fireOrderAdded(MealOrders mealOrders, MealOrder mealOrder) {
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
