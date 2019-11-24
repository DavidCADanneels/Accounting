package be.dafke.Accounting.BusinessModel

class StockTransactions {
    private Journal purchaseJournal
    private Journal salesJournal
    private Journal salesNoInvoiceJournal
    private Journal gainJournal
    private Account stockAccount
    private Account gainAccount
    private Account salesAccount
    private Account salesGainAccount
    private Account promoAccount

    protected final ArrayList<Order> orders = new ArrayList()

    ArrayList<Order> getOrders() {
        orders
    }

    void addOrder(Order order){
        orders.add(order)
        if(order instanceof PromoOrder){
            PromoOrder promoOrder = (PromoOrder) order
            promoOrder.getBusinessObjects().forEach({ orderItem ->
                Article article = orderItem.getArticle()
                int numberOfItems = orderItem.getNumberOfItems()
                article.setPromoOrderDelivered(numberOfItems)
            })
        } else if(order instanceof SalesOrder){
            SalesOrder salesOrder = (SalesOrder) order
            if(!salesOrder.isCreditNote()){
                salesOrder.getBusinessObjects().forEach({ orderItem ->
                    Article article = orderItem.getArticle()
                    int numberOfItems = orderItem.getNumberOfItems()
                    article.setSoDelivered(numberOfItems)
                })
            } else {
                salesOrder.getBusinessObjects().forEach({ orderItem ->
                    Article article = orderItem.getArticle()
                    int numberOfItems = orderItem.getNumberOfItems()
                    article.setSoCnDelivered(numberOfItems)
                })
            }
        } else if (order instanceof PurchaseOrder){
            PurchaseOrder purchaseOrder = (PurchaseOrder) order
            if(!purchaseOrder.isCreditNote()) {
                purchaseOrder.getBusinessObjects().forEach({ orderItem ->
                    Article article = orderItem.getArticle()
                    int numberOfItems = orderItem.getNumberOfItems()
                    article.setPoDelivered(numberOfItems)
                })
            } else {
                purchaseOrder.getBusinessObjects().forEach({ orderItem ->
                    Article article = orderItem.getArticle()
                    int numberOfItems = orderItem.getNumberOfItems()
                    article.setPoCnDelivered(numberOfItems)
                })
            }
        } else if (order instanceof StockOrder){
            order.getBusinessObjects().forEach({ orderItem ->
                Article article = orderItem.getArticle()
                int numberOfItems = orderItem.getNumberOfItems()
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
