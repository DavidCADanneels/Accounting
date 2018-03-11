package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;
import org.apache.xpath.operations.Or;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class PurchaseOrderCreateDataTableModel extends SelectableTableModel<OrderItem> {
	private final OrderItems orderItems;
	public static int NR_COL = 0;
	public static int NAME_COL = 1;
	public static int HS_COL = 2;
	public static int PRICE_COL = 3;
	public static int VAT_COL = 4;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private Contact contact;
	private Order order;
	private Predicate<OrderItem> filter;
	private PurchaseTotalsPanel purchaseTotalsPanel;

	public PurchaseOrderCreateDataTableModel(Order orderItems, Contact contact, Order order, PurchaseTotalsPanel purchaseTotalsPanel) {
		this.purchaseTotalsPanel = purchaseTotalsPanel;
		this.order = order;
		this.orderItems = orderItems;
		this.contact = contact;
		setColumnNames();
		setColumnClasses();
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
		OrderItem orderItem = getObject(row, col);
		Article article = orderItem.getArticle();
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
			return order.getUnitsInStock(article);
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		if(orderItems==null || contact==null || filter==null) return 0;
		List<OrderItem> businessObjects = orderItems.getBusinessObjects(filter);
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
		OrderItem orderItem = getObject(row,col);
		if(col == NR_COL){
			int nr = (Integer) value;
			orderItem.setNumberOfUnits(nr);
			orderItem.calculateNumberOfItems();
			purchaseTotalsPanel.fireOrderContentChanged(order);
		}
	}

	@Override
	public OrderItem getObject(int row, int col) {
		if(contact==null || filter==null) return null;
		List<OrderItem> orderItemList = orderItems.getBusinessObjects(filter);
		if(orderItemList == null || orderItemList.size() == 0) return null;
		OrderItem orderItem = orderItemList.get(row);
		Article article = orderItem.getArticle();
		return order.getBusinessObject(article.getName());
	}

	public void setContact(Contact contact) {
		this.contact = contact;
		filter = OrderItem.ofSupplier(contact);
		order.setSupplier(contact);
		fireTableDataChanged();
	}

	public void setOrder(Order order) {
		this.order = order;
		fireTableDataChanged();
	}
}