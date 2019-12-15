package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.StockTransactions
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class StockBalanceDataTableModel extends SelectableTableModel<Article> {
    final Accounting accounting
    static int ARTIKEL_COL = 0
    static int PURCHASE_COL = 1
    static int PURCHASE_CN_COL = 2
    static int SALE_COL = 3
    static int SALE_CN_COL = 4
    static int PROMO_COL = 5
    static int LEFT_COL = 6
    static int NR_OF_COLS = 7
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    StockBalanceDataTableModel(Accounting accounting) {
        this.accounting = accounting
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(ARTIKEL_COL, Article.class)
        columnClasses.put(PURCHASE_COL, Integer.class)
        columnClasses.put(SALE_COL, Integer.class)
        columnClasses.put(LEFT_COL, Integer.class)
        columnClasses.put(PURCHASE_CN_COL, Integer.class)
        columnClasses.put(SALE_CN_COL, Integer.class)
        columnClasses.put(PROMO_COL, Integer.class)
    }

    void setColumnNames() {
        columnNames.put(ARTIKEL_COL, getBundle("Accounting").getString("ARTICLE_NAME"))
        columnNames.put(PURCHASE_COL, getBundle("Accounting").getString("PURCHASE"))
        columnNames.put(PURCHASE_CN_COL, getBundle("Accounting").getString("PURCHASE_CN"))
        columnNames.put(SALE_COL, getBundle("Accounting").getString("SALE"))
        columnNames.put(SALE_CN_COL, getBundle("Accounting").getString("SALE_CN"))
        columnNames.put(PROMO_COL, getBundle("Accounting").getString("PROMO"))
        columnNames.put(LEFT_COL, getBundle("Accounting").getString("ORDER_LEFT"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Article article = getObject(row,col)
        StockTransactions stockTransactions = accounting.stockTransactions
        if (col == ARTIKEL_COL) return article
        if (col == PURCHASE_COL) return stockTransactions.getNrPurchase(article)
        if (col == PURCHASE_CN_COL) return stockTransactions.getNrPurchaseCn(article)
        if (col == SALE_COL) return stockTransactions.getNrSale(article)
        if (col == SALE_CN_COL) return stockTransactions.getNrSaleCn(article)
        if (col == PROMO_COL) return stockTransactions.getNrPromo(article)
        if (col == LEFT_COL) return stockTransactions.getNrInStock(article)
        null
    }

    int getColumnCount() {
        NR_OF_COLS
    }

    int getRowCount() {
        accounting.articles?accounting.articles.getBusinessObjects(Article.withOrders()).size():0
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
        List<Article> businessObjects = accounting.articles.getBusinessObjects(Article.withOrders())
        businessObjects.get(row)
    }
}
