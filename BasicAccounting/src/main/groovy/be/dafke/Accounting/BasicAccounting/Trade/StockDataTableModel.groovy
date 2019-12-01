package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.ComponentModel.SelectableTableModel

import java.util.function.Predicate

import static java.util.ResourceBundle.getBundle 

class StockDataTableModel extends SelectableTableModel<Article> {
    final Articles articles
    static int UNITS_IN_STOCK_COL = 0
    static int ITEMS_PER_UNIT_COL = 1
    static int ITEMS_IN_STOCK_COL = 2
    static int ARTIKEL_COL = 3
    static int SUPPLIER_COL = 4
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    Predicate<Article> filter

    StockDataTableModel(Articles articles) {
        this.articles = articles
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(UNITS_IN_STOCK_COL, Integer.class)
        columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class)
        columnClasses.put(ITEMS_IN_STOCK_COL, Integer.class)
        columnClasses.put(ARTIKEL_COL, Article.class)
        columnClasses.put(SUPPLIER_COL, Contact.class)
    }

    void setColumnNames() {
        columnNames.put(UNITS_IN_STOCK_COL, getBundle("Accounting").getString("UNITS_IN_STOCK"))
        columnNames.put(ITEMS_IN_STOCK_COL, getBundle("Accounting").getString("ITEMS_IN_STOCK"))
        columnNames.put(ITEMS_PER_UNIT_COL, getBundle("Accounting").getString("ITEMS_PER_UNIT"))
        columnNames.put(ARTIKEL_COL, getBundle("Accounting").getString("ARTICLE_NAME"))
        columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Article article = getObject(row,col)
        if(article == null) return null
        if (col == UNITS_IN_STOCK_COL) return Math.floor(article.getNrInStock()/article.itemsPerUnit).intValue()
        if (col == ITEMS_IN_STOCK_COL) return article.getNrInStock()
        if (col == ITEMS_PER_UNIT_COL) return article.itemsPerUnit
        if (col == ARTIKEL_COL) return article
        if (col == SUPPLIER_COL) return article.supplier
        null
    }

    int getColumnCount() {
        columnNames.size()
    }

    int getRowCount() {
        (articles && filter)?articles.getBusinessObjects(filter).size():0
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

    @Override
    Article getObject(int row, int col) {
        if(articles == null || filter == null) null
        List<Article> businessObjects = articles.getBusinessObjects(filter)
        businessObjects.get(row)
    }

    void setFilter(Predicate<Article> filter){
        this.filter = filter
        fireTableDataChanged()
    }
}
