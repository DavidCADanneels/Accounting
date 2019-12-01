package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.ComponentModel.SelectableTableModel

import java.util.function.Predicate

import static java.util.ResourceBundle.getBundle 

class StockDataTableModel extends SelectableTableModel<Article> {
    private final Articles articles
    static int UNITS_IN_STOCK_COL = 0
    static int ITEMS_PER_UNIT_COL = 1
    static int ITEMS_IN_STOCK_COL = 2
    static int ARTIKEL_COL = 3
    static int SUPPLIER_COL = 4
    private HashMap<Integer,String> columnNames = new HashMap<>()
    private HashMap<Integer,Class> columnClasses = new HashMap<>()
    private Predicate<Article> filter

    StockDataTableModel(Articles articles) {
        this.articles = articles
        setColumnNames()
        setColumnClasses()
    }

    private void setColumnClasses() {
        columnClasses.put(UNITS_IN_STOCK_COL, Integer.class)
        columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class)
        columnClasses.put(ITEMS_IN_STOCK_COL, Integer.class)
        columnClasses.put(ARTIKEL_COL, Article.class)
        columnClasses.put(SUPPLIER_COL, Contact.class)
    }

    private void setColumnNames() {
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
        if(article == null) null
        if (col == UNITS_IN_STOCK_COL) {
            article.getNrInStock()/article.getItemsPerUnit()
        }
        if (col == ITEMS_IN_STOCK_COL) {
            article.getNrInStock()
        }
        if (col == ITEMS_PER_UNIT_COL) {
            article.getItemsPerUnit()
        }
        if (col == ARTIKEL_COL) {
            article
        }
        if (col == SUPPLIER_COL) {
            article.getSupplier()
        }
        null
    }

    int getColumnCount() {
        columnNames.size()
    }

    int getRowCount() {
        if(articles == null || filter == null){
            0
        }
        articles.getBusinessObjects(filter).size()
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
