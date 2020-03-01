package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Good
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class PurchaseOrderViewDataTableModel  extends SelectableTableModel<OrderItem> {
    static int NR_OF_UNITS_COL = 0
    static int NR_OF_ITEMS_COL = 1
    static int ITEMS_PER_UNIT_COL = 2
    static int NAME_COL = 3
    static int INGREDIENT_COL = 4
    static int HS_COL = 5
    static int PRICE_UNIT_COL = 6
    static int VAT_RATE_COL = 7
    static int PRICE_TOTAL_EXCL_COL = 8
    static int VAT_AMOUNT_COL = 9
    static int PRICE_TOTAL_INCL_COL = 10
    static int NR_OF_COL = 11
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    protected PurchaseOrder order

    PurchaseOrderViewDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(NR_OF_UNITS_COL, Integer.class)
        columnClasses.put(NR_OF_ITEMS_COL, Integer.class)
        columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class)
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(INGREDIENT_COL, Ingredient.class)
        columnClasses.put(HS_COL, String.class)
        columnClasses.put(PRICE_UNIT_COL, BigDecimal.class)
        columnClasses.put(VAT_RATE_COL, Integer.class)
        columnClasses.put(PRICE_TOTAL_EXCL_COL, BigDecimal.class)
        columnClasses.put(VAT_AMOUNT_COL, BigDecimal.class)
        columnClasses.put(PRICE_TOTAL_INCL_COL, BigDecimal.class)
    }

    void setColumnNames() {
        columnNames.put(NR_OF_UNITS_COL, getBundle("Accounting").getString("UNITS_TO_ORDER"))
        columnNames.put(NR_OF_ITEMS_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"))
        columnNames.put(ITEMS_PER_UNIT_COL, getBundle("Accounting").getString("ITEMS_PER_UNIT"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"))
        columnNames.put(INGREDIENT_COL, getBundle("Accounting").getString("INGREDIENT"))
        columnNames.put(HS_COL, getBundle("Accounting").getString("ARTICLE_HS"))
        columnNames.put(PRICE_UNIT_COL, getBundle("Accounting").getString("PRICE_UNIT"))
        columnNames.put(VAT_RATE_COL, getBundle("Accounting").getString("VAT_RATE"))
        columnNames.put(PRICE_TOTAL_EXCL_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"))
        columnNames.put(VAT_AMOUNT_COL, getBundle("Accounting").getString("TOTAL_VAT"))
        columnNames.put(PRICE_TOTAL_INCL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if (order == null) return null
        OrderItem orderItem = getObject(row, col)
        if(orderItem ==null) return null
        Article article = orderItem.article
        if (article == null) return null
        if (col == INGREDIENT_COL) return article.getIngredient()
        if (col == NAME_COL) return article.name
        if (col == VAT_RATE_COL) return orderItem.getPurchaseVatRate()
        if (col == HS_COL) {
            if(article instanceof Good) {
                Good good = (Good)article
                return good.getHSCode()
            }
            return null
        }
        if (col == PRICE_UNIT_COL) return orderItem.getPurchasePriceForUnit()
        if (col == PRICE_TOTAL_EXCL_COL) return orderItem.getPurchasePriceWithoutVat()
        if (col == VAT_AMOUNT_COL) return orderItem.getPurchaseVatAmount()
        if (col == PRICE_TOTAL_INCL_COL) return orderItem.getPurchasePriceWithVat()
        if (col == NR_OF_UNITS_COL) return orderItem.numberOfUnits
        if (col == NR_OF_ITEMS_COL) return orderItem.numberOfItems
        if (col == ITEMS_PER_UNIT_COL) return orderItem.itemsPerUnit
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
        // No editable cells !
    }

    @Override
    OrderItem getObject(int row, int col) {
        List<OrderItem> orderItems = order.businessObjects
        if(orderItems == null || orderItems.size() == 0) null
        orderItems.get(row)
    }

    PurchaseOrder getOrder() {
        order
    }

    void setOrder(PurchaseOrder order) {
        this.order = order
        fireTableDataChanged()
    }

}
