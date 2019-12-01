package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.IngredientOrder
import be.dafke.Accounting.BusinessModel.IngredientOrderItem
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class IngredientOrderViewDataTableModel extends SelectableTableModel<IngredientOrderItem> {
    static int QUANTITY_COL = 0
    static int UNIT_COL = 1
    static int INGREDIENT_NAME_COL = 2
    static int ARTICLE_COL = 3
    static int NR_OF_COL = 4
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
        columnClasses.put(INGREDIENT_NAME_COL, BigDecimal.class)
        columnClasses.put(ARTICLE_COL, Article.class)
    }

    protected void setColumnNames() {
        columnNames.put(QUANTITY_COL, getBundle("Accounting").getString("QUANTITY"))
        columnNames.put(UNIT_COL, getBundle("Accounting").getString("INGREDIENT_UNIT"))
        columnNames.put(INGREDIENT_NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"))
        columnNames.put(ARTICLE_COL, getBundle("Accounting").getString("ARTICLE"))
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        IngredientOrderItem orderItem = getObject(row, col)
        if (orderItem == null) null
        Ingredient ingredient = orderItem.getIngredient()
        if (col == ARTICLE_COL) {
            Article article = orderItem.getArticle()
            article==null?null:article.getName()
        }
        if (col == INGREDIENT_NAME_COL) {
            ingredient == null?null:ingredient.getName()
        }
        if (col == QUANTITY_COL) {
            orderItem.getQuantity()
        }
        if (col == UNIT_COL) {
            ingredient == null?null:ingredient.getUnit()
        }
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        if(order==null) 0
        List<IngredientOrderItem> businessObjects = order.getBusinessObjects()
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
        // No editable fields
    }

    @Override
    IngredientOrderItem getObject(int row, int col) {
        List<IngredientOrderItem> orderItems = order.getBusinessObjects()
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