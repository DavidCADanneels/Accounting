package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PromoOrder
import be.dafke.Accounting.BusinessModel.PromoOrders
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class PromoOrdersOverviewPanel extends JPanel {
    final SelectableTable<PromoOrder> overviewTable
    final SelectableTable<OrderItem> detailsTable
    final PromoOrdersOverviewDataTableModel overviewTableModel

    final PromoOrderViewDataTableModel detailsTableModel
    final TotalsPanel totalsPanel

    final PromoOrderDetailPanel promoOrderDetailPanel
    final SalesOrderDetailsPopupMenu popup
    boolean multiSelection

    PromoOrdersOverviewPanel() {
        overviewTableModel = new PromoOrdersOverviewDataTableModel()
        overviewTable = new SelectableTable<>(overviewTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        detailsTableModel = new PromoOrderViewDataTableModel()
        detailsTable = new SelectableTable<>(detailsTableModel)
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200))
        //
        popup = new SalesOrderDetailsPopupMenu(detailsTable)
        detailsTable.addMouseListener(PopupForTableActivator.getInstance(popup,detailsTable))

        totalsPanel = new TotalsPanel()
        promoOrderDetailPanel = new PromoOrderDetailPanel()

        firePromoOrderAddedOrRemoved()

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

        JPanel center = new JPanel(new BorderLayout())
        center.add(createFilterPane(), BorderLayout.NORTH)
        center.add(splitPane, BorderLayout.CENTER)
        center.add(totalsPanel, BorderLayout.SOUTH)

        setLayout(new BorderLayout())
        add(center, BorderLayout.CENTER)
        add(promoOrderDetailPanel, BorderLayout.EAST)
    }

    void updateSelection() {
        if(multiSelection) {
            ArrayList<PromoOrder> selectedObjects = overviewTable.selectedObjects
            PromoOrder combinedOrder = PromoOrders.mergeOrders(selectedObjects)
            detailsTableModel.setOrder(combinedOrder)
            totalsPanel.fireOrderContentChanged(combinedOrder)
            promoOrderDetailPanel.setOrder(combinedOrder)
            promoOrderDetailPanel.disableButtons()
        } else {
            PromoOrder promoOrder = overviewTable.selectedObject
            detailsTableModel.setOrder(promoOrder)
            totalsPanel.fireOrderContentChanged(promoOrder)
            promoOrderDetailPanel.setOrder(promoOrder)
            promoOrderDetailPanel.updateButtonsAndCheckBoxes()
        }
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

    void firePromoOrderAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged()
    }
}
