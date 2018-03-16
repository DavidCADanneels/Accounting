package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.PurchaseOrder;
import be.dafke.BusinessModel.SalesOrder;

import javax.swing.*;
import java.awt.*;

public class SaleTotalsPanel extends JPanel{
    private JTextField textField1, textField2, textField3;

    public SaleTotalsPanel() {
        setLayout(new GridLayout(0,2));

        textField1 = new JTextField("0.00",10);
        textField2 = new JTextField("0.00",10);
        textField3 = new JTextField("0.00",10);
        textField1.setEditable(false);
        textField2.setEditable(false);
        textField3.setEditable(false);

        add(new JLabel("Total (excl. VAT):"));
        add(textField1);
        add(new JLabel("Total VAT:"));
        add(textField2);
        add(new JLabel("Total (incl. VAT):"));
        add(textField3);
    }

    public void fireOrderContentChanged(SalesOrder order){
        textField1.setText(order==null?"":order.getTotalSalesPriceExclVat().toString());
        textField2.setText(order==null?"":order.getTotalSalesVat().toString());
        textField3.setText(order==null?"":order.getTotalSalesPriceInclVat().toString());
    }
}
