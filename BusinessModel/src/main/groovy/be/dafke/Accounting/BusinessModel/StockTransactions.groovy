package be.dafke.Accounting.BusinessModel

import java.util.function.Predicate

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


    HashMap<Article, Integer> stock = [:]
    HashMap<Article, Integer> sale = [:]
    HashMap<Article, Integer> saleCN = [:]
    HashMap<Article, Integer> purchase = [:]
    HashMap<Article, Integer> purchaseCN = [:]
    HashMap<Article, Integer> promos = [:]

    protected final ArrayList<Order> orders =  []

    ArrayList<Order> getOrders() {
        orders
    }

    HashMap<Article, Integer> getStock() {
        return stock
    }

    List<Article> getArticles(){
        stock.keySet().collect()
    }

    List<Article> getArticlesWithTransactions(){

    }

    Integer getNrInStock(Article article){
        stock.get(article, 0)
    }

    Integer getNrSale(Article article){
        sale.get(article, 0)
    }

    Integer getNrSaleCn(Article article){
        saleCN.get(article, 0)
    }

    Integer getNrPurchase(Article article){
        purchase.get(article, 0)
    }

    Integer getNrPurchaseCn(Article article){
        purchaseCN.get(article, 0)
    }

    Integer getNrPromo(Article article){
        promos.get(article, 0)
    }

    void removeFromStock(Article article, Integer number){
        int currentNumber = stock.get(article, 0)
        int result = currentNumber - number
        if (result == 0) stock.remove(article)
        else stock.put(article, result)
    }

    void addToStock(Article article, Integer number) {
        int currentNumber = stock.get(article, 0)
        stock.put(article, currentNumber + number)
    }

    void addToPurchase(Article article, Integer number) {
        int currentNumber = purchase.get(article, 0)
        purchase.put(article, currentNumber + number)
    }

    void addToPurchaseCn(Article article, Integer number) {
        int currentNumber = purchaseCN.get(article, 0)
        purchaseCN.put(article, currentNumber + number)
    }

    void addToSale(Article article, Integer number) {
        int currentNumber = sale.get(article, 0)
        sale.put(article, currentNumber + number)
    }

    void addToSaleCn(Article article, Integer number) {
        int currentNumber = saleCN.get(article, 0)
        saleCN.put(article, currentNumber + number)
    }

    void addToPromo(Article article, Integer number) {
        int currentNumber = promos.get(article, 0)
        promos.put(article, currentNumber + number)
    }

    void addOrder(Order order){
        orders.add(order)
        if(order instanceof PromoOrder){
            PromoOrder promoOrder = (PromoOrder) order
            promoOrder.businessObjects.forEach({ orderItem ->
                Article article = orderItem.article
                int numberOfItems = orderItem.numberOfItems
                addToPromo(article, numberOfItems)
                removeFromStock(article, numberOfItems)
            })
        } else if(order instanceof SalesOrder){
            SalesOrder salesOrder = (SalesOrder) order
            if(!salesOrder.creditNote){
                salesOrder.businessObjects.forEach({ orderItem ->
                    Article article = orderItem.article
                    int numberOfItems = orderItem.numberOfItems
                    addToSale(article, numberOfItems)
                    removeFromStock(article, numberOfItems)
                })
            } else {
                salesOrder.businessObjects.forEach({ orderItem ->
                    Article article = orderItem.article
                    int numberOfItems = orderItem.numberOfItems
                    addToSaleCn(article, numberOfItems)
                    addToStock(article, numberOfItems)
                })
            }
        } else if (order instanceof PurchaseOrder){
            PurchaseOrder purchaseOrder = (PurchaseOrder) order
            if(!purchaseOrder.creditNote) {
                purchaseOrder.businessObjects.forEach({ orderItem ->
                    Article article = orderItem.article
                    int numberOfItems = orderItem.numberOfItems
                    addToPurchase(article, numberOfItems)
                    addToStock(article, numberOfItems)
                })
            } else {
                purchaseOrder.businessObjects.forEach({ orderItem ->
                    Article article = orderItem.article
                    int numberOfItems = orderItem.numberOfItems
                    addToPurchaseCn(article, numberOfItems)
                    removeFromStock(article, numberOfItems)
                })
            }
        } else if (order instanceof StockOrder){
            order.businessObjects.forEach({ orderItem ->
                Article article = orderItem.article
                int numberOfItems = orderItem.numberOfItems
                addToStock(article, numberOfItems)
            })
        }
    }

    static Predicate<StockTransactions> inStock(Article article){
        { stockTransactions -> stockTransactions.stock.get(article,0) > 0 }
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
