package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class StockDataTableModel extends SelectableTableModel<Article> {
    static int UNITS_IN_STOCK_COL = 0
    static int ITEMS_PER_UNIT_COL = 1
    static int ITEMS_IN_STOCK_COL = 2
    static int ARTIKEL_COL = 3
    static int SUPPLIER_COL = 4
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    boolean withOrders

    StockDataTableModel() {
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
        if (col == UNITS_IN_STOCK_COL) return Math.floor(Session.activeAccounting.stockTransactions.getNrInStock(article)/article.itemsPerUnit).intValue()
        if (col == ITEMS_IN_STOCK_COL) return Session.activeAccounting.stockTransactions.getNrInStock(article)
        if (col == ITEMS_PER_UNIT_COL) return article.itemsPerUnit
        if (col == ARTIKEL_COL) return article
        if (col == SUPPLIER_COL) return article.supplier
        null
    }

    int getColumnCount() {
        columnNames.size()
    }

    int getRowCount() {
        if(withOrders) {
            Session.activeAccounting.articles.getBusinessObjects(Article.withOrders()).size()
        } else Session.activeAccounting.stockTransactions.stock.keySet().size()
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
        List<Article> articleList
        if(withOrders) {
            articleList = Session.activeAccounting.articles.getBusinessObjects(Article.withOrders())
        } else articleList = Session.activeAccounting.stockTransactions.stock.keySet().collect()
        if(articleList == null) return null
        articleList.get(row)
    }

    void setWithOrders(boolean withOrders) {
        this.withOrders = withOrders
        fireTableDataChanged()
    }
}
