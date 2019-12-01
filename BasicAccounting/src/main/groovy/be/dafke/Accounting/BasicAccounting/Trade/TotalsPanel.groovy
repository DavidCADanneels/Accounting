package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Order
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.SalesOrder

import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.GridLayout

class TotalsPanel  extends JPanel {
    protected JLabel net0pct, net6pct, net21pct
    protected JLabel vat0pct, vat6pct, vat21pct
    protected JLabel total0pct, total6pct, total21pct
    protected JLabel totalNet, totalVatRest, totalVat, total

    TotalsPanel() {
        setLayout(new GridLayout(0,4))

        net0pct = new JLabel("0.00", 10)
        net6pct = new JLabel("0.00",10)
        net21pct = new JLabel("0.00",10)

        vat0pct = new JLabel("0.00",10)
        vat6pct = new JLabel("0.00",10)
        vat21pct = new JLabel("0.00",10)

        total0pct = new JLabel("0.00",10)
        total6pct = new JLabel("0.00",10)
        total21pct = new JLabel("0.00",10)

        totalNet = new JLabel("0.00",10)
        totalVat = new JLabel("0.00",10)
        total = new JLabel("0.00",10)

        totalVatRest = new JLabel("(0.00)", 10)

        add(new JLabel("Totals (excl. VAT)"))
        add(new JLabel("VAT Rate"))
        add(new JLabel("VAT Amount"))
        add(new JLabel("Totals (incl. VAT)"))

        add(net0pct)
        add(new JLabel("0 %"))
        add(vat0pct)
        add(total0pct)

        add(net6pct)
        add(new JLabel("6 %"))
        add(vat6pct)
        add(total6pct)

        add(net21pct)
        add(new JLabel("21 %"))
        add(vat21pct)
        add(total21pct)

        add(totalNet)
        add(totalVatRest)
        add(totalVat)
        add(total)
    }

    void reset(){
        net0pct.setText("0.00")
        net6pct.setText("0.00")
        net21pct.setText("0.00")

        vat6pct.setText("0.00")
        vat21pct.setText("0.00")

        total0pct.setText("0.00")
        total6pct.setText("0.00")
        total21pct.setText("0.00")

        totalNet.setText("0.00")
        totalVatRest.setText("(0.00)")
        totalVat.setText("0.00")
        total.setText("0.00")
    }

    void fireOrderContentChanged(Order order){
        if(order==null){
            reset()
        } else if (order instanceof SalesOrder) {
            setTotals((SalesOrder)order)
        } else if (order instanceof PurchaseOrder) {
            setTotals((PurchaseOrder)order)
        } else {
            setTotals(order)
        }
    }

    void setTotals(SalesOrder order){
        net0pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(0)).toString())
        net6pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(6)).toString())
        net21pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(21)).toString())

        vat6pct.setText(order.getTotalSalesVat(OrderItem.withSalesVatRate(6)).toString())
        vat21pct.setText(order.getTotalSalesVat(OrderItem.withSalesVatRate(21)).toString())

        total0pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(0)).toString())
        total6pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(6)).toString())
        total21pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(21)).toString())

        totalNet.setText(order.totalSalesPriceExclVat.toString())
        totalVatRest.setText("("+order.calculateTotalSalesVat().toString()+")")
        totalVat.setText(order.totalSalesVat.toString())
        total.setText(order.totalSalesPriceInclVat.toString())
    }
    void setTotals(PurchaseOrder order){
        net0pct.setText(order.getTotalPurchasePriceExclVat(OrderItem.withPurchaseVatRate(0)).toString())
        net6pct.setText(order.getTotalPurchasePriceExclVat(OrderItem.withPurchaseVatRate(6)).toString())
        net21pct.setText(order.getTotalPurchasePriceExclVat(OrderItem.withPurchaseVatRate(21)).toString())

        vat6pct.setText(order.getTotalPurchaseVat(OrderItem.withPurchaseVatRate(6)).toString())
        vat21pct.setText(order.getTotalPurchaseVat(OrderItem.withPurchaseVatRate(21)).toString())

        total0pct.setText(order.getTotalPurchasePriceInclVat(OrderItem.withPurchaseVatRate(0)).toString())
        total6pct.setText(order.getTotalPurchasePriceInclVat(OrderItem.withPurchaseVatRate(6)).toString())
        total21pct.setText(order.getTotalPurchasePriceInclVat(OrderItem.withPurchaseVatRate(21)).toString())

        totalNet.setText(order.totalPurchasePriceExclVat.toString())
        totalVatRest.setText("("+order.calculateTotalPurchaseVat().toString()+")")
        totalVat.setText(order.totalPurchaseVat.toString())
        total.setText(order.totalPurchasePriceInclVat.toString())
    }

    void setTotals(Order order){
        net0pct.setText(order.totalStockValue.toString())
        net6pct.setText("-")
        net21pct.setText("-")

        vat6pct.setText("-")
        vat21pct.setText("-")

        total0pct.setText(order.totalStockValue.toString())
        total6pct.setText("-")
        total21pct.setText("-")

        totalNet.setText(order.totalStockValue.toString())
        totalVatRest.setText("-")
        totalVat.setText("-")
        total.setText(order.totalStockValue.toString())
    }
}
