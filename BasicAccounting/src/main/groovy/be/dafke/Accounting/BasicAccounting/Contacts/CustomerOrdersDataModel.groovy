package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.ComponentModel.SelectableTableModel

class CustomerOrdersDataModel extends SelectableTableModel<SalesOrder> {

    ArrayList<SalesOrder> salesOrders

    CustomerOrdersDataModel(ArrayList<SalesOrder> salesOrders) {
        this.salesOrders = salesOrders
    }

    @Override
    SalesOrder getObject(int row, int col) {
        return null
    }

    @Override
    int getRowCount() {
        return salesOrders.size()
    }

    @Override
    int getColumnCount() {
        return 4
    }

    @Override
    Object getValueAt(int row, int col) {
        SalesOrder salesOrder = salesOrders.get(row)
        if(col == 0) {
            salesOrder.toString()
        } else if (col==1){
            salesOrder.salesTransaction?.toString()
        } else if (col==2){
            salesOrder.paymentTransaction?.toString()
        } else if (col==3){
            salesOrder.gainTransaction?.toString()
        } else null
    }
}
