package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class SalesOrderDetailsDataTableModel extends SelectableTableModel<OrderItem> {
    static int NR_OF_ITEMS_COL = 0
    static int ITEMS_PER_UNIT_COL = 1
    static int NAME_COL = 2
    static int PO_COL = 3
    static int PRICE_ITEM_COL = 4
    static int VAT_RATE_COL = 5
    static int TOTAL_EXCL_COL = 6
    static int TOTAL_VAT_COL = 7
    static int TOTAL_INCL_COL = 8
    static int NR_OF_COL = 9
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    protected SalesOrder order

    SalesOrderDetailsDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(NR_OF_ITEMS_COL, Integer.class)
        columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class)
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(PRICE_ITEM_COL, BigDecimal.class)
        columnClasses.put(TOTAL_EXCL_COL, BigDecimal.class)
        columnClasses.put(TOTAL_VAT_COL, BigDecimal.class)
        columnClasses.put(TOTAL_INCL_COL, BigDecimal.class)
        columnClasses.put(PO_COL, PurchaseOrder.class)
        columnClasses.put(VAT_RATE_COL, Integer.class)
    }

    void setColumnNames() {
        columnNames.put(NR_OF_ITEMS_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"))
        columnNames.put(ITEMS_PER_UNIT_COL, getBundle("Accounting").getString("ITEMS_PER_UNIT"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"))
        columnNames.put(PRICE_ITEM_COL, getBundle("Accounting").getString("PRICE_ITEM"))
        columnNames.put(TOTAL_EXCL_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"))
        columnNames.put(TOTAL_VAT_COL, getBundle("Accounting").getString("TOTAL_VAT"))
        columnNames.put(TOTAL_INCL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"))
        columnNames.put(PO_COL, getBundle("Accounting").getString("PO"))
        columnNames.put(VAT_RATE_COL, getBundle("Accounting").getString("VAT_RATE"))
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        OrderItem orderItem = getObject(row, col)
        if (orderItem==null) return null
        if (col == NAME_COL) return orderItem.name
        if (col == ITEMS_PER_UNIT_COL) return orderItem.itemsPerUnit
        if (col == PRICE_ITEM_COL) return orderItem.salesPriceForItem
        if (col == PO_COL) return orderItem.purchaseOrder
        if (col == VAT_RATE_COL) return orderItem.salesVatRate
        if (col == TOTAL_EXCL_COL) return orderItem.salesPriceWithoutVat
        if (col == TOTAL_INCL_COL) return orderItem.salesPriceWithVat
        if (col == TOTAL_VAT_COL) return orderItem.salesVatAmount
        if (col == NR_OF_ITEMS_COL) return orderItem.numberOfItems
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        order?order.businessObjects.size():0
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        false
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        // No editable fields
    }

    @Override
    OrderItem getObject(int row, int col) {
        List<OrderItem> orderItems = order.businessObjects
        if(orderItems == null || orderItems.size() == 0) null
        orderItems.get(row)
    }

    SalesOrder getOrder() {
        order
    }

    void setOrder(SalesOrder order) {
        this.order = order
        fireTableDataChanged()
    }

}
