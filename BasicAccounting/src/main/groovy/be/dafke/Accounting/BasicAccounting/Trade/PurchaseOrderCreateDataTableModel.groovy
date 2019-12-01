package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder

import java.util.function.Predicate

class PurchaseOrderCreateDataTableModel extends PurchaseOrderViewDataTableModel {
    private final Articles articles
    private Contact contact
    private Predicate<Article> filter
    private TotalsPanel purchaseTotalsPanel

    PurchaseOrderCreateDataTableModel(Articles articles, Contact contact, PurchaseOrder order, TotalsPanel purchaseTotalsPanel) {
        super()
        this.purchaseTotalsPanel = purchaseTotalsPanel
        this.order = order
        this.articles = articles
        this.contact = contact
    }
    @Override
    boolean isCellEditable(int row, int col) {
        (col == NR_OF_UNITS_COL || col == PRICE_UNIT_COL || col == ITEMS_PER_UNIT_COL)
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        OrderItem orderItem = getObject(row,col)
        if(col == NR_OF_UNITS_COL){
            int nr = (Integer) value
            orderItem.setNumberOfUnits(nr)
            orderItem.calculateNumberOfItems()
        } else if(col == ITEMS_PER_UNIT_COL){
            int nr = (Integer) value
            orderItem.setItemsPerUnit(nr)
            orderItem.calculateNumberOfItems()
        } else if(col == PRICE_UNIT_COL){
            BigDecimal purchasePriceForUnit = (BigDecimal) value
            orderItem.setPurchasePriceForUnit(purchasePriceForUnit)
        }
        order.setOrderItem(orderItem)
        purchaseTotalsPanel.fireOrderContentChanged(order)
        fireTableDataChanged()
    }

    int getRowCount() {
        if(articles==null || contact==null || filter==null) 0
        List<Article> businessObjects = articles.getBusinessObjects(filter)
        if(businessObjects == null || businessObjects.size() == 0) 0
        businessObjects.size()
    }

    @Override
    OrderItem getObject(int row, int col) {
        if(contact==null || filter==null) null
        List<Article> articleList = articles.getBusinessObjects(filter)
        if(articleList == null || articleList.size() == 0) null
        Article article = articleList.get(row)
        order.getBusinessObject(article.getName())
    }

    void setContact(Contact contact) {
        this.contact = contact
        filter = Article.ofSupplier(contact)
        order.setSupplier(contact)
        fireTableDataChanged()
    }
}
