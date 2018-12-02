package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class SalesOrderDetailTable extends JPanel {

    private final SelectableTable<OrderItem> table;
    private final SalesOrdersViewDataTableModel salesOrdersViewDataTableModel;
    private final SaleTotalsPanel saleTotalsPanel;

    public SalesOrderDetailTable(SaleTotalsPanel saleTotalsPanel) {
        this.saleTotalsPanel = saleTotalsPanel;
        salesOrdersViewDataTableModel = new SalesOrdersViewDataTableModel();
        table = new SelectableTable<>(salesOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    public SalesOrder getOrder(){
        return salesOrdersViewDataTableModel.getOrder();
    }

    public void setOrder(SalesOrder salesOrder){
        salesOrdersViewDataTableModel.setOrder(salesOrder);
        saleTotalsPanel.fireOrderContentChanged(salesOrder);
    }
}
