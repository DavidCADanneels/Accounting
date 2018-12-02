package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.SalesOrder;

import javax.swing.*;
import java.awt.*;

public class SaleTotalsPanel extends TotalsPanel{

    public void fireOrderContentChanged(SalesOrder order){
        if(order==null){
            reset();
        } else {
            net0pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(0)).toString());
            net6pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(6)).toString());
            net21pct.setText(order.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(21)).toString());

            vat6pct.setText(order.getTotalSalesVat(OrderItem.withSalesVatRate(6)).toString());
            vat21pct.setText(order.getTotalSalesVat(OrderItem.withSalesVatRate(21)).toString());

            total0pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(0)).toString());
            total6pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(6)).toString());
            total21pct.setText(order.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(21)).toString());

            totalNet.setText(order.getTotalSalesPriceExclVat().toString());
            totalVatRest.setText("("+order.calculateTotalSalesVat().toString()+")");
            totalVat.setText(order.getTotalSalesVat().toString());
            total.setText(order.getTotalSalesPriceInclVat().toString());
        }
    }
}
