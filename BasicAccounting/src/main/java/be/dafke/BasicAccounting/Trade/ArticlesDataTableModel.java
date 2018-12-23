package be.dafke.BasicAccounting.Trade;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class ArticlesDataTableModel extends SelectableTableModel<Article> {
	private final Articles articles;
	public static int UNIT_NAME_COL = 0;
	public static int ITEM_NAME_COL = 1;
	public static int ITEMS_PER_UNIT_COL = 2;
	public static int HS_COL = 3;
	public static int PURCHASE_PRICE_COL = 4;
	public static int PURCHASE_VAT_COL = 5;
	public static int SUPPLIER_COL = 6;
	public static int SALE_ITEM_EXCL_COL = 7;
	public static int SALE_UNIT_EXCL_COL = 8;
	public static int SALES_VAT_COL = 9;
	public static int SALE_ITEM_INCL_COL = 10;
	public static int SALE_UNIT_INCL_COL = 11;
	public static int GAIN_ON_ITEM_COL = 12;
	public static int GAIN_ON_UNIT_COL = 13;
	public static int NR_OF_COL = 14;
	private final Component parent;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private List<Integer> editableColumns = new ArrayList<>();

	public ArticlesDataTableModel(Component parent, Articles articles) {
		this.parent = parent;
		this.articles = articles;
		setColumnNames();
		setColumnClasses();
		setEditableColumns();
	}

	private void setEditableColumns() {
		editableColumns.add(UNIT_NAME_COL);
		editableColumns.add(ITEM_NAME_COL);
		editableColumns.add(ITEMS_PER_UNIT_COL);
		editableColumns.add(HS_COL);
		editableColumns.add(PURCHASE_PRICE_COL);
		editableColumns.add(PURCHASE_VAT_COL);
		editableColumns.add(SALES_VAT_COL);
		editableColumns.add(SUPPLIER_COL);
		editableColumns.add(SALE_ITEM_INCL_COL);
		editableColumns.add(SALE_UNIT_INCL_COL);
	}

	private void setColumnClasses() {
		columnClasses.put(UNIT_NAME_COL, String.class);
		columnClasses.put(ITEM_NAME_COL, String.class);
		columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class);
		columnClasses.put(HS_COL, String.class);
		columnClasses.put(PURCHASE_PRICE_COL, BigDecimal.class);
		columnClasses.put(PURCHASE_VAT_COL, Integer.class);
		columnClasses.put(SALES_VAT_COL, Integer.class);
		columnClasses.put(SUPPLIER_COL, Contact.class);
		columnClasses.put(SALE_ITEM_EXCL_COL, BigDecimal.class);
		columnClasses.put(SALE_UNIT_EXCL_COL, BigDecimal.class);
		columnClasses.put(SALE_ITEM_INCL_COL, BigDecimal.class);
		columnClasses.put(SALE_UNIT_INCL_COL, BigDecimal.class);
		columnClasses.put(GAIN_ON_ITEM_COL, BigDecimal.class);
		columnClasses.put(GAIN_ON_UNIT_COL, BigDecimal.class);
	}

	private void setColumnNames() {
		columnNames.put(UNIT_NAME_COL, getBundle("Accounting").getString("ARTICLE_UNIT_NAME"));
		columnNames.put(ITEM_NAME_COL, getBundle("Accounting").getString("ARTICLE_ITEM_NAME"));
		columnNames.put(ITEMS_PER_UNIT_COL, getBundle("Accounting").getString("ITEMS_PER_UNIT"));
		columnNames.put(HS_COL, getBundle("Accounting").getString("ARTICLE_HS"));
		columnNames.put(PURCHASE_PRICE_COL, getBundle("Accounting").getString("PURCHASE_PRICE"));
		columnNames.put(PURCHASE_VAT_COL, getBundle("Accounting").getString("PURCHASE_VAT"));
		columnNames.put(SALES_VAT_COL, getBundle("Accounting").getString("SALES_VAT"));
		columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"));
		columnNames.put(SALE_ITEM_EXCL_COL, getBundle("Accounting").getString("SALE_ITEM_EXCL"));
		columnNames.put(SALE_UNIT_EXCL_COL, getBundle("Accounting").getString("SALE_UNIT_EXCL"));
		columnNames.put(SALE_ITEM_INCL_COL, getBundle("Accounting").getString("SALE_ITEM_INCL"));
		columnNames.put(SALE_UNIT_INCL_COL, getBundle("Accounting").getString("SALE_UNIT_INCL"));
		columnNames.put(GAIN_ON_ITEM_COL, getBundle("Accounting").getString("GAIN_ON_ITEM"));
		columnNames.put(GAIN_ON_UNIT_COL, getBundle("Accounting").getString("GAIN_ON_UNIT"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Article article = getObject(row, col);
		if(article==null) return null;
		if (col == UNIT_NAME_COL) {
			return article.getName();
		}
		if (col == ITEM_NAME_COL) {
			return article.getItemName();
		}
		if (col == PURCHASE_VAT_COL) {
			return article.getPurchaseVatRate();
		}
		if (col == SALES_VAT_COL) {
			return article.getSalesVatRate();
		}
		if (col == HS_COL) {
			return article.getHSCode();
		}
		if (col == PURCHASE_PRICE_COL) {
			return article.getPurchasePrice();
		}
		if (col == SUPPLIER_COL) {
			return article.getSupplier();
		}
		if (col == ITEMS_PER_UNIT_COL) {
			return article.getItemsPerUnit();
		}
		if (col == SALE_ITEM_EXCL_COL) {
			BigDecimal salesPriceSingleWithoutVat = article.getSalesPriceItemWithoutVat();
			return salesPriceSingleWithoutVat!=null?salesPriceSingleWithoutVat:BigDecimal.ZERO;
		}
		if (col == SALE_UNIT_EXCL_COL) {
			BigDecimal salesPriceUnitWithoutVat = article.getSalesPriceUnitWithoutVat();
			return salesPriceUnitWithoutVat!=null?salesPriceUnitWithoutVat:BigDecimal.ZERO;
		}
		if (col == SALE_ITEM_INCL_COL) {
			BigDecimal salesPriceSingleWithVat = article.getSalesPriceItemWithVat();
			return salesPriceSingleWithVat!=null?salesPriceSingleWithVat:BigDecimal.ZERO;
		}
		if (col == SALE_UNIT_INCL_COL) {
			BigDecimal salesPricePromoWithVat = article.getSalesPriceUnitWithVat();
			return salesPricePromoWithVat!=null?salesPricePromoWithVat:BigDecimal.ZERO;
		}
		if (col == GAIN_ON_ITEM_COL) {
			BigDecimal salesPriceSingleWithoutVat = article.getSalesPriceItemWithoutVat();
			if(salesPriceSingleWithoutVat==null) return BigDecimal.ZERO;
			return article.getItemProfit(salesPriceSingleWithoutVat);
		}
		if (col == GAIN_ON_UNIT_COL) {
			BigDecimal salesPricePromoWithoutVat = article.getSalesPriceUnitWithoutVat();
			if(salesPricePromoWithoutVat==null) return BigDecimal.ZERO;
			return article.getUnitProfit(salesPricePromoWithoutVat);
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
        if(articles == null){
            return 0;
        }
		return articles.getBusinessObjects().size();
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
		return editableColumns.contains(col);
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Article article = getObject(row,col);
		if(col == PURCHASE_PRICE_COL){
			BigDecimal purchasePrice = (BigDecimal) value;
			if(purchasePrice.scale()<2)
				purchasePrice = purchasePrice.setScale(2);
			article.setPurchasePrice(purchasePrice);
		}
		if(col == HS_COL){
            article.setHSCode((String) value);
		}
		if(col == PURCHASE_VAT_COL){
            article.setPurchaseVatRate((Integer) value);
		}
		if(col == SALES_VAT_COL){
            article.setSalesVatRate((Integer) value);
		}
		if(col == SUPPLIER_COL){
            article.setSupplier((Contact) value);
		}
		if(col == ITEMS_PER_UNIT_COL){
            article.setItemsPerUnit((Integer) value);
		}
		if (col == SALE_ITEM_INCL_COL) {
			BigDecimal amount = (BigDecimal) value;
			article.setSalesPriceItemWithVat(amount.setScale(2));
		}
		if (col == SALE_UNIT_INCL_COL) {
			BigDecimal amount = (BigDecimal) value;
			article.setSalesPriceUnitWithVat(amount.setScale(2));
		}
		if(col == ITEM_NAME_COL){
            article.setItemName((String) value);
		}
		if(col == UNIT_NAME_COL) {
//            article.setName((String) value);
			String oldName = article.getName();
			String newName = (String) value;
			if (newName != null && !oldName.trim().equals(newName.trim())) {
				try {
					articles.modifyName(oldName, newName);
				} catch (DuplicateNameException e) {
					ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_DUPLICATE_NAME, newName.trim());
				} catch (EmptyNameException e) {
					ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_NAME_EMPTY);
				}
			}
		}
		fireTableDataChanged();
	}

	@Override
	public Article getObject(int row, int col) {
		return articles.getBusinessObjects().get(row);
	}
}