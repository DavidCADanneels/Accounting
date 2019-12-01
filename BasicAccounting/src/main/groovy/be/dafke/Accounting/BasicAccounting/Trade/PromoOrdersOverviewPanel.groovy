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
    private final SelectableTable<PromoOrder> overviewTable
    private final SelectableTable<OrderItem> detailsTable
    private final PromoOrdersOverviewDataTableModel overviewTableModel

    private final PromoOrderViewDataTableModel detailsTableModel
    private final TotalsPanel totalsPanel

    private final PromoOrderDetailPanel promoOrderDetailPanel
    private final SalesOrderDetailsPopupMenu popup
    private boolean multiSelection

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

    private void updateSelection() {
        if(multiSelection) {
            ArrayList<PromoOrder> selectedObjects = overviewTable.getSelectedObjects()
            PromoOrder combinedOrder = PromoOrders.mergeOrders(selectedObjects)
            detailsTableModel.setOrder(combinedOrder)
            totalsPanel.fireOrderContentChanged(combinedOrder)
            promoOrderDetailPanel.setOrder(combinedOrder)
            promoOrderDetailPanel.disableButtons()
        } else {
            PromoOrder promoOrder = overviewTable.getSelectedObject()
            detailsTableModel.setOrder(promoOrder)
            totalsPanel.fireOrderContentChanged(promoOrder)
            promoOrderDetailPanel.setOrder(promoOrder)
            promoOrderDetailPanel.updateButtonsAndCheckBoxes()
        }
    }

    private JPanel createFilterPane() {
        JPanel panel = new JPanel()
        JCheckBox showSummary = new JCheckBox("Combine selected orders")
        showSummary.setSelected(false)
        showSummary.addActionListener({ e ->
            multiSelection = showSummary.isSelected()
            updateSelection()
        })
        panel.add(showSummary)
        panel
    }

    void firePromoOrderAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged()
    }

    void setAccounting(Accounting accounting) {
        overviewTableModel.setAccounting(accounting)
        popup.setAccounting(accounting)
        promoOrderDetailPanel.setAccounting(accounting)
    }
}
