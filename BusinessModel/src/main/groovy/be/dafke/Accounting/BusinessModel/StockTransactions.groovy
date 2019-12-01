package be.dafke.Accounting.BusinessModel

class StockTransactions {
    Journal purchaseJournal
    Journal salesJournal
    Journal salesNoInvoiceJournal
    Journal gainJournal
    Account stockAccount
    Account gainAccount
    Account salesAccount
    Account salesGainAccount
    Account promoAccount

    protected final ArrayList<Order> orders = new ArrayList()

    ArrayList<Order> getOrders() {
        orders
    }

    void addOrder(Order order){
        orders.add(order)
        if(order instanceof PromoOrder){
            PromoOrder promoOrder = (PromoOrder) order
            promoOrder.businessObjects.forEach({ orderItem ->
                Article article = orderItem.article
                int numberOfItems = orderItem.numberOfItems
                article.setPromoOrderDelivered(numberOfItems)
            })
        } else if(order instanceof SalesOrder){
            SalesOrder salesOrder = (SalesOrder) order
            if(!salesOrder.creditNote){
                salesOrder.businessObjects.forEach({ orderItem ->
                    Article article = orderItem.article
                    int numberOfItems = orderItem.numberOfItems
                    article.setSoDelivered(numberOfItems)
                })
            } else {
                salesOrder.businessObjects.forEach({ orderItem ->
                    Article article = orderItem.article
                    int numberOfItems = orderItem.numberOfItems
                    article.setSoCnDelivered(numberOfItems)
                })
            }
        } else if (order instanceof PurchaseOrder){
            PurchaseOrder purchaseOrder = (PurchaseOrder) order
            if(!purchaseOrder.creditNote) {
                purchaseOrder.businessObjects.forEach({ orderItem ->
                    Article article = orderItem.article
                    int numberOfItems = orderItem.numberOfItems
                    article.setPoDelivered(numberOfItems)
                })
            } else {
                purchaseOrder.businessObjects.forEach({ orderItem ->
                    Article article = orderItem.article
                    int numberOfItems = orderItem.numberOfItems
                    article.setPoCnDelivered(numberOfItems)
                })
            }
        } else if (order instanceof StockOrder){
            order.businessObjects.forEach({ orderItem ->
                Article article = orderItem.article
                int numberOfItems = orderItem.numberOfItems
                article.setStockOrderDelivered(numberOfItems)
            })
        }
    }

    Account getStockAccount() {
        stockAccount
    }

    void setStockAccount(Account stockAccount) {
        this.stockAccount = stockAccount
    }

    Journal getSalesJournal() {
        salesJournal
    }

    void setSalesJournal(Journal salesJournal) {
        this.salesJournal = salesJournal
    }

    Journal getSalesNoInvoiceJournal() {
        salesNoInvoiceJournal
    }

    void setSalesNoInvoiceJournal(Journal salesNoInvoiceJournal) {
        this.salesNoInvoiceJournal = salesNoInvoiceJournal
    }

    Journal getGainJournal() {
        gainJournal
    }

    void setGainJournal(Journal gainJournal) {
        this.gainJournal = gainJournal
    }

    Account getGainAccount() {
        gainAccount
    }

    void setGainAccount(Account gainAccount) {
        this.gainAccount = gainAccount
    }

    Account getSalesAccount() {
        salesAccount
    }

    void setSalesAccount(Account salesAccount) {
        this.salesAccount = salesAccount
    }

    Account getSalesGainAccount() {
        salesGainAccount
    }

    void setSalesGainAccount(Account salesGainAccount) {
        this.salesGainAccount = salesGainAccount
    }

    Account getPromoAccount() {
        promoAccount
    }

    void setPromoAccount(Account promoAccount) {
        this.promoAccount = promoAccount
    }

    Journal getPurchaseJournal() {
        purchaseJournal
    }

    void setPurchaseJournal(Journal purchaseJournal) {
        this.purchaseJournal = purchaseJournal
    }
}
