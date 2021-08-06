package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.DefaultListSelectionModel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Rectangle

class PurchaseOrdersOverviewPanel extends JPanel {
    final SelectableTable<PurchaseOrder> overviewTable
    final SelectableTable<OrderItem> detailsTable
    final PurchaseOrdersOverviewDataTableModel overviewTableModel
    final PurchaseOrderViewDataTableModel detailsTableModel
    final TotalsPanel totalsPanel

    final PurchaseOrdersDetailPanel purchaseOrdersDetailPanel

    PurchaseOrdersOverviewPanel() {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Purchase Orders"))
        overviewTableModel = new PurchaseOrdersOverviewDataTableModel()
        overviewTable = new SelectableTable<>(overviewTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        detailsTableModel = new PurchaseOrderViewDataTableModel()
        detailsTable = new SelectableTable<>(detailsTableModel)
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200))

        totalsPanel = new TotalsPanel()

        purchaseOrdersDetailPanel = new PurchaseOrdersDetailPanel()

        DefaultListSelectionModel selection = new DefaultListSelectionModel()
        selection.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                PurchaseOrder purchaseOrder = overviewTable.selectedObject
                detailsTableModel.setOrder(purchaseOrder)
                totalsPanel.fireOrderContentChanged(purchaseOrder)
                purchaseOrdersDetailPanel.setOrder(purchaseOrder)
            }
        })
        overviewTable.setSelectionModel(selection)

        JScrollPane overviewScroll = new JScrollPane(overviewTable)
        overviewScroll.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Overview"))
        JScrollPane detailScroll = new JScrollPane(detailsTable)
        detailScroll.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Details"))
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT)

        JPanel center = new JPanel(new BorderLayout())
        center.add(splitPane, BorderLayout.CENTER)
        center.add(totalsPanel, BorderLayout.SOUTH)

        setLayout(new BorderLayout())
        add(center, BorderLayout.CENTER)
        add(purchaseOrdersDetailPanel, BorderLayout.EAST)
    }

    void selectOrder(PurchaseOrder purchaseOrder) {
        int row = overviewTableModel.getRow(purchaseOrder)
        if(row!=-1){
            overviewTable.setRowSelectionInterval(row,row)
            Rectangle cellRect = overviewTable.getCellRect(row, 0, false)
            overviewTable.scrollRectToVisible(cellRect)
        }
    }

    void refresh(){
        overviewTableModel.fireTableDataChanged()
        detailsTableModel.fireTableDataChanged()
    }
}
