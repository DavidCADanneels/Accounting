package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class StockDataTableModel extends SelectableTableModel<Article> {
	private final Articles articles;
	public static int UNITS_IN_STOCK_COL = 0;
	public static int ITEMS_PER_UNIT_COL = 1;
	public static int ITEMS_IN_STOCK_COL = 2;
	public static int ARTIKEL_COL = 3;
	public static int SUPPLIER_COL = 4;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();

	public StockDataTableModel(Articles articles) {
		this.articles = articles;
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(UNITS_IN_STOCK_COL, Integer.class);
		columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class);
		columnClasses.put(ITEMS_IN_STOCK_COL, Integer.class);
		columnClasses.put(ARTIKEL_COL, Article.class);
		columnClasses.put(SUPPLIER_COL, Contact.class);
	}

	private void setColumnNames() {
		columnNames.put(UNITS_IN_STOCK_COL, getBundle("Accounting").getString("UNITS_IN_STOCK"));
		columnNames.put(ITEMS_IN_STOCK_COL, getBundle("Accounting").getString("ITEMS_IN_STOCK"));
		columnNames.put(ITEMS_PER_UNIT_COL, getBundle("Accounting").getString("ITEMS_PER_UNIT"));
		columnNames.put(ARTIKEL_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Article article = getObject(row,col);
		if(article == null) return null;
		if (col == UNITS_IN_STOCK_COL) {
			return article.getNrInStock()/article.getItemsPerUnit();
		}
		if (col == ITEMS_IN_STOCK_COL) {
			return article.getNrInStock();
		}
		if (col == ITEMS_PER_UNIT_COL) {
			return article.getItemsPerUnit();
		}
		if (col == ARTIKEL_COL) {
			return article;
		}
		if (col == SUPPLIER_COL) {
			return article.getSupplier();
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.size();
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