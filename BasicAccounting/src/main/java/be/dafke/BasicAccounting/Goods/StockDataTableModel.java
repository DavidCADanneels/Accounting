package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class StockDataTableModel extends SelectableTableModel<OrderItem> {
	private final Stock stock;
	public static int NR_IN_STOCK_COL = 0;
	public static int ARTIKEL_COL = 1;
	public static int SUPPLIER_COL = 2;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();

	public StockDataTableModel(Stock stock) {
		this.stock = stock;
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NR_IN_STOCK_COL, Integer.class);
		columnClasses.put(ARTIKEL_COL, Article.class);
		columnClasses.put(SUPPLIER_COL, Contact.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_IN_STOCK_COL, getBundle("Accounting").getString("NR_IN_STOCK"));
		columnNames.put(ARTIKEL_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		OrderItem orderItem = getObject(row,col);
		if (col == NR_IN_STOCK_COL) {
			return orderItem.getNumber();
		}
		if (col == ARTIKEL_COL) {
			return orderItem.getArticle();
		}
		if (col == SUPPLIER_COL) {
			Article article = orderItem.getArticle();
			return article==null?null:article.getSupplier();
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
        if(stock == null){
            return 0;
        }
		return stock.getBusinessObjects().size();
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
		OrderItem orderItem = getObject(row,col);
		if(col == ARTIKEL_COL){
			// non-editable
		}
		if(col == SUPPLIER_COL){
            // non-editable
		}
		if(col == NR_IN_STOCK_COL) {
			// non-editable
		}
	}

	@Override
	public OrderItem getObject(int row, int col) {
		return stock.getBusinessObjects().get(row);
	}
}