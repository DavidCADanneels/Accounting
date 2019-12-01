package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.IngredientOrderItem
import be.dafke.Accounting.BusinessModel.Ingredients

class IngredientOrderCreateDataTableModel extends IngredientOrderViewDataTableModel {

    private Ingredients ingredients

    IngredientOrderCreateDataTableModel() {
        super()
        fireTableDataChanged()
    }

    void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients
        fireTableDataChanged()
    }

    int getColumnCount() {
        NR_OF_COL
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
        col == QUANTITY_COL || col == ARTICLE_COL
    }

    // DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        IngredientOrderItem ingredientOrderItem = getObject(row, col)
        if(col == QUANTITY_COL) {
            BigDecimal quantity = (BigDecimal) value
            ingredientOrderItem.setQuantity(quantity)
        }
        if(col == ARTICLE_COL) {
            Article article = (Article) value
            ingredientOrderItem.setArticle(article)
        }
    }

    @Override
    int getRowCount() {
        if(ingredients == null) 0
        List<Ingredient> businessObjects = ingredients.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) 0
        businessObjects.size()
    }

    private List<Ingredient> getIngredients(){
        if(ingredients==null) null
        List<Ingredient> businessObjects = ingredients.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) null
        businessObjects
    }

    @Override
    IngredientOrderItem getObject(int row, int col) {
        List<Ingredient> articleList = getIngredients()
        if(articleList == null) null
        Ingredient ingredient = articleList.get(row)
        order.getBusinessObject(ingredient.getName())
    }

//	void setAccounting(Accounting accounting) {
//		setIngredients(accounting==null?null:accounting.getIngredients())
//		fireTableDataChanged()
//	}
}