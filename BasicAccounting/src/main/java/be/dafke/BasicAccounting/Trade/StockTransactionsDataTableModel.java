package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class StockTransactionsDataTableModel extends SelectableTableModel<OrderItem> {
	private final StockTransactions stockTransactions;
	public static int ORDER_COL = 0;
	public static int ARTIKEL_COL = 1;
	public static int ADDED_COL = 2;
	public static int REMOVED_COL = 3;
	public static int NR_OF_COLS = 4;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();

	public StockTransactionsDataTableModel(StockTransactions stockTransactions) {
		this.stockTransactions = stockTransactions;
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(ORDER_COL, OrderItems.class);
		columnClasses.put(ARTIKEL_COL, Article.class);
		columnClasses.put(ADDED_COL, Integer.class);
		columnClasses.put(REMOVED_COL, Integer.class);
	}

	private void setColumnNames() {
		columnNames.put(ORDER_COL, getBundle("Accounting").getString("ORDER"));
		columnNames.put(ARTIKEL_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(ADDED_COL, getBundle("Accounting").getString("ORDER_ADDED"));
		columnNames.put(REMOVED_COL, getBundle("Accounting").getString("ORDER_REMOVED"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		OrderItem orderItem = getObject(row,col);
		Article article = orderItem.getArticle();

		if (col == ARTIKEL_COL) {
			return article;
		}
		Order order = orderItem.getOrder();
		if (col == ORDER_COL) {
			return order;
		}
		if (col == ADDED_COL) {
			if(order!=null && order instanceof PurchaseOrder) {
				return orderItem.getNumberOfItems();
			} else return null;
		}
		if (col == REMOVED_COL) {
			if(order!=null && order instanceof SalesOrder) {
				return orderItem.getNumberOfItems();
			} else return null;
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COLS;
	}

	public int getRowCount() {
        if(stockTransactions == null){
            return 0;
        }
        int size = 0;
        for(Order order:stockTransactions.getOrders()){
        	size += order.getBusinessObjects().size();
		}
		return size;
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

	private ArrayList<OrderItem> getAllItems(){
		ArrayList<OrderItem> orderItems = new ArrayList<>();
		for(Order order : stockTransactions.getOrders()){
			orderItems.addAll(order.getBusinessObjects());
		}
		return orderItems;
	}
	
	@Override
	public OrderItem getObject(int row, int col) {
		if(stockTransactions == null) return null;
		ArrayList<OrderItem> orderItems = getAllItems();
		return orderItems.get(row);
	}

	private int getRowInList(ArrayList<OrderItem> list, OrderItem booking){
		int row = 0;
		for(OrderItem search:list){
			if(search!=booking){
				row++;
			} else{
				return row;
			}
		}
		return -1;
	}

	public int getRow(OrderItem orderItem) {
		if(stockTransactions == null) return -1;
		ArrayList<OrderItem> orderItems = getAllItems();
		return getRowInList(orderItems, orderItem);
	}
}