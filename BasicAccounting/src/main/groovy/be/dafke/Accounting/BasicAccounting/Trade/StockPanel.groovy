package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.ComponentModel.SelectableTable

import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JScrollPane
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout

import static java.util.ResourceBundle.getBundle

class StockPanel extends JPanel {
    final JButton viewPurchaseOrder , viewSalesOrder
    final SelectableTable<Article> table
    final StockDataTableModel stockDataTableModel

    StockPanel() {
        stockDataTableModel = new StockDataTableModel()
        table = new SelectableTable<>(stockDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400))
        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)

        viewPurchaseOrder = new JButton(getBundle("Accounting").getString("VIEW_PO"))
        viewPurchaseOrder.addActionListener({ e ->
            PurchaseOrdersOverviewGUI purchaseOrdersViewGUI = PurchaseOrdersOverviewGUI.showPurchaseOrderGUI()
            purchaseOrdersViewGUI.setLocation(getLocationOnScreen())
            purchaseOrdersViewGUI.visible = true
        })

        viewSalesOrder = new JButton(getBundle("Accounting").getString("VIEW_SO"))
        viewSalesOrder.addActionListener({ e ->
            SalesOrdersOverviewGUI salesOrdersViewGUI = SalesOrdersOverviewGUI.showSalesOrderGUI()
            salesOrdersViewGUI.setLocation(getLocationOnScreen())
            salesOrdersViewGUI.visible = true
        })
        add(createFilterPanel(), BorderLayout.NORTH)
        add(createButtonPanel(), BorderLayout.SOUTH)
    }

    JPanel createFilterPanel(){
        JPanel panel = new JPanel()
        JRadioButton withOrders = new JRadioButton("with Orders")
        JRadioButton inStock = new JRadioButton("in Stock")
        withOrders.addActionListener({ e -> stockDataTableModel.setWithOrders(true) })
        inStock.addActionListener({ e -> stockDataTableModel.setWithOrders(false) })
        ButtonGroup group = new ButtonGroup()
        group.add(withOrders)
        group.add(inStock)
        inStock.selected = true
        stockDataTableModel.setWithOrders(false)
        panel.add(inStock)
        panel.add(withOrders)
        panel
    }

    JPanel createButtonPanel(){
        JPanel panel = new JPanel(new GridLayout(0,2))
        panel.add(viewPurchaseOrder)
        panel.add(viewSalesOrder)
        panel
    }
}
