package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PromoOrder
import be.dafke.Accounting.BusinessModel.PromoOrders
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class PromoOrdersOverviewDataTableModel extends SelectableTableModel<PromoOrder> {
    static int ORDER_NR_COL = 0
    static int DATE_COL = 1
    static int TOTAL_VALUE_COL = 2
    static int NR_OF_COL = 3
    protected HashMap<Integer,String> columnNames = new HashMap<>()
    protected HashMap<Integer,Class> columnClasses = new HashMap<>()

    PromoOrdersOverviewDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    protected void setColumnClasses() {
        columnClasses.put(ORDER_NR_COL, String.class)
        columnClasses.put(DATE_COL, String.class)
        columnClasses.put(TOTAL_VALUE_COL, BigDecimal.class)
    }

    protected void setColumnNames() {
        columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("ORDER_NR"))
        columnNames.put(DATE_COL, getBundle("Accounting").getString("DELIVERY_DATE"))
        columnNames.put(TOTAL_VALUE_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"))
    }

    int getColumnCount() {
        NR_OF_COL
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
        // No editable cells !
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if (Session.activeAccounting.promoOrders == null) return null
        PromoOrder promoOrder = getObject(row, col)
        if(promoOrder == null) return null
        if (col == TOTAL_VALUE_COL) return promoOrder.totalStockValue
        if (col == ORDER_NR_COL) return promoOrder.name
        if (col == DATE_COL) return promoOrder.deliveryDate
        null
    }

    @Override
    int getRowCount() {
        Session.activeAccounting.promoOrders?.businessObjects?.size()?:0
    }

    @Override
    PromoOrder getObject(int row, int col) {
        List<PromoOrder> businessObjects = Session.activeAccounting.promoOrders.businessObjects
        if(businessObjects == null || businessObjects.size() == 0) return null
        businessObjects.get(row)
    }
}
