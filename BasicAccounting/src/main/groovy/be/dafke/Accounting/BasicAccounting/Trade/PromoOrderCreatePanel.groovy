package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PromoOrder
import be.dafke.Accounting.BusinessModel.PromoOrders
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JScrollPane
import java.awt.BorderLayout
import java.awt.Dimension

class PromoOrderCreatePanel extends JPanel {
    PromoOrder promoOrder
    Articles articles
    final PromoOrderCreateDataTableModel promoOrderCreateDataTableModel

    PromoOrderCreatePanel(Accounting accounting) {
        this.articles = accounting.articles
        promoOrder = new PromoOrder()
        promoOrder.articles = articles

        TotalsPanel totalsPanel = new TotalsPanel()
        promoOrderCreateDataTableModel = new PromoOrderCreateDataTableModel(articles, promoOrder, totalsPanel)
        SelectableTable<OrderItem> table = new SelectableTable<>(promoOrderCreateDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400))


        JPanel north = new JPanel()

        JButton orderButton = new JButton("Add Promo Order")
        orderButton.addActionListener({ e ->
            PromoOrders promoOrders = accounting.promoOrders

            try {
                promoOrder.removeEmptyOrderItems()

                promoOrders.addBusinessObject promoOrder
                promoOrder = new PromoOrder()
                promoOrder.articles = articles
                promoOrderCreateDataTableModel.order = promoOrder
                totalsPanel.fireOrderContentChanged promoOrder
                PromoOrdersOverviewGUI.firePromoOrderAddedOrRemovedForAccounting accounting
            } catch (EmptyNameException e1) {
                e1.printStackTrace()
            } catch (DuplicateNameException e1) {
                e1.printStackTrace()
            }
        })
        JPanel south = new JPanel(new BorderLayout())
        south.add(orderButton, BorderLayout.SOUTH)
        south.add(totalsPanel, BorderLayout.CENTER)

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())

        add(scrollPane, BorderLayout.CENTER)
        add(north, BorderLayout.NORTH)
        add(south, BorderLayout.SOUTH)
    }

    void setPromoOrder(PromoOrder promoOrder) {
        this.promoOrder = promoOrder
        promoOrderCreateDataTableModel.setOrder(promoOrder)
        promoOrderCreateDataTableModel.fireTableDataChanged()
    }
}
