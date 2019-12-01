package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PromoOrder
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class PromoOrderViewDataTableModel extends SelectableTableModel<OrderItem> {
    static int NR_OF_ITEMS_COL = 0
    static int NAME_COL = 1
    static int PURCHASE_PRICE_ITEM_COL = 2
    static int PURCHASE_PRICE_UNIT_COL = 3
    static int TOTAL_PURCHASE_PRICE_COL = 4
    static int NR_OF_COL = 5
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    protected PromoOrder order

    PromoOrderViewDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(NR_OF_ITEMS_COL, Integer.class)
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(PURCHASE_PRICE_ITEM_COL, BigDecimal.class)
        columnClasses.put(PURCHASE_PRICE_UNIT_COL, BigDecimal.class)
        columnClasses.put(TOTAL_PURCHASE_PRICE_COL, BigDecimal.class)
    }

    void setColumnNames() {
        columnNames.put(NR_OF_ITEMS_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"))
        columnNames.put(PURCHASE_PRICE_ITEM_COL, getBundle("Accounting").getString("PRICE_ITEM"))
        columnNames.put(PURCHASE_PRICE_UNIT_COL, getBundle("Accounting").getString("PRICE_UNIT"))
        columnNames.put(TOTAL_PURCHASE_PRICE_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"))
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        OrderItem orderItem = getObject(row, col)
        if (orderItem==null) return null
        if (col == NAME_COL) return orderItem.name
        if (col == PURCHASE_PRICE_ITEM_COL) return orderItem.getPurchasePriceForItem()
        if (col == PURCHASE_PRICE_UNIT_COL) return orderItem.getPurchasePriceForUnit()
        if (col == TOTAL_PURCHASE_PRICE_COL) return orderItem.getStockValue()
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
        if(order==null) null
        List<OrderItem> orderItems = order.businessObjects
        if(orderItems == null || orderItems.size() == 0) null
        orderItems.get(row)
    }

    PromoOrder getOrder() {
        order
    }

    void setOrder(PromoOrder order) {
        this.order = order
        fireTableDataChanged()
    }

}
