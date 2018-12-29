package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class StockBalanceDataTableModel extends SelectableTableModel<Article> {
	private final Articles articles;
	public static int ARTIKEL_COL = 0;
	public static int PO_ORDERED_COL = 1;
	public static int ADDED_COL = 2;
	public static int SO_ORDERED_COL = 3;
	public static int REMOVED_COL = 4;
	public static int LEFT_COL = 5;
	public static int NR_OF_COLS = 6;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();

	public StockBalanceDataTableModel(Accounting accounting) {
		this.articles = accounting.getArticles();
//		accounting.getS
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(ARTIKEL_COL, Article.class);
		columnClasses.put(ADDED_COL, Integer.class);
		columnClasses.put(REMOVED_COL, Integer.class);
		columnClasses.put(LEFT_COL, Integer.class);
		columnClasses.put(PO_ORDERED_COL, Integer.class);
		columnClasses.put(SO_ORDERED_COL, Integer.class);
	}

	private void setColumnNames() {
		columnNames.put(ARTIKEL_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(ADDED_COL, getBundle("Accounting").getString("ORDER_ADDED"));
		columnNames.put(REMOVED_COL, getBundle("Accounting").getString("ORDER_REMOVED"));
		columnNames.put(LEFT_COL, getBundle("Accounting").getString("ORDER_LEFT"));
		columnNames.put(PO_ORDERED_COL, getBundle("Accounting").getString("PO_ORDERED"));
		columnNames.put(SO_ORDERED_COL, getBundle("Accounting").getString("SO_ORDERED"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Article article = getObject(row,col);
		if (col == ARTIKEL_COL) {
			return article;
		}
		if (col == ADDED_COL) {
			return article.getNrAdded();
		}
		if (col == REMOVED_COL) {
			return article.getNrRemoved();
		}
		if (col == PO_ORDERED_COL) {
			return article.getNrOrderedByPO();
		}
		if (col == SO_ORDERED_COL) {
			return article.getNrOrderedForSO();
		}
		if (col == LEFT_COL) {
			return article.getNrInStock();
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COLS;
	}

	public int getRowCount() {
		if(articles == null){
			return 0;
		}
		return articles.getBusinessObjects(Article.withOrders()).size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		// no editable fields
	}

	@Override
	public Article getObject(int row, int col) {
        List<Article> businessObjects = articles.getBusinessObjects(Article.withOrders());
        return businessObjects.get(row);
	}
}