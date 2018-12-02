package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class SalesOrdersOverviewPanel extends JPanel {
    private final SelectableTable<SalesOrder> table;
    private final SalesOrdersOverviewDataTableModel tableModel;
    private final SalesOrderDetailTable salesOrderDetailTable;
    private final SaleTotalsPanel saleTotalsPanel;
    private final JButton createSalesOrder;
    private final SalesOrdersDetailPanel salesOrdersDetailPanel;

    public SalesOrdersOverviewPanel(Accounting accounting) {
        tableModel = new SalesOrdersOverviewDataTableModel(accounting.getSalesOrders());
        table = new SelectableTable<>(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        saleTotalsPanel = new SaleTotalsPanel();

        salesOrderDetailTable = new SalesOrderDetailTable(saleTotalsPanel);
        salesOrdersDetailPanel = new SalesOrdersDetailPanel(accounting);

        fireSalesOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
                int rowInModel = rowSorter.convertRowIndexToModel(selectedRow);

                SalesOrder salesOrder = tableModel.getObject(rowInModel, 0);

                salesOrderDetailTable.setOrder(salesOrder);
                salesOrdersDetailPanel.setOrder(salesOrder);
            }
        });
        table.setSelectionModel(selection);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel details = new JPanel(new BorderLayout());
        details.add(salesOrderDetailTable, BorderLayout.CENTER);
        details.add(saleTotalsPanel, BorderLayout.SOUTH);

        createSalesOrder = new JButton(getBundle("Accounting").getString("CREATE_SO"));
        createSalesOrder.addActionListener(e -> {
            SalesOrderCreateGUI salesOrderCreateGUI = SalesOrderCreateGUI.showSalesOrderGUI(accounting);
            salesOrderCreateGUI.setLocation(getLocationOnScreen());
            salesOrderCreateGUI.setVisible(true);
        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(salesOrdersDetailPanel);
        rightPanel.add(createSalesOrder, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(Main.createSplitPane(scrollPane, details, JSplitPane.VERTICAL_SPLIT), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    public void fireSalesOrderAddedOrRemoved() {
        tableModel.fireTableDataChanged();
    }
}