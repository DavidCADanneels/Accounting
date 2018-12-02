package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.PurchaseOrder;

public class PurchaseTotalsPanel extends TotalsPanel{

    public void fireOrderContentChanged(PurchaseOrder order){
        if(order==null){
            reset();
        } else {
            net0pct.setText(order.getTotalPurchasePriceExclVat(OrderItem.withPurchaseVatRate(0)).toString());
            net6pct.setText(order.getTotalPurchasePriceExclVat(OrderItem.withPurchaseVatRate(6)).toString());
            net21pct.setText(order.getTotalPurchasePriceExclVat(OrderItem.withPurchaseVatRate(21)).toString());

            vat6pct.setText(order.getTotalPurchaseVat(OrderItem.withPurchaseVatRate(6)).toString());
            vat21pct.setText(order.getTotalPurchaseVat(OrderItem.withPurchaseVatRate(21)).toString());

            total0pct.setText(order.getTotalPurchasePriceInclVat(OrderItem.withPurchaseVatRate(0)).toString());
            total6pct.setText(order.getTotalPurchasePriceInclVat(OrderItem.withPurchaseVatRate(6)).toString());
            total21pct.setText(order.getTotalPurchasePriceInclVat(OrderItem.withPurchaseVatRate(21)).toString());

            totalNet.setText(order.getTotalPurchasePriceExclVat().toString());
            totalVatRest.setText("("+order.calculateTotalPurchaseVat().toString()+")");
            totalVat.setText(order.getTotalPurchaseVat().toString());
            total.setText(order.getTotalPurchasePriceInclVat().toString());
        }
    }
}
