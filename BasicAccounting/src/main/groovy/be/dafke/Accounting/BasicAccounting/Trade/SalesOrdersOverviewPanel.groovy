package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.Accounting.BusinessModel.SalesOrders
import be.dafke.ComponentModel.SelectableTable

import javax.swing.ButtonGroup
import javax.swing.DefaultListSelectionModel
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import java.awt.BorderLayout
import java.awt.Dimension

class SalesOrdersOverviewPanel extends JPanel {
    private final SelectableTable<SalesOrder> overviewTable
    private final SelectableTable<OrderItem> detailsTable
    private final SalesOrdersOverviewDataTableModel overviewTableModel
    private final SalesOrderDetailsDataTableModel detailsTableModel
    private final TotalsPanel totalsPanel

    private final SalesOrderDetailPanel salesOrderDetailPanel
    private final SalesOrderDetailsPopupMenu popup
    private boolean multiSelection

    SalesOrdersOverviewPanel(){
        overviewTableModel = new SalesOrdersOverviewDataTableModel()
        overviewTable = new SelectableTable<>(overviewTableModel)
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        SalesOrderInvoiceColorRenderer invoice = new SalesOrderInvoiceColorRenderer()
        SalesOrderPayedColorRenderer payed = new SalesOrderPayedColorRenderer()
        overviewTable.setDefaultRenderer(BigDecimal.class, payed)
        overviewTable.setDefaultRenderer(Contact.class, invoice)
        overviewTable.setDefaultRenderer(String.class, invoice)

        detailsTableModel = new SalesOrderDetailsDataTableModel()
        detailsTable = new SelectableTable<>(detailsTableModel)
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200))
        //
        popup = new SalesOrderDetailsPopupMenu(detailsTable)
        detailsTable.addMouseListener(PopupForTableActivator.getInstance(popup,detailsTable))

        totalsPanel = new TotalsPanel()
        salesOrderDetailPanel = new SalesOrderDetailPanel()

        fireSalesOrderAddedOrRemoved()

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

        JPanel center = new JPanel(new BorderLayout())
        center.add(createFilterPane(), BorderLayout.NORTH)
        center.add(splitPane, BorderLayout.CENTER)
        center.add(totalsPanel, BorderLayout.SOUTH)

        setLayout(new BorderLayout())
        add(center, BorderLayout.CENTER)
        add(salesOrderDetailPanel, BorderLayout.EAST)
    }

    private void updateSelection() {
        if(multiSelection) {
            ArrayList<SalesOrder> selectedObjects = overviewTable.getSelectedObjects()
            SalesOrder combinedOrder = SalesOrders.mergeOrders(selectedObjects)
            detailsTableModel.setOrder(combinedOrder)
            totalsPanel.fireOrderContentChanged(combinedOrder)
            salesOrderDetailPanel.setOrder(combinedOrder)
            salesOrderDetailPanel.disableButtons()
            // Clear details
            salesOrderDetailPanel.updateContactDetails(combinedOrder)
            salesOrderDetailPanel.updateInvoiceButtonAndField()
        } else {
            SalesOrder salesOrder = overviewTable.getSelectedObject()
            detailsTableModel.setOrder(salesOrder)
            totalsPanel.fireOrderContentChanged(salesOrder)
            salesOrderDetailPanel.setOrder(salesOrder)
            salesOrderDetailPanel.updateButtonsAndCheckBoxes()
        }
    }

    private JPanel createFilterPane() {
        JRadioButton all = new JRadioButton("All")
        JRadioButton invoice = new JRadioButton("Invoice")
        JRadioButton noInvoice = new JRadioButton("Non-Invoice")
        ButtonGroup group = new ButtonGroup()
        group.add(all)
        group.add(invoice)
        group.add(noInvoice)

        all.setSelected(true)
        all.addActionListener({ e ->
            overviewTableModel.setFilter(null)
            overviewTableModel.fireTableDataChanged()
            overviewTable.revalidate()
        })

        invoice.addActionListener({ e ->
            overviewTableModel.setFilter({ salesOrder -> salesOrder.isInvoice() })
            overviewTableModel.fireTableDataChanged()
            overviewTable.revalidate()
        })

        noInvoice.addActionListener({ e ->
            overviewTableModel.setFilter({ salesOrder -> !salesOrder.isInvoice() })
            overviewTableModel.fireTableDataChanged()
            overviewTable.revalidate()
        })

        JPanel panel = new JPanel()
        panel.add(all)
        panel.add(invoice)
        panel.add(noInvoice)

        JCheckBox showSummary = new JCheckBox("Combine selected orders")
        showSummary.setSelected(false)
        showSummary.addActionListener({ e ->
            multiSelection = showSummary.isSelected()
            updateSelection()
        })
        panel.add(showSummary)
        panel
    }

    void fireSalesOrderAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged()
    }

    void setAccounting(Accounting accounting) {
        overviewTableModel.setAccounting(accounting)
        popup.setAccounting(accounting)
        salesOrderDetailPanel.setAccounting(accounting)
    }
}
