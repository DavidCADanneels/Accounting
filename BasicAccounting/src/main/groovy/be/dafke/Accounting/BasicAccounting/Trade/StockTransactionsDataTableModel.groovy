package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.*
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class StockTransactionsDataTableModel extends SelectableTableModel<OrderItem> {
    final StockTransactions stockTransactions
    static int ORDER_COL = 0
    static int ARTIKEL_COL = 1
    static int ADDED_COL = 2
    static int REMOVED_COL = 3
    static int NR_OF_COLS = 4
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    StockTransactionsDataTableModel(StockTransactions stockTransactions) {
        this.stockTransactions = stockTransactions
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(ORDER_COL, OrderItems.class)
        columnClasses.put(ARTIKEL_COL, Article.class)
        columnClasses.put(ADDED_COL, Integer.class)
        columnClasses.put(REMOVED_COL, Integer.class)
    }

    void setColumnNames() {
        columnNames.put(ORDER_COL, getBundle("Accounting").getString("ORDER"))
        columnNames.put(ARTIKEL_COL, getBundle("Accounting").getString("ARTICLE_NAME"))
        columnNames.put(ADDED_COL, getBundle("Accounting").getString("ORDER_ADDED"))
        columnNames.put(REMOVED_COL, getBundle("Accounting").getString("ORDER_REMOVED"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        OrderItem orderItem = getObject(row,col)
        Article article = orderItem.article
        if (col == ARTIKEL_COL) return article
        Order order = orderItem.getOrder()
        if (col == ORDER_COL) return order
        if (col == ADDED_COL) {
            if(!order) return null
            else {
                if ((order instanceof PurchaseOrder && !order.creditNote) ||
                        (order instanceof SalesOrder && order.creditNote) ||
                        (order instanceof StockOrder)){
                    return orderItem.numberOfItems
                } else return null
            }
        }
        if (col == REMOVED_COL) {
            if (!order) return null
            else {
                if ((order instanceof SalesOrder && !order.creditNote) ||
                        (order instanceof PurchaseOrder && order.creditNote) ||
                        order instanceof PromoOrder) {
                    return orderItem.numberOfItems
                } else return null
            }
        } else return null
    }

    int getColumnCount() {
        NR_OF_COLS
    }

    int getRowCount() {
        if(stockTransactions == null){
            return 0
        }
        int size = 0
        for(Order order:stockTransactions.getOrders()){
            size += order.businessObjects.size()
        }
        size
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
        // no editable fields
    }

    ArrayList<OrderItem> getAllItems(){
        ArrayList<OrderItem> orderItems = new ArrayList<>()
        for(Order order : stockTransactions.getOrders()){
            orderItems.addAll(order.businessObjects)
        }
        orderItems
    }

    @Override
    OrderItem getObject(int row, int col) {
        if(stockTransactions == null) null
        ArrayList<OrderItem> orderItems = getAllItems()
        orderItems.get(row)
    }

    int getRowInList(ArrayList<OrderItem> list, OrderItem booking){
        int row = 0
        for(OrderItem search:list){
            if(search!=booking){
                row++
            } else{
                row
            }
        }
        -1
    }

    int getRow(OrderItem orderItem) {
        if(stockTransactions == null) -1
        ArrayList<OrderItem> orderItems = getAllItems()
        getRowInList(orderItems, orderItem)
    }
}
