package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.IngredientOrder
import be.dafke.Accounting.BusinessModel.IngredientOrderItem
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class IngredientOrderViewDataTableModel extends SelectableTableModel<IngredientOrderItem> {
    static int QUANTITY_COL = 0
    static int UNIT_COL = 1
    static int INGREDIENT_NAME_COL = 2
    static int INGREDIENT_AMOUNT_COL = 3
    static int ARTICLE_COL = 4
    static int SUPPLIER_COL = 5
    static int NR_OF_COL = 6
    protected HashMap<Integer,String> columnNames = new HashMap<>()
    protected HashMap<Integer,Class> columnClasses = new HashMap<>()
    protected IngredientOrder order

    IngredientOrderViewDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    protected void setColumnClasses() {
        columnClasses.put(QUANTITY_COL, BigDecimal.class)
        columnClasses.put(UNIT_COL, String.class)
        columnClasses.put(INGREDIENT_NAME_COL, String.class)
        columnClasses.put(INGREDIENT_AMOUNT_COL, BigDecimal.class)
        columnClasses.put(ARTICLE_COL, Article.class)
        columnClasses.put(SUPPLIER_COL, Contact.class)
    }

    protected void setColumnNames() {
        columnNames.put(QUANTITY_COL, getBundle("Accounting").getString("QUANTITY"))
        columnNames.put(UNIT_COL, getBundle("Accounting").getString("INGREDIENT_UNIT"))
        columnNames.put(INGREDIENT_NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"))
        columnNames.put(INGREDIENT_AMOUNT_COL, getBundle("Accounting").getString("INGREDIENT_AMOUNT"))
        columnNames.put(ARTICLE_COL, getBundle("Accounting").getString("ARTICLE"))
        columnNames.put(SUPPLIER_COL, getBundle("Accounting").getString("SUPPLIER"))
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        IngredientOrderItem orderItem = getObject(row, col)
        if (orderItem == null) return null
        Ingredient ingredient = orderItem.getIngredient()
        if (col == ARTICLE_COL) {
            Article article = orderItem.article
            return article?article.name:null
        }
        if (col == INGREDIENT_NAME_COL) return ingredient?ingredient.name:null
        if (col == INGREDIENT_AMOUNT_COL) {
            Article article = orderItem.article
            return article?article.ingredientAmount:null
        }
        if (col == SUPPLIER_COL) {
            Article article = orderItem.article
            return article?article.supplier:null
        }
        if (col == QUANTITY_COL) return orderItem.quantity
        if (col == UNIT_COL) return ingredient?ingredient.unit:null
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
    IngredientOrderItem getObject(int row, int col) {
        List<IngredientOrderItem> orderItems = order.businessObjects
        if(orderItems == null || orderItems.size() == 0) null
        orderItems.get(row)
    }

    IngredientOrder getOrder() {
        order
    }

    void setOrder(IngredientOrder order) {
        this.order = order
        fireTableDataChanged()
    }

}