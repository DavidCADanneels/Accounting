package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class SalesOrderDataTableModel extends SelectableTableModel<StockItem> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Articles articles;
	public static int NR_COL = 0;
	public static int NAME_COL = 1;
	public static int HS_COL = 2;
	public static int PRICE_COL = 3;
	public static int VAT_COL = 4;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private Contact contact;
	private Order order;

	public SalesOrderDataTableModel(Articles articles, Contact contact) {
		this.articles = articles;
		this.contact = contact;
		setColumnNames();
		setColumnClasses();
		order = new Order(articles);
	}

	private void setColumnClasses() {
		columnClasses.put(NR_COL, Integer.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(HS_COL, String.class);
		columnClasses.put(PRICE_COL, BigDecimal.class);
		columnClasses.put(VAT_COL, Integer.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_COL, getBundle("Accounting").getString("NR_TO_ORDER"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(HS_COL, getBundle("Accounting").getString("ARTICLE_HS"));
		columnNames.put(PRICE_COL, getBundle("Accounting").getString("ARTICLE_PRICE"));
		columnNames.put(VAT_COL, getBundle("Accounting").getString("ARTICLE_VAT"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		StockItem stockItem = getObject(row, col);
		Article article = stockItem.getArticle();
		if (article == null) return null;

		if (col == NAME_COL) {
			return article.getName();
		}
		if (col == VAT_COL) {
			return article.getVatRate();
		}
		if (col == HS_COL) {
			return article.getHSCode();
		}
		if (col == PRICE_COL) {
			return article.getPurchasePrice();
		}
		if (col == NR_COL) {
			if (order==null) return null;
			StockItem item = order.getBusinessObject(article);
			return item==null?0:item.getNumber();
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		if(articles==null || contact==null) return 0;
		List<Article> businessObjects = articles.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
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
		return col==NR_COL;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		StockItem stockItem = getObject(row,col);
		if(col == NR_COL){
			int nr = (Integer) value;
			stockItem.setNumber(nr);
			order.setItem(stockItem);
		}
	}

	@Override
	public StockItem getObject(int row, int col) {
		if(contact==null) return null;
		List<Article> articleList = articles.getBusinessObjects();
		if(articleList == null || articleList.size() == 0) return null;
		Article article = articleList.get(row);
		return order.getBusinessObject(article);
	}

	public Order getOrder() {
		return order;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
		order.setCustomer(contact);
		fireTableDataChanged();
	}
}