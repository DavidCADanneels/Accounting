package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
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
    private HashMap<Integer,String> columnNames = new HashMap<>()
    private HashMap<Integer,Class> columnClasses = new HashMap<>()
    protected PurchaseOrder order

    PurchaseOrderViewDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    private void setColumnClasses() {
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

    private void setColumnNames() {
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
        if (order == null) null

        OrderItem orderItem = getObject(row, col)
        if(orderItem ==null) null

        Article article = orderItem.getArticle()
        if (article == null) null
        if (col == INGREDIENT_COL){
            article.getIngredient()
        }
        if (col == NAME_COL) {
            article.getName()
        }
        if (col == VAT_RATE_COL) {
            orderItem.getPurchaseVatRate()
        }
        if (col == HS_COL) {
            article.getHSCode()
        }
        if (col == PRICE_UNIT_COL) {
            orderItem.getPurchasePriceForUnit()
        }
        if (col == PRICE_TOTAL_EXCL_COL) {
            orderItem.getPurchasePriceWithoutVat()
        }
        if (col == VAT_AMOUNT_COL) {
            orderItem.getPurchaseVatAmount()
        }
        if (col == PRICE_TOTAL_INCL_COL) {
            orderItem.getPurchasePriceWithVat()
        }
        if (col == NR_OF_UNITS_COL) {
            orderItem.getNumberOfUnits()
        }
        if (col == NR_OF_ITEMS_COL) {
            orderItem.getNumberOfItems()
        }
        if (col == ITEMS_PER_UNIT_COL) {
            orderItem.getItemsPerUnit()
        }
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        if(order==null) 0
        List<OrderItem> businessObjects = order.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) 0
        businessObjects.size()
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
        List<OrderItem> orderItems = order.getBusinessObjects()
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
