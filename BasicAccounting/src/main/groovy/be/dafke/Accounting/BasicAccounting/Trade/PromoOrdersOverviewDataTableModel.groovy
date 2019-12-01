package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PromoOrder
import be.dafke.Accounting.BusinessModel.PromoOrders
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class PromoOrdersOverviewDataTableModel extends SelectableTableModel<PromoOrder> {
    static int ORDER_NR_COL = 0
    static int DATE_COL = 1
    static int TOTAL_VALUE_COL = 2
    static int NR_OF_COL = 3
    protected HashMap<Integer,String> columnNames = new HashMap<>()
    protected HashMap<Integer,Class> columnClasses = new HashMap<>()

    private PromoOrders promoOrders

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
        if (promoOrders == null) null
        PromoOrder promoOrder = getObject(row, col)
        if(promoOrder == null) null

        if (col == TOTAL_VALUE_COL) {
            promoOrder.getTotalStockValue()
        }
        if (col == ORDER_NR_COL) {
            promoOrder.getName()
        }
        if (col == DATE_COL) {
            promoOrder.getDeliveryDate()
        }
        null
    }

    @Override
    int getRowCount() {
        if(promoOrders == null) 0
        List<PromoOrder> businessObjects = promoOrders.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) 0
        businessObjects.size()
    }

    @Override
    PromoOrder getObject(int row, int col) {
        List<PromoOrder> businessObjects = this.promoOrders.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) null
        businessObjects.get(row)
    }

    void setAccounting(Accounting accounting) {
        promoOrders = accounting.getPromoOrders()
        fireTableDataChanged()
    }
}
