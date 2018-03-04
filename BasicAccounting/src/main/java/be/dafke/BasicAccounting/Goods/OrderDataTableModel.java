package be.dafke.BasicAccounting.Goods;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.Articles;
import be.dafke.BusinessModel.Contact;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class OrderDataTableModel extends SelectableTableModel<Article> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Articles articles;
	public static int NAME_COL = 0;
	public static int HS_COL = 1;
	public static int PRICE_COL = 2;
	public static int VAT_COL = 3;
	public static int SUPPLIER_COL = 4;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private Contact supplier;

	public OrderDataTableModel(Articles articles, Contact supplier) {
		this.articles = articles;
		this.supplier = supplier;
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(HS_COL, String.class);
		columnClasses.put(PRICE_COL, BigDecimal.class);
		columnClasses.put(VAT_COL, Integer.class);
		columnClasses.put(SUPPLIER_COL, Contact.class);
	}

	private void setColumnNames() {
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(HS_COL, getBundle("Accounting").getString("ARTICLE_HS"));
		columnNames.put(PRICE_COL, getBundle("Accounting").getString("ARTICLE_PRICE"));
		columnNames.put(VAT_COL, getBundle("Accounting").getString("ARTICLE_VAT"));
		columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Article article = getObject(row, col);
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
		if (col == SUPPLIER_COL) {
			return article.getSupplier();
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		if(articles == null || supplier==null) return 0;
		List<Article> businessObjects = articles.getBusinessObjects(Article.ofSupplier(supplier));
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
		return true;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Article article = getObject(row,col);
		if(col == PRICE_COL){
            article.setPurchasePrice((BigDecimal) value);
		}
		if(col == HS_COL){
            article.setHSCode((String) value);
		}
		if(col == VAT_COL){
            article.setVatRate((Integer) value);
		}
		if(col == SUPPLIER_COL){
            article.setSupplier((Contact) value);
		}
		if(col == NAME_COL) {
//            article.setName((String) value);
			String oldName = article.getName();
			String newName = (String) value;
			if (newName != null && !oldName.trim().equals(newName.trim())) {
				try {
					articles.modifyName(oldName, newName);
				} catch (DuplicateNameException e) {
					ActionUtils.showErrorMessage(ActionUtils.ARTICLE_DUPLICATE_NAME, newName.trim());
				} catch (EmptyNameException e) {
					ActionUtils.showErrorMessage(ActionUtils.ARTICLE_NAME_EMPTY);
				}
			}
		}
	}

	@Override
	public Article getObject(int row, int col) {
		if(supplier==null) return null;
		List<Article> businessObjects = articles.getBusinessObjects(Article.ofSupplier(supplier));
		if(businessObjects == null || businessObjects.size() == 0) return null;
		return businessObjects.get(row);
	}

	public void setSupplier(Contact supplier) {
		this.supplier = supplier;
		fireTableDataChanged();
	}
}