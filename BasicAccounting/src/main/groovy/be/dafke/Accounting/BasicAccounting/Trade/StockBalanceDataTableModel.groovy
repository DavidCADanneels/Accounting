package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class StockBalanceDataTableModel extends SelectableTableModel<Article> {
    final Articles articles
    static int ARTIKEL_COL = 0
    static int PO_ORDERED_COL = 1
    static int ADDED_COL = 2
    static int SO_ORDERED_COL = 3
    static int REMOVED_COL = 4
    static int LEFT_COL = 5
    static int NR_OF_COLS = 6
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    StockBalanceDataTableModel(Accounting accounting) {
        this.articles = accounting.articles
//		accounting.getS
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(ARTIKEL_COL, Article.class)
        columnClasses.put(ADDED_COL, Integer.class)
        columnClasses.put(REMOVED_COL, Integer.class)
        columnClasses.put(LEFT_COL, Integer.class)
        columnClasses.put(PO_ORDERED_COL, Integer.class)
        columnClasses.put(SO_ORDERED_COL, Integer.class)
    }

    void setColumnNames() {
        columnNames.put(ARTIKEL_COL, getBundle("Accounting").getString("ARTICLE_NAME"))
        columnNames.put(ADDED_COL, getBundle("Accounting").getString("ORDER_ADDED"))
        columnNames.put(REMOVED_COL, getBundle("Accounting").getString("ORDER_REMOVED"))
        columnNames.put(LEFT_COL, getBundle("Accounting").getString("ORDER_LEFT"))
        columnNames.put(PO_ORDERED_COL, getBundle("Accounting").getString("PO_ORDERED"))
        columnNames.put(SO_ORDERED_COL, getBundle("Accounting").getString("SO_ORDERED"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Article article = getObject(row,col)
        if (col == ARTIKEL_COL) return article
        if (col == ADDED_COL) return article.getNrAdded()
        if (col == REMOVED_COL) return article.getNrRemoved()
        if (col == PO_ORDERED_COL) return article.getNrOrderedByPO()
        if (col == SO_ORDERED_COL) return article.getNrOrderedForSO()
        if (col == LEFT_COL) return article.getNrInStock()
        null
    }

    int getColumnCount() {
        NR_OF_COLS
    }

    int getRowCount() {
        articles?articles.getBusinessObjects(Article.withOrders()).size():0
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
        List<Article> businessObjects = articles.getBusinessObjects(Article.withOrders())
        businessObjects.get(row)
    }
}
